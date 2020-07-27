package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mindology.app.network.ListResponse;

public class InspectionAssets extends BaseModel {
    @SerializedName("areas")
    @Expose
    private ListResponse<Area> areas = new ListResponse<>();

    @SerializedName("meters")
    @Expose
    private ListResponse<Meter> meters = new ListResponse<>();

    @SerializedName("alarms")
    @Expose
    private ListResponse<Alarm> alarms = new ListResponse<>();

    public ListResponse<Area> getAreas() {
        return areas;
    }

    public void setAreas(ListResponse<Area> areas) {
        this.areas = areas;
    }

    public ListResponse<Meter> getMeters() {
        return meters;
    }

    public void setMeters(ListResponse<Meter> meters) {
        this.meters = meters;
    }

    public ListResponse<Alarm> getAlarms() {
        return alarms;
    }

    public void setAlarms(ListResponse<Alarm> alarms) {
        this.alarms = alarms;
    }

    public boolean isEqualTo(InspectionAssets assets) {
        return areas.isEqualTo(assets.areas) &&
                meters.isEqualTo(assets.meters) &&
                alarms.isEqualTo(assets.alarms);
    }

}
