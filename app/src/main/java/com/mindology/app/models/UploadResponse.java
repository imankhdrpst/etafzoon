package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UploadResponse {
    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("hash")
    @Expose
    private String hash;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    @SerializedName("size")
    @Expose
    private double size;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }
}
