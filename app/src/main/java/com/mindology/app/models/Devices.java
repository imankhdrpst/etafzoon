package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Devices extends BaseModel {

    @SerializedName("osVersion")
    @Expose
    private String osVersion;

    @SerializedName("os")
    @Expose
    private String os;

    @SerializedName("info")
    @Expose
    private String info;

    @SerializedName("appVersion")
    @Expose
    private String appVersion;

    @SerializedName("token")
    @Expose
    private String token;

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
