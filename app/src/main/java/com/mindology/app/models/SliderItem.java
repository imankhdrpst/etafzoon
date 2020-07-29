package com.mindology.app.models;

public class SliderItem {

    private String description;
    private String imageUrl;
    private String base64Content;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setBase64String(String content)
    {
        this.base64Content = content;
    }

    public String getBase64Content()
    {
        return base64Content;
    }
}
