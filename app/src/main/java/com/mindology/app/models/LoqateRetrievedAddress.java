package com.mindology.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoqateRetrievedAddress {
    @SerializedName("Id")
    @Expose
    private String id;

    @SerializedName("DomesticId")
    @Expose
    private String domesticId;

    @SerializedName("Language")
    @Expose
    private String language;

    @SerializedName("LanguageAlternatives")
    @Expose
    private String languageAlternatives;

    @SerializedName("Department")
    @Expose
    private String department;

    @SerializedName("Company")
    @Expose
    private String company;

    @SerializedName("SubBuilding")
    @Expose
    private String subBuilding;

    @SerializedName("BuildingNumber")
    @Expose
    private String buildingNumber;

    @SerializedName("BuildingName")
    @Expose
    private String buildingName;

    @SerializedName("SecondaryStreet")
    @Expose
    private String secondaryStreet;

    @SerializedName("Street")
    @Expose
    private String street;

    @SerializedName("Block")
    @Expose
    private String block;

    @SerializedName("Neighbourhood")
    @Expose
    private String neighbourhood;

    @SerializedName("District")
    @Expose
    private String district;

    @SerializedName("City")
    @Expose
    private String city;

    @SerializedName("Line1")
    @Expose
    private String line1;

    @SerializedName("Line2")
    @Expose
    private String line2;

    @SerializedName("Line3")
    @Expose
    private String line3;

    @SerializedName("Line4")
    @Expose
    private String line4;

    @SerializedName("Line5")
    @Expose
    private String line5;

    @SerializedName("AdminAreaName")
    @Expose
    private String adminAreaName;

    @SerializedName("AdminAreaCode")
    @Expose
    private String adminAreaCode;

    @SerializedName("Province")
    @Expose
    private String province;

    @SerializedName("ProvinceName")
    @Expose
    private String provinceName;

    @SerializedName("ProvinceCode")
    @Expose
    private String provinceCode;

    @SerializedName("PostalCode")
    @Expose
    private String postalCode;

    @SerializedName("CountryName")
    @Expose
    private String countryName;

    @SerializedName("CountryIso2")
    @Expose
    private String countryIso2;

    @SerializedName("CountryIso3")
    @Expose
    private String countryIso3;

    @SerializedName("CountryIsoNumber")
    @Expose
    private int countryIsoNumber;

    @SerializedName("SortingNumber1")
    @Expose
    private String sortingNumber1;

    @SerializedName("SortingNumber2")
    @Expose
    private String sortingNumber2;

    @SerializedName("Barcode")
    @Expose
    private String barcode;

    @SerializedName("Label")
    @Expose
    private String label;

    @SerializedName("Type")
    @Expose
    private String type;

    @SerializedName("DataLevel")
    @Expose
    private String dataLevel;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDomesticId() {
        return domesticId;
    }

    public void setDomesticId(String domesticId) {
        this.domesticId = domesticId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getLanguageAlternatives() {
        return languageAlternatives;
    }

    public void setLanguageAlternatives(String languageAlternatives) {
        this.languageAlternatives = languageAlternatives;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSubBuilding() {
        return subBuilding;
    }

    public void setSubBuilding(String subBuilding) {
        this.subBuilding = subBuilding;
    }

    public String getBuildingNumber() {
        return buildingNumber;
    }

    public void setBuildingNumber(String buildingNumber) {
        this.buildingNumber = buildingNumber;
    }

    public String getBuildingName() {
        return buildingName;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public String getSecondaryStreet() {
        return secondaryStreet;
    }

    public void setSecondaryStreet(String secondaryStreet) {
        this.secondaryStreet = secondaryStreet;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBlock() {
        return block;
    }

    public void setBlock(String block) {
        this.block = block;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public void setNeighbourhood(String neighbourhood) {
        this.neighbourhood = neighbourhood;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLine3() {
        return line3;
    }

    public void setLine3(String line3) {
        this.line3 = line3;
    }

    public String getLine4() {
        return line4;
    }

    public void setLine4(String line4) {
        this.line4 = line4;
    }

    public String getLine5() {
        return line5;
    }

    public void setLine5(String line5) {
        this.line5 = line5;
    }

    public String getAdminAreaName() {
        return adminAreaName;
    }

    public void setAdminAreaName(String adminAreaName) {
        this.adminAreaName = adminAreaName;
    }

    public String getAdminAreaCode() {
        return adminAreaCode;
    }

    public void setAdminAreaCode(String adminAreaCode) {
        this.adminAreaCode = adminAreaCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryIso2() {
        return countryIso2;
    }

    public void setCountryIso2(String countryIso2) {
        this.countryIso2 = countryIso2;
    }

    public String getCountryIso3() {
        return countryIso3;
    }

    public void setCountryIso3(String countryIso3) {
        this.countryIso3 = countryIso3;
    }

    public int getCountryIsoNumber() {
        return countryIsoNumber;
    }

    public void setCountryIsoNumber(int countryIsoNumber) {
        this.countryIsoNumber = countryIsoNumber;
    }

    public String getSortingNumber1() {
        return sortingNumber1;
    }

    public void setSortingNumber1(String sortingNumber1) {
        this.sortingNumber1 = sortingNumber1;
    }

    public String getSortingNumber2() {
        return sortingNumber2;
    }

    public void setSortingNumber2(String sortingNumber2) {
        this.sortingNumber2 = sortingNumber2;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDataLevel() {
        return dataLevel;
    }

    public void setDataLevel(String dataLevel) {
        this.dataLevel = dataLevel;
    }
}
