package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MoodStatisticsDTO extends BaseModel {
    @SerializedName("endDate")
    @Expose
    private Date endDate;

    @SerializedName("moods")
    @Expose
    private List<MoodDTO> moods = new ArrayList<>();

    @SerializedName("startDate")
    @Expose
    private Date startDate;

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<MoodDTO> getMoods() {
        return moods;
    }

    public void setMoods(List<MoodDTO> moods) {
        this.moods = moods;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
}
