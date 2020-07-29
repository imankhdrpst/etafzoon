package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mindology.app.util.Enums;

public class PostGroup extends BaseModel {
    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("imageContent")
    @Expose
    private String imageContent;

    @SerializedName("type")
    @Expose
    private Enums.PostGroupType type = Enums.PostGroupType.PICTURE;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageContent() {
        return imageContent;
    }

    public void setImageContent(String imageContent) {
        this.imageContent = imageContent;
    }

    public Enums.PostGroupType getType() {
        return type;
    }

    public void setType(Enums.PostGroupType type) {
        this.type = type;
    }
}
