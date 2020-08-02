package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookmarkPostDTO extends BaseModel {
    @SerializedName("userMobile")
    @Expose
    private String userMobile;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("postId")
    @Expose
    private int postId;

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }
}
