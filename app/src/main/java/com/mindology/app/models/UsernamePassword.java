package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsernamePassword extends BaseModel {
    @SerializedName("username")
    @Expose
    private String username;

    @SerializedName("password")
    @Expose
    private String password;


    public UsernamePassword(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
