package com.mindology.app.models;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mindology.app.network.ListResponse;
import com.mindology.app.util.Enums;

import java.util.ArrayList;
import java.util.List;

public class Inspection extends BaseModel {

    @SerializedName("id")
    @Expose
    protected String id = "";

    @SerializedName("type")
    @Expose
    private Enums.InspectionTypes inspectionType = Enums.InspectionTypes.NONE;

    @SerializedName("reference")
    @Expose
    private String reference;

    @SerializedName("date")
    @Expose
    private String date = "";

    @SerializedName("note")
    @Expose
    private String note;

    @SerializedName("state")
    @Expose
    private Enums.InspectionStatus state = Enums.InspectionStatus.ASSIGNED;

    @SerializedName("property")
    @Expose
    private Property property = new Property();

    @SerializedName("linkInspection")
    @Expose
    private Inspection linkedInspection;

    @SerializedName("client")
    @Expose
    private Client client;

    @SerializedName("inspector")
    @Expose
    private ClientUserDTO inspector;

    @SerializedName("assets")
    @Expose
    private InspectionAssets assets = new InspectionAssets();

    @Expose(serialize = false, deserialize = false)
    private boolean isSynced = true;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Enums.InspectionStatus getState() {
        return state;
    }

    public void setState(Enums.InspectionStatus state) {
        this.state = state;
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

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public Inspection getLinkedInspection() {
        return linkedInspection;
    }

    public void setLinkedInspection(Inspection linkedInspection) {
        this.linkedInspection = linkedInspection;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public InspectionAssets getAssets() {
        return assets;
    }

    public void setAssets(InspectionAssets assets) {
        this.assets = assets;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }

    public InspectionCreate getCreateObject() {
        InspectionCreate createObj = new InspectionCreate();

        createObj.setDate(getDate());
        createObj.setInspectionStatus(getState());
        createObj.setInspectionType(getInspectionType());
        createObj.setLinkedClient(getClient());
        createObj.setLinkedInspection(getLinkedInspection());
        createObj.setLinkedProperty(getProperty());
        createObj.setNote(getNote());
        createObj.setInspector(getInspector());

        return createObj;
    }

    public Inspection copyOf() {
        Inspection inspection = new Inspection();
        inspection.setInspectionType(getInspectionType());
        inspection.setNote(getNote());
        inspection.setState(getState());
        inspection.setSynced(isSynced());
        inspection.setClient(getClient());
        inspection.setLinkedInspection(getLinkedInspection() == null ? null : getLinkedInspection().copyOf());
        inspection.setProperty(getProperty() == null ? null : getProperty().copyOf());
        InspectionAssets newAssets = new InspectionAssets();
        List<Area> newAreas = new ArrayList<>();
        List<Meter> newMeters = new ArrayList<>();
        List<Alarm> newAlarms = new ArrayList<>();
        newAreas.addAll(getAssets().getAreas().getData());
        newMeters.addAll(getAssets().getMeters().getData());
        newAlarms.addAll(getAssets().getAlarms().getData());
        newAssets.setAreas(new ListResponse<Area>(newAreas));
        newAssets.setMeters(new ListResponse<Meter>(newMeters));
        newAssets.setAlarms(new ListResponse<Alarm>(newAlarms));
        inspection.setAssets(newAssets);
        inspection.setDate(getDate());
        inspection.setId(getId());
        inspection.setReference(getReference());

        return inspection;
    }

    public boolean isEqualTo(Inspection value) {
        if (value == null) return false;
        return ((TextUtils.isEmpty(id) && TextUtils.isEmpty(value.getId()) || (id.equals(value.getId())) )) &&
                ((inspectionType == null && value.getInspectionType() == null) || (inspectionType != null && inspectionType.equals(value.getInspectionType()))) &&
                (( TextUtils.isEmpty(note) && TextUtils.isEmpty(value.getNote()) || (!TextUtils.isEmpty(note) && note.equals(value.getNote())) )) &&
                ((state == null && value.getState() == null) || (state != null && state.equals(value.getState()))) &&
                ((client == null && value.getClient() == null) || (client != null && client.isEqualTo(value.getClient()))) &&
                ((linkedInspection == null && value.getLinkedInspection() == null) || (linkedInspection != null && linkedInspection.isEqualTo(value.getLinkedInspection()))) &&
                ((property == null && value.getProperty() == null) || (property != null && property.isEqualTo(value.getProperty()))) &&
//                ((assets == null && value.getAssets() == null) || (assets != null && assets.isEqualTo(value.getAssets()))) &&
                ((date == null && value.getDate() == null) || (date != null && date.equals(value.getDate())));
    }

    public static class InspectionStateJsonable
    {
        @SerializedName("state")
        @Expose
        private String state;

        public InspectionStateJsonable(String state)
        {
            this.state = state;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

}
