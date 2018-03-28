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

    DonorListStructure(String name, String phone, String bloodGroup, Double lat, Double lng) {
        this.name = name;
        this.phone = phone;
        this.bloodGroup = bloodGroup;
        this.lat = lat;
        this.lng = lng;
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
}
