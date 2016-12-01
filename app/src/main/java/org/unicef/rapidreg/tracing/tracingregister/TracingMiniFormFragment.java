package org.unicef.rapidreg.tracing.tracingregister;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.unicef.rapidreg.R;
import org.unicef.rapidreg.base.Feature;
import org.unicef.rapidreg.base.record.RecordActivity;
import org.unicef.rapidreg.base.record.recordphoto.RecordPhotoAdapter;
import org.unicef.rapidreg.base.record.recordregister.RecordRegisterAdapter;
import org.unicef.rapidreg.base.record.recordregister.RecordRegisterFragment;
import org.unicef.rapidreg.event.SaveTracingEvent;
import org.unicef.rapidreg.forms.Field;
import org.unicef.rapidreg.forms.RecordForm;
import org.unicef.rapidreg.forms.Section;
import org.unicef.rapidreg.model.Tracing;
import org.unicef.rapidreg.service.RecordService;
import org.unicef.rapidreg.service.TracingFormService;
import org.unicef.rapidreg.service.TracingPhotoService;
import org.unicef.rapidreg.service.TracingService;
import org.unicef.rapidreg.service.cache.ItemValues;
import org.unicef.rapidreg.service.cache.ItemValuesMap;
import org.unicef.rapidreg.tracing.TracingActivity;
import org.unicef.rapidreg.tracing.TracingFeature;
import org.unicef.rapidreg.tracing.tracingphoto.TracingPhotoAdapter;
import org.unicef.rapidreg.utils.JsonUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.OnClick;

public class TracingMiniFormFragment extends RecordRegisterFragment {

    public static final String TAG = TracingMiniFormFragment.class.getSimpleName();

    @Inject
    TracingRegisterPresenter tracingRegisterPresenter;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void saveTracing(SaveTracingEvent event) {
       tracingRegisterPresenter.saveRecord(getItemValues());
    }

    @NonNull
    @Override
    public TracingRegisterPresenter createPresenter() {
        return tracingRegisterPresenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        getComponent().inject(this);
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Field> fields = getFields();
        presenter.initContext(getActivity(), fields, true);
        initItemValues();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected List<Field> getFields() {
        List<Field> fields = new ArrayList<>();

        RecordForm form = TracingFormService.getInstance().getCurrentForm();
        List<Section> sections = form.getSections();

        for (Section section : sections) {
            for (Field field : section.getFields()) {
                if (field.isShowOnMiniForm()) {
                    if (field.isPhotoUploadBox()) {
                        fields.add(0, field);
                    } else {
                        fields.add(field);
                    }
                }
            }
        }
        if (!fields.isEmpty()) {
            addProfileFieldForDetailsPage(fields);
        }

        return fields;
    }

    @Override
    public void initView(RecordRegisterAdapter adapter) {
        super.initView(adapter);
        formSwitcher.setText(R.string.show_more_details);

        if (((RecordActivity) getActivity()).getCurrentFeature().isDetailMode()) {
            editButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void saveSuccessfully(long recordId) {
        Bundle args = new Bundle();
        args.putLong(TracingService.TRACING_PRIMARY_ID, recordId);
        Toast.makeText(getActivity(), R.string.save_success, Toast.LENGTH_SHORT).show();
        ((RecordActivity) getActivity()).turnToFeature(TracingFeature.DETAILS_MINI, args, null);
    }

    @OnClick(R.id.edit)
    public void onEditClicked() {
        Bundle args = new Bundle();
        args.putSerializable(RecordService.ITEM_VALUES, getItemValues());
        args.putStringArrayList(RecordService.RECORD_PHOTOS, (ArrayList<String>) getPhotos());
        ((TracingActivity) getActivity()).turnToFeature(TracingFeature.EDIT_MINI, args, null);
    }

    @OnClick(R.id.form_switcher)
    public void onSwitcherChecked() {
        Bundle args = new Bundle();
        args.putSerializable(RecordService.ITEM_VALUES, getItemValues());
        args.putStringArrayList(RecordService.RECORD_PHOTOS, (ArrayList<String>) getPhotos());
        Feature feature = ((RecordActivity) getActivity()).getCurrentFeature().isDetailMode() ?
                TracingFeature.DETAILS_FULL : ((RecordActivity) getActivity()).getCurrentFeature().isAddMode() ?
                TracingFeature.ADD_FULL : TracingFeature.EDIT_FULL;
        ((RecordActivity) getActivity()).turnToFeature(feature, args, ANIM_TO_FULL);
    }

    protected void initItemValues() {
        if (getArguments() != null) {
            recordId = getArguments().getLong(TracingService.TRACING_PRIMARY_ID, INVALID_RECORD_ID);
            if (recordId != INVALID_RECORD_ID) {
                Tracing item = tracingRegisterPresenter.getById(recordId);
                String tracingJson = new String(item.getContent().getBlob());
                try {
                    ItemValuesMap itemValues = new ItemValuesMap(JsonUtils.toMap(ItemValues.generateItemValues(tracingJson).getValues()));
                    itemValues.addStringItem(TracingService.TRACING_ID, item.getUniqueId());
                    setItemValues(itemValues);
                } catch (JSONException e) {
                    Log.e(TAG, "Json conversion error");
                }

                DateFormat dateFormat = SimpleDateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
                String shortUUID = RecordService.getShortUUID(item.getUniqueId());
                getItemValues().addStringItem(ItemValues.RecordProfile.ID_NORMAL_STATE, shortUUID);
                getItemValues().addStringItem(ItemValues.RecordProfile.REGISTRATION_DATE,
                        dateFormat.format(item.getRegistrationDate()));
                getItemValues().addNumberItem(ItemValues.RecordProfile.ID, item.getId());
                setPhotoAdapter(initPhotoAdapter(recordId));
            } else {
                ItemValuesMap itemValues = (ItemValuesMap) getArguments().getSerializable(ITEM_VALUES);
                setPhotoAdapter(new TracingPhotoAdapter(getContext(),
                        getArguments().getStringArrayList(RecordService.RECORD_PHOTOS)));
                setItemValues(itemValues);
            }
        } else {
            setItemValues(new ItemValuesMap());
            setPhotoAdapter(new TracingPhotoAdapter(getContext(), new ArrayList<String>()));
        }
    }

    private RecordPhotoAdapter initPhotoAdapter(long recordId) {
        List<String> paths = new ArrayList<>();
        List<Long> tracings = TracingPhotoService.getInstance().getIdsByTracingId(recordId);
        for (Long tracingId : tracings) {
            paths.add(String.valueOf(tracingId));
        }
        return new TracingPhotoAdapter(getContext(), paths);
    }
}