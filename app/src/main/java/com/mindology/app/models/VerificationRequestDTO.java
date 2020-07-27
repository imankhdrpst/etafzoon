package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerificationRequestDTO {
    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;

    @SerializedName("verificationCode")
    @Expose
    private String activationCode;

    public VerificationRequestDTO(String mobileNumber, String activationCode) {
        this.mobileNumber = mobileNumber;
        this.activationCode = activationCode;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }
}
