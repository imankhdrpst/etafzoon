package com.mindology.app.models;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mindology.app.network.ListResponse;
import com.mindology.app.util.Enums;

import java.util.ArrayList;
import java.util.List;

public class Alarm extends BaseModel implements Comparable<Alarm> {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("type")
    @Expose
    private Enums.AssetType type = Enums.AssetType.ALARM;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("statuses")
    @Expose
    private List<Enums.AlarmStatus> statuses = new ArrayList<>();

    @SerializedName("referencedAttachments")
    @Expose
    private ListResponse<Attachment> referencedAttachments;

    @SerializedName("location")
    @Expose
    private String location;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Enums.AlarmStatus> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Enums.AlarmStatus> statuses) {
        this.statuses = statuses;
    }

    public ListResponse<Attachment> getReferencedAttachments() {
        return referencedAttachments;
    }

    public void setReferencedAttachments(ListResponse<Attachment> attachments) {
        this.referencedAttachments = attachments;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String isValid() {
        if (TextUtils.isEmpty(title)) {
            return "An alarm has no title";
        }
        if (statuses == null) {
            return "Alarm " + title + " has no status defined";
        }
        return "";
    }

    public boolean isEqualTo(Alarm t1) {
        return ((TextUtils.isEmpty(id) && TextUtils.isEmpty(t1.id)) || (id.equals(t1.id))) &&
                ((TextUtils.isEmpty(title) && TextUtils.isEmpty(t1.title)) || (title.equals(t1.title))) &&
                ((type == null && t1.type == null ) || (type.equals(t1.type))) &&
                ((statuses == null && t1.statuses == null) || (statuses.equals(t1.statuses))) &&
                ((referencedAttachments == null && t1.referencedAttachments == null) || (referencedAttachments.isEqualTo(t1.referencedAttachments))) &&
                ((TextUtils.isEmpty(location) && TextUtils.isEmpty(t1.location)) || (location.equals(t1.location)))
                ;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int compareTo(Alarm alarm) {
        return index - alarm.index;
    }
}
