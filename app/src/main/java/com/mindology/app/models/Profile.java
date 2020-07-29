package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mindology.app.util.Enums;

public class Profile extends BaseModel {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("firstName")
    @Expose
    private String firstName;

    @SerializedName("lastName")
    @Expose
    private String lastName;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;

    @SerializedName("marriageStatus")
    @Expose
    private Enums.MarriageStatus marriageStatus = Enums.MarriageStatus.SINGLE;

    @SerializedName("educationType")
    @Expose
    private Enums.EducationType educationType;

    @SerializedName("livingCity")
    @Expose
    private String livingCity;

    @SerializedName("age")
    @Expose
    private String age;

    @SerializedName("profilePicture")
    @Expose
    private String profilePicture;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Enums.MarriageStatus getMarriageStatus() {
        return marriageStatus;
    }

    public void setMarriageStatus(Enums.MarriageStatus marriageStatus) {
        this.marriageStatus = marriageStatus;
    }

    public Enums.EducationType getEducationType() {
        return educationType;
    }

    public void setEducationType(Enums.EducationType educationType) {
        this.educationType = educationType;
    }

    public String getLivingCity() {
        return livingCity;
    }

    public void setLivingCity(String livingCity) {
        this.livingCity = livingCity;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}

















