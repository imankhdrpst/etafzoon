package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenResponse extends BaseModel {

    @SerializedName("authenticated")
    @Expose
    private String authenticated;

    @SerializedName("verificationCode")
    @Expose
    private String verificationCode;

    @SerializedName("token")
    @Expose
    private String token;

    public TokenResponse()
    {}

    public String getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(String authenticated) {
        this.authenticated = authenticated;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

