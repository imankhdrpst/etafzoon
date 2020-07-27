package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mindology.app.util.Enums;

import java.time.Period;

public class ClientUserDTO extends BaseModel {

    @SerializedName("age")
    @Expose
    private String age;

    @SerializedName("educationType")
    @Expose
    private Enums.EducationType educationType = Enums.EducationType.BACHELOR;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("firstName")
    @Expose
    private String firstName;

    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("lastName")
    @Expose
    private String lastName;

    @SerializedName("livingCity")
    @Expose
    private String livingCity;

    @SerializedName("marriageStatus")
    @Expose
    private Enums.MarriageStatus marriageStatus = Enums.MarriageStatus.SINGLE;

    @SerializedName("mobileNumber")
    @Expose
    private String mobileNumber;

    @SerializedName("profilePicture")
    @Expose
    private String profilePicture;

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Enums.EducationType getEducationType() {
        return educationType;
    }

    public void setEducationType(Enums.EducationType educationType) {
        this.educationType = educationType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLivingCity() {
        return livingCity;
    }

    public void setLivingCity(String livingCity) {
        this.livingCity = livingCity;
    }

    public Enums.MarriageStatus getMarriageStatus() {
        return marriageStatus;
    }

    public void setMarriageStatus(Enums.MarriageStatus marriageStatus) {
        this.marriageStatus = marriageStatus;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}
