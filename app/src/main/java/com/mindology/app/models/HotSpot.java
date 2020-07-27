package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mindology.app.network.ListResponse;
import com.mindology.app.util.Enums;

public class HotSpot extends BaseModel {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("priority")
    @Expose
    private Enums.HotSpotPriority priority;

    @SerializedName("condition")
    @Expose
    private String condition;

    @SerializedName("index")
    @Expose
    private int index;

    @SerializedName("coordination")
    @Expose
    private Coordination coordination;

    @SerializedName("referencedAttachments")
    @Expose
    private ListResponse<Attachment> referencedAttachments = new ListResponse<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Enums.HotSpotPriority getPriority() {
        return priority;
    }

    public void setPriority(Enums.HotSpotPriority priority) {
        this.priority = priority;
    }

    public Coordination getCoordination() {
        return coordination;
    }

    public void setCoordination(Coordination coordination) {
        this.coordination = coordination;
    }

    public ListResponse<Attachment> getReferencedAttachments() {
        return referencedAttachments;
    }

    public void setReferencedAttachments(ListResponse<Attachment> attachments) {
        this.referencedAttachments = attachments;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
