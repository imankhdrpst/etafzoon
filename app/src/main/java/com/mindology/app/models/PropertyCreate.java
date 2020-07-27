package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PropertyCreate extends BaseModel {
    @SerializedName("address")
    @Expose
    private AddressCreate address;

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

    public AddressCreate getAddress() {
        return address;
    }

    public void setAddress(AddressCreate address) {
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
}
