package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mindology.app.util.Enums;

public class InspectionCreate extends BaseModel {

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("type")
    @Expose
    private Enums.InspectionTypes inspectionType;

    @SerializedName("property")
    @Expose
    private Property linkedProperty;

    @SerializedName("linkInspection")
    @Expose
    private Inspection linkedInspection;

    @SerializedName("client")
    @Expose
    private Client linkedClient;

    @SerializedName("note")
    @Expose
    private String note;

    @SerializedName("inspectionStatus")
    @Expose
    private Enums.InspectionStatus inspectionStatus;

    @SerializedName("inspector")
    @Expose
    private ClientUserDTO inspector;

    public Enums.InspectionStatus getInspectionStatus() {
        return inspectionStatus;
    }

    public void setInspectionStatus(Enums.InspectionStatus inspectionStatus) {
        this.inspectionStatus = inspectionStatus;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Enums.InspectionTypes getInspectionType() {
        return inspectionType;
    }

    public void setInspectionType(Enums.InspectionTypes inspectionType) {
        this.inspectionType = inspectionType;
    }

    public Property getLinkedProperty() {
        return linkedProperty;
    }

    public void setLinkedProperty(Property linkedProperty) {
        this.linkedProperty = linkedProperty;
    }

    public Inspection getLinkedInspection() {
        return linkedInspection;
    }

    public void setLinkedInspection(Inspection linkedInspection) {
        this.linkedInspection = linkedInspection;
    }

    public Client getLinkedClient() {
        return linkedClient;
    }

    public void setLinkedClient(Client linkedClient) {
        this.linkedClient = linkedClient;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public ClientUserDTO getInspector() {
        return inspector;
    }

    public void setInspector(ClientUserDTO inspector) {
        this.inspector = inspector;
    }
}
