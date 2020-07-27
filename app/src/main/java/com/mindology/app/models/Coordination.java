package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coordination {
    @SerializedName("system")
    @Expose
    private String system = "SNSpherical";

    @SerializedName("pitch")
    @Expose
    private float pitch;

    @SerializedName("yaw")
    @Expose
    private float yaw;

    public Coordination(float pitch, float yaw) {
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void convertToRadians() {
        pitch = (float)Math.toRadians(pitch);
        yaw = (float)Math.toRadians(yaw);
    }

    public void convertToDegrees()
    {
        pitch = (float)Math.toDegrees(pitch);
        yaw = (float)Math.toDegrees(yaw);
    }
}
