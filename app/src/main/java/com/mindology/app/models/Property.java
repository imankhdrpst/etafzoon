package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Property extends BaseModel {
    @SerializedName("id")
    @Expose
    protected String id = "";

    @SerializedName("address")
    @Expose
    private Address address = new Address();

    @SerializedName("furnishing")
    @Expose
    private String furnishing;

    @SerializedName("hasGarage")
    @Expose
    private boolean garageContained;

    @SerializedName("hasParking")
    @Expose
    private boolean parkingContained;

    @SerializedName("hasGarden")
    @Expose
    private boolean gardenContained;

    @SerializedName("notes")
    @Expose
    private String notes;

    @SerializedName("bedroomsCount")
    @Expose
    private int bedrooms;

    @SerializedName("bathroomsCount")
    @Expose
    private int bathrooms;

    @SerializedName("type")
    @Expose
    private String propertyType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getFurnishing() {
        return furnishing;
    }

    public void setFurnishing(String furnishing) {
        this.furnishing = furnishing;
    }

    public boolean isGarageContained() {
        return garageContained;
    }

    public void setGarageContained(boolean garageContained) {
        this.garageContained = garageContained;
    }

    public boolean isParkingContained() {
        return parkingContained;
    }

    public void setParkingContained(boolean parkingContained) {
        this.parkingContained = parkingContained;
    }

    public boolean isGardenContained() {
        return gardenContained;
    }

    public void setGardenContained(boolean gardenContained) {
        this.gardenContained = gardenContained;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public int getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(int bathrooms) {
        this.bathrooms = bathrooms;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public void onSelected() {
    }

    public PropertyCreate getCreateObject() {
        PropertyCreate res = new PropertyCreate();

        res.setAddress(getAddress().getCreateObject());
        res.setBathrooms(getBathrooms());
        res.setBedrooms(getBedrooms());
        res.setFurnishing(getFurnishing());
        res.setGarageContained(isGarageContained());
        res.setGardenContained(isGardenContained());
        res.setParkingContained(isParkingContained());
        res.setNotes(getNotes());
        res.setPropertyType(getPropertyType());


        return res;
    }

    public Property copyOf()
    {
        Property res = new Property();
        res.setPropertyType(getPropertyType());
        res.setId(getId());
        res.setNotes(getNotes());
        res.setGardenContained(isGardenContained());
        res.setGarageContained(isGarageContained());
        res.setParkingContained(isParkingContained());
        res.setBedrooms(getBedrooms());
        res.setBathrooms(getBathrooms());
        res.setFurnishing(getFurnishing());
        res.setAddress(getAddress() == null ? null : getAddress().copyOf());

        return res;
    }

    public boolean isEqualTo(Property value) {
        if (value == null ) return false;
        boolean res =
                (address != null && address.isEqualTo(value.getAddress()))
                && furnishing.equals(value.getFurnishing())
                && garageContained == value.isGarageContained()
                && gardenContained == value.isGardenContained()
                && parkingContained == value.isParkingContained()
                && bathrooms == value.getBathrooms()
                && bedrooms == value.getBedrooms();
        if (propertyType == null) {
            return res && (value.getPropertyType() == null || value.getPropertyType().toLowerCase().equals("none"));
        }
        if (value.getPropertyType() == null)
        {
            return res && (propertyType == null || propertyType.toLowerCase().equals("none"));
        }
        return res && (propertyType.toLowerCase().equals(value.getPropertyType().toLowerCase()));
    }
}
