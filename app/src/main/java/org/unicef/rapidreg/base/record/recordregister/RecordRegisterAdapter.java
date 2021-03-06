package org.unicef.rapidreg.base.record.recordregister;

import android.content.Context;
import android.content.res.Resources;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unicef.rapidreg.base.record.RecordActivity;
import org.unicef.rapidreg.base.record.recordphoto.RecordPhotoAdapter;
import org.unicef.rapidreg.forms.Field;
import org.unicef.rapidreg.service.cache.ItemValuesMap;
import org.unicef.rapidreg.widgets.viewholder.AudioUploadViewHolder;
import org.unicef.rapidreg.widgets.viewholder.BaseViewHolder;
import org.unicef.rapidreg.widgets.viewholder.CustomViewHolder;
import org.unicef.rapidreg.widgets.viewholder.DateRangeViewHolder;
import org.unicef.rapidreg.widgets.viewholder.DefaultViewHolder;
import org.unicef.rapidreg.widgets.viewholder.GenericViewHolder;
import org.unicef.rapidreg.widgets.viewholder.IncidentMiniFormProfileViewHolder;
import org.unicef.rapidreg.widgets.viewholder.MiniFormProfileViewHolder;
import org.unicef.rapidreg.widgets.viewholder.PhotoUploadMiniFormViewHolder;
import org.unicef.rapidreg.widgets.viewholder.PhotoUploadViewHolder;
import org.unicef.rapidreg.widgets.viewholder.SingleLineRadioViewHolder;
import org.unicef.rapidreg.widgets.viewholder.SubFormViewHolder;
import org.unicef.rapidreg.widgets.viewholder.TextViewHolder;
import org.unicef.rapidreg.widgets.viewholder.TickBoxViewHolder;

import java.util.HashMap;
import java.util.List;

import static java.util.Collections.EMPTY_LIST;

public class RecordRegisterAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String LAYOUT = "layout";
    private static final String PREFIX = "form_";
    private static final int VIEW_HOLDER_GENERIC = 0;
    private static final int VIEW_HOLDER_TICK_BOX = 1;
    private static final int VIEW_HOLDER_PHOTO_UPLOAD_BOX = 2;
    private static final int VIEW_HOLDER_AUDIO_UPLOAD_BOX = 3;
    private static final int VIEW_HOLDER_SUBFORM = 4;
    private static final int VIEW_HOLDER_TEXT = 5;
    private static final int VIEW_HOLDER_TEXT_AREA = 6;
    private static final int VIEW_HOLDER_SELECT_BOX = 7;
    private static final int VIEW_HOLDER_RADIO_SINGLE_LINE = 8;
    private static final int VIEW_HOLDER_PHOTO_UPLOAD_BOX_MINI_FORM = 9;
    private static final int VIEW_HOLDER_MINI_FORM_PROFILE = 10;
    private static final int VIEW_HOLDER_INCIDENT_MINI_FORM_PROFILE = 11;
    private static final int VIEW_HOLDER_DATE_RANGE = 12;
    private static final int VIEW_HOLDER_CUSTOM = 13;

    private boolean isMiniForm;

    private List<Field> fields;
    private RecordActivity activity;
    protected LayoutInflater inflater;
    protected Resources resources;
    protected String packageName;
    private RecordPhotoAdapter adapter;

    private ItemValuesMap itemValues;
    private ItemValuesMap itemValuesVerifyList;

    private HashMap<Integer, List<Boolean>> subformDropDownStatus = new HashMap<>();

    public RecordRegisterAdapter(Context context, List<Field> fields, ItemValuesMap itemValues, ItemValuesMap itemValuesVerifyList, boolean isMiniForm) {
        this.fields = fields;
        this.activity = (RecordActivity) context;
        this.itemValues = itemValues;
        this.isMiniForm = isMiniForm;
        this.itemValuesVerifyList = itemValuesVerifyList;

        inflater = LayoutInflater.from(context);
        resources = context.getResources();
        packageName = context.getPackageName();
    }

    public void setPhotoAdapter(RecordPhotoAdapter adapter) {
        this.adapter = adapter;
    }

    public RecordPhotoAdapter getPhotoAdapter() {
        return adapter;
    }

    public void setItemValues(ItemValuesMap itemValues) {
        this.itemValues = itemValues;
    }

    public ItemValuesMap getFieldValueVerifyResult() {
        return itemValuesVerifyList;
    }

    public void setFieldValueVerifyResult(ItemValuesMap fieldValueVerifyResult) {
        this.itemValuesVerifyList = fieldValueVerifyResult;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_HOLDER_TEXT:
            case VIEW_HOLDER_TEXT_AREA:
                return new TextViewHolder(activity, inflater.inflate(resources
                        .getIdentifier(PREFIX + Field.TYPE_TEXT_FIELD,
                                LAYOUT, packageName), parent, false), itemValues);

            case VIEW_HOLDER_RADIO_SINGLE_LINE:
                return new SingleLineRadioViewHolder(activity, inflater.inflate(resources
                        .getIdentifier(PREFIX + Field.TYPE_SINGLE_LINE_RADIO,
                                LAYOUT, packageName), parent, false), itemValues);

            case VIEW_HOLDER_SELECT_BOX:
            case VIEW_HOLDER_GENERIC:
                return new GenericViewHolder(activity, inflater.inflate(resources
                        .getIdentifier(PREFIX + Field.TYPE_TEXT_FIELD,
                                LAYOUT, packageName), parent, false), itemValues);

            case VIEW_HOLDER_TICK_BOX:
                return new TickBoxViewHolder(activity, inflater.inflate(resources
                                .getIdentifier(PREFIX + Field.TYPE_TICK_BOX, LAYOUT, packageName),
                        parent, false), itemValues);

            case VIEW_HOLDER_PHOTO_UPLOAD_BOX:
                return new PhotoUploadViewHolder(activity, inflater.inflate(resources
                                .getIdentifier(PREFIX + Field.TYPE_PHOTO_UPLOAD_LAYOUT, LAYOUT, packageName),
                        parent, false), itemValues, adapter);

            case VIEW_HOLDER_PHOTO_UPLOAD_BOX_MINI_FORM:
                return new PhotoUploadMiniFormViewHolder(activity, inflater.inflate(resources
                                .getIdentifier(Field.TYPE_PHOTO_VIEW_SLIDER, LAYOUT, packageName),
                        parent, false), itemValues);

            case VIEW_HOLDER_AUDIO_UPLOAD_BOX:
                return new AudioUploadViewHolder(activity, inflater.inflate(resources
                                .getIdentifier(PREFIX + Field.TYPE_AUDIO_UPLOAD_LAYOUT, LAYOUT, packageName),
                        parent, false), itemValues);

            case VIEW_HOLDER_SUBFORM:
                return new SubFormViewHolder(activity, inflater.inflate(resources
                                .getIdentifier(PREFIX + Field.TYPE_SUBFORM_FIELD, LAYOUT, packageName),
                        parent, false), itemValues);

            case VIEW_HOLDER_MINI_FORM_PROFILE:
                return new MiniFormProfileViewHolder(activity, inflater.inflate(resources
                                .getIdentifier(PREFIX + Field.TYPE_MINI_FORM_PROFILE, LAYOUT, packageName),
                        parent, false), itemValues);
            case VIEW_HOLDER_INCIDENT_MINI_FORM_PROFILE:
                return new IncidentMiniFormProfileViewHolder(activity, inflater.inflate(resources
                                .getIdentifier(PREFIX + Field.TYPE_MINI_FORM_PROFILE, LAYOUT, packageName),
                        parent, false), itemValues);
            case VIEW_HOLDER_DATE_RANGE:
                return new DateRangeViewHolder(activity, inflater.inflate(resources
                        .getIdentifier(PREFIX + Field.TYPE_DATE_RANGE, LAYOUT, packageName),
                        parent, false), itemValues);
            case VIEW_HOLDER_CUSTOM:
                return new CustomViewHolder(activity, inflater.inflate(resources
                                .getIdentifier(PREFIX + Field.TYPE_CUSTOM, LAYOUT, packageName),
                        parent, false), itemValues);
            default:
                return new DefaultViewHolder(activity, new View(activity));
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Field field = fields.get(position);
        holder.setFieldValueVerifyResult(itemValuesVerifyList);
        holder.setValue(field);
        holder.setIsRecyclable(false);
        holder.setCurrentPosition(position);

        if (holder instanceof SubFormViewHolder) {
            if (subformDropDownStatus.get(position) == null) {
                ((SubFormViewHolder)holder).setSubformVisible(EMPTY_LIST);
            } else {
                ((SubFormViewHolder)holder).setSubformVisible(subformDropDownStatus.get(position));
            }
        }
        if (activity.getCurrentFeature().isDetailMode()) {
            holder.setFieldClickable(true);
            holder.setFieldEditable(false);
        } else {
            holder.setOnClickListener(field);
            holder.setFieldClickable(false);
            holder.setFieldEditable(holder.isEditable(field));
        }
    }

    @Override
    public int getItemViewType(int position) {
        Field field = fields.get(position);
        if (field.isTextField() || field.isNumericField()) {
            return VIEW_HOLDER_TEXT;
        }
        if (field.isTextArea()){
            return VIEW_HOLDER_TEXT_AREA;
        }

        if (field.isSelectField() || field.isRadioButton()) {
            if (field.hasMoreThanTwoOptions()) {
                return VIEW_HOLDER_SELECT_BOX;
            }
            return field.isMultiSelect() ? VIEW_HOLDER_SELECT_BOX : VIEW_HOLDER_RADIO_SINGLE_LINE;
        }

        if (field.isTickBox()) {
            return VIEW_HOLDER_TICK_BOX;
        }
        if (field.isPhotoUploadBox()) {
            if (!activity.getCurrentFeature().isDetailMode()) {
                return VIEW_HOLDER_PHOTO_UPLOAD_BOX;
            }
            return isMiniForm ? VIEW_HOLDER_PHOTO_UPLOAD_BOX_MINI_FORM : VIEW_HOLDER_PHOTO_UPLOAD_BOX;
        }
        if (field.isAudioUploadBox()) {
            return VIEW_HOLDER_AUDIO_UPLOAD_BOX;
        }
        if (field.isSubform()) {
            return VIEW_HOLDER_SUBFORM;
        }
        if (field.isIncidentMiniFormProfile()) {
            return VIEW_HOLDER_INCIDENT_MINI_FORM_PROFILE;
        }
        if (field.isMiniFormProfile()) {
            return VIEW_HOLDER_MINI_FORM_PROFILE;
        }
        if (field.isCustom()) {
            return VIEW_HOLDER_CUSTOM;
        }
        if (field.isDateRange()) {
            return VIEW_HOLDER_DATE_RANGE;
        }
        return VIEW_HOLDER_GENERIC;
    }

    @Override
    public int getItemCount() {
        return fields.size();
    }

    @Override
    public void onViewDetachedFromWindow(BaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof SubFormViewHolder) {
            subformDropDownStatus.put(holder.getCurrentPosition(), ((SubFormViewHolder) holder).getSubformStatusList());
        }
    }

    @Override
    public void onViewAttachedToWindow(BaseViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder instanceof SubFormViewHolder) {
            subformDropDownStatus.put(holder.getCurrentPosition(), ((SubFormViewHolder) holder).getSubformStatusList());
        }
    }

    public List<Field> getFields() {
        return fields;
    }

    public ItemValuesMap getItemValues() {
        return itemValues;
    }

    public void setSubformDropDownStatus(HashMap<Integer, List<Boolean>> subformDropDownStatus) {
        this.subformDropDownStatus = subformDropDownStatus;
    }

    public HashMap<Integer, List<Boolean>> getSubformDropDownStatus() {
        return subformDropDownStatus;
    }
}
