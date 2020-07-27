package com.mindology.app.models;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Attachment extends BaseModel {
    @SerializedName("fileId")
    @Expose
    private String fileId;

    @SerializedName("fileUrl")
    @Expose
    private String fileUrl;

    @SerializedName("mimeType")
    @Expose
    private String mimeType;

    @SerializedName("createdAt")
    @Expose
    private String createdAt;

    @SerializedName("fileName")
    @Expose
    private String fileName;

    @SerializedName("localPath")
    private String localPath;

    @SerializedName("dateTaken")
    @Expose
    private String dateTaken;

    public Attachment(String fileId)
    {
        this.fileId = fileId;
    }

    public Attachment()
    {}


    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String uri) {
        this.localPath = uri;
    }

    public boolean isLocal()
    {
        return (TextUtils.isEmpty(fileId) || fileId == null) && (TextUtils.isEmpty(fileUrl) || fileUrl == null);
    }

//    public void setRelatedFile(File relatedFile) {
//        this.relatedFile = relatedFile;
//    }
//
//    public File getRelatedFile() {
//        return relatedFile;
//    }

    public boolean isEqualTo(Attachment t1) {
        return ((TextUtils.isEmpty(fileId) && TextUtils.isEmpty(t1.fileId)) || (fileId.equals(t1.fileId))) &&
                ((TextUtils.isEmpty(fileUrl) && TextUtils.isEmpty(fileUrl)) || (fileUrl.equals(t1.fileUrl))) &&
                ((TextUtils.isEmpty(mimeType) && TextUtils.isEmpty(t1.mimeType)) || (mimeType.equals(t1.mimeType))) &&
                ((TextUtils.isEmpty(fileName) && TextUtils.isEmpty(t1.fileName)) || (fileName.equals(t1.fileName))) &&
                ((localPath == null && t1.localPath == null) || (localPath.equals(t1.localPath)));
    }

    public void setDateTaken(String dateTaken) {
        this.dateTaken = dateTaken;
    }

    public String getDateTaken() {
        return dateTaken;
    }
}
