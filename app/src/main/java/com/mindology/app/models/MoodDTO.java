package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MoodDTO extends BaseModel {
    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("moodDate")
    @Expose
    private Date moodDate;

    @SerializedName("otherReason")
    @Expose
    private String otherReason;

    @SerializedName("reason0")
    @Expose
    private int reason0 = 0;

    @SerializedName("reason1")
    @Expose
    private int reason1 = 0;

    @SerializedName("reason2")
    @Expose
    private int reason2 = 0;

    @SerializedName("reason3")
    @Expose
    private int reason3 = 0;

    @SerializedName("reason4")
    @Expose
    private int reason4 = 0;

    @SerializedName("reason5")
    @Expose
    private int reason5 = 0;

    @SerializedName("userMobile")
    @Expose
    private String userMobile;
    private MoodType moodType;

    public MoodType getMoodType() {
        return moodType;
    }

    public void setMoodType(MoodType moodType) {
        this.moodType = moodType;
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

    public Date getMoodDate() {
        return moodDate;
    }

    public void setMoodDate(Date moodDate) {
        this.moodDate = moodDate;
    }

    public String getOtherReason() {
        return otherReason;
    }

    public void setOtherReason(String otherReason) {
        this.otherReason = otherReason;
    }

    public int getReason0() {
        return reason0;
    }

    public void setReason0(int reason0) {
        this.reason0 = reason0;
    }

    public int getReason1() {
        return reason1;
    }

    public void setReason1(int reason1) {
        this.reason1 = reason1;
    }

    public int getReason2() {
        return reason2;
    }

    public void setReason2(int reason2) {
        this.reason2 = reason2;
    }

    public int getReason3() {
        return reason3;
    }

    public void setReason3(int reason3) {
        this.reason3 = reason3;
    }

    public int getReason4() {
        return reason4;
    }

    public void setReason4(int reason4) {
        this.reason4 = reason4;
    }

    public int getReason5() {
        return reason5;
    }

    public void setReason5(int reason5) {
        this.reason5 = reason5;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }
}
