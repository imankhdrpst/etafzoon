package com.mindology.app.models;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mindology.app.network.ListResponse;
import com.mindology.app.util.Enums;

public class Meter extends BaseModel implements Comparable<Meter> {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("type")
    @Expose
    private Enums.AssetType type = Enums.AssetType.METER;

    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("referencedAttachments")
    @Expose
    private ListResponse<Attachment> referencedAttachments;

    @SerializedName("index")
    @Expose
    private int index;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Enums.AssetType getType() {
        return type;
    }

    public void setType(Enums.AssetType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ListResponse<Attachment> getReferencedAttachments() {
        return referencedAttachments;
    }

    public void setReferencedAttachments(ListResponse<Attachment> attachments) {
        this.referencedAttachments = attachments;
    }

    public String isValid()
    {
        if (TextUtils.isEmpty(title))
        {
            return "A meter has no title";
        }
        if (TextUtils.isEmpty(serialNumber))
        {
            return "Meter " + title + " has no serial number";
        }
        if (TextUtils.isEmpty(value))
        {
            return "Meter " + title + " has no value";
        }
        return "";
    }

    public boolean isEqualTo(Meter t1) {
        return ((TextUtils.isEmpty(id) && TextUtils.isEmpty(t1.id)) || (id.equals(t1.id))) &&
                ((type.equals(t1.type))) &&
                ((TextUtils.isEmpty(value) && TextUtils.isEmpty(t1.value)) || (value.equals(t1.value))) &&
                ((TextUtils.isEmpty(serialNumber) && TextUtils.isEmpty(t1.serialNumber)) || (serialNumber.equals(t1.serialNumber))) &&
                ((TextUtils.isEmpty(title) && TextUtils.isEmpty(t1.title)) || (title.equals(t1.title))) &&
                ((referencedAttachments == null && t1.referencedAttachments == null) || (referencedAttachments.isEqualTo(t1.referencedAttachments)));
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public int compareTo(Meter meter) {
        return index - meter.index;
    }
}
