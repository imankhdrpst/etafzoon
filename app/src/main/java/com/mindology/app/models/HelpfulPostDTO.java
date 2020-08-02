package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HelpfulPostDTO extends BaseModel {
    @SerializedName("clientUserId")
    @Expose
    private int clientUserId;

    @SerializedName("helpful")
    @Expose
    private boolean helpful;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("postId")
    @Expose
    private int postId;

    public int getClientUserId() {
        return clientUserId;
    }

    public void setClientUserId(int clientUserId) {
        this.clientUserId = clientUserId;
    }

    public boolean isHelpful() {
        return helpful;
    }

    public void setHelpful(boolean helpful) {
        this.helpful = helpful;
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
