package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoqateAddress {
    @SerializedName("Id")
    @Expose
    private String id;

    @SerializedName("Type")
    @Expose
    private String type;

    @SerializedName("Text")
    @Expose
    private String text;

    @SerializedName("Highlight")
    @Expose
    private String highlight;

    @SerializedName("Description")
    @Expose
    private String description;

    private boolean isContainer = false;

    private String containerMeta = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHighlight() {
        return highlight;
    }

    public void setHighlight(String highlight) {
        this.highlight = highlight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isContainer() {
        return isContainer;
    }

    public void setContainer(boolean container) {
        isContainer = container;
    }

    public String getContainerMeta() {
        return containerMeta;
    }

    public void setContainerMeta(String containerMeta) {
        this.containerMeta = containerMeta;
    }
}
