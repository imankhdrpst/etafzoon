package com.mindology.app.models;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address extends BaseModel {

    @SerializedName("id")
    @Expose
    protected String id = "";

    @SerializedName("latitude")
    @Expose
    private double latitude = 0;

    @SerializedName("longitude")
    @Expose
    private double longitude = 0;

    @SerializedName("line1")
    @Expose
    private String line1 = "";

    @SerializedName("line2")
    @Expose
    private String line2 = "";

    @SerializedName("city")
    @Expose
    private String city = "";

    @SerializedName("county")
    @Expose
    private String county = "";

    @SerializedName("country")
    @Expose
    private String country = "";

    @SerializedName("postCode")
    @Expose
    private String postCode = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if (city == null || TextUtils.isEmpty(city) || city.toLowerCase().trim().equals("null")) {
            city = "";
        }
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        if (country == null || TextUtils.isEmpty(country) || country.toLowerCase().trim().equals("null")) {
            country = "";
        }
        this.country = country;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        if (postCode == null || TextUtils.isEmpty(postCode) || postCode.toLowerCase().trim().equals("null")) {
            postCode = "";
        }
        this.postCode = postCode;
    }

    public AddressCreate getCreateObject() {
        AddressCreate res = new AddressCreate();
        res.setLine1(getLine1());
        res.setLine2(getLine2());
        res.setCity(getCity());
        res.setCounty(getCounty());
        res.setCountry(getCountry());
        res.setPostCode(getPostCode());
        res.setLatitude(getLatitude());
        res.setLongitude(getLongitude());

        return res;
    }

    public Address copyOf()
    {
        Address address = new Address();
        address.setPostCode(getPostCode());
        address.setCountry(getCountry());
        address.setCounty(getCounty());
        address.setCity(getCity());
        address.setLine2(getLine2());
        address.setLine1(getLine1());
        address.setLatitude(getLatitude());
        address.setLongitude(getLongitude());
        address.setId(getId());

        return address;
    }

    @NonNull
    @Override
    public String toString() {

        try {
//            String res = line1 + " " + line2 + " " + city + " " + county + " " + country + " " + postCode;
            String res11 =
                    (line1 == null ? "" : line1) + " " +
                            (line2 == null ? "" : line2) + " " +
                            (city == null ? "" : city) + " " +
                            (county == null ? "" : county) + " " +
                            (country == null ? "" : country) + " " +
                            (postCode == null ? "" : postCode);
            return res11;

        } catch (Exception e) {
            e.printStackTrace();
            return super.toString();
        }
    }

    public boolean isEqualTo(Address address) {
        return ( TextUtils.isEmpty(line1) && TextUtils.isEmpty(address.line1)) || (line1.equals(address.line1))
                && ( (TextUtils.isEmpty(line2) && TextUtils.isEmpty(address.line2)) || line2.equals(address.line2) )
                && (( TextUtils.isEmpty(city) && TextUtils.isEmpty(address.city)) || city.equals(address.city) )
                && ((TextUtils.isEmpty(county) && TextUtils.isEmpty(address.county)) || county.equals(address.county) )
                && ((TextUtils.isEmpty(country) && TextUtils.isEmpty(address.country)) || country.equals(address.country) )
                && ((TextUtils.isEmpty(postCode) && TextUtils.isEmpty(address.postCode)) || postCode.equals(address.postCode) );
    }
}
