package com.example.android.e_blood;

/**
 * Created by chakr_000 on 27-Mar-17.
 */

public class DonorListStructure {
    private String name;
    private String phone;
    private String bloodGroup;
    private Double lat;
    private Double lng;
    private String donorID;

    DonorListStructure(String name, String phone, String bloodGroup, Double lat, Double lng, String donorID) {
        this.name = name;
        this.phone = phone;
        this.bloodGroup = bloodGroup;
        this.lat = lat;
        this.lng = lng;
        this.donorID = donorID;
    }

    String getName() {
        return name;
    }

    String getPhone() { return phone; }

    String getBloodGroup() {
        return bloodGroup;
    }


    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }

    public String getDonorID() {
        return donorID;
    }
}
