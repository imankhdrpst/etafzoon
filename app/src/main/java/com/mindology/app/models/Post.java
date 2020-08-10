package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Post extends BaseModel {
    @SerializedName("author")
    @Expose
    private String author;

    @SerializedName("authorProfilePicture")
    @Expose
    private String authorProfilePicture;

    @SerializedName("commentList")
    @Expose
    private List<CommentDTO> commentList;

    @SerializedName("createDate")
    @Expose
    private Date createDate;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("imageContent")
    @Expose
    private String imageContent;

    @SerializedName("likeNumber")
    @Expose
    private int likeNumber;

    @SerializedName("postGroupId")
    @Expose
    private int postGroupId;

    @SerializedName("shareNumber")
    @Expose
    private int shareNumber;

    @SerializedName("title")
    @Expose
    private String title;

    private boolean isBookmarked = false;
    private String helpful = "-1";

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorProfilePicture() {
        return authorProfilePicture;
    }

    public void setAuthorProfilePicture(String authorProfilePicture) {
        this.authorProfilePicture = authorProfilePicture;
    }

    public List<CommentDTO> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<CommentDTO> commentList) {
        this.commentList = commentList;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

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

    public int getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(int likeNumber) {
        this.likeNumber = likeNumber;
    }

    public int getPostGroupId() {
        return postGroupId;
    }

    public void setPostGroupId(int postGroupId) {
        this.postGroupId = postGroupId;
    }

    public int getShareNumber() {
        return shareNumber;
    }

    public void setShareNumber(int shareNumber) {
        this.shareNumber = shareNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }

    public void setHelpful(String helpful) {

        this.helpful = helpful;
    }

    public String getHelpful() {
        return helpful;
    }
}
