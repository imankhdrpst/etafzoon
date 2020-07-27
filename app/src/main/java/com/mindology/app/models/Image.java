package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Image {
    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("id")
    @Expose(serialize = false, deserialize = false)
    private String id;

    public Image()
    {

    }

    public Image(String url)
    {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
