package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommentDTO extends BaseModel {

    @SerializedName("comment")
    @Expose
    private String comment;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("postId")
    @Expose
    private int postId;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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