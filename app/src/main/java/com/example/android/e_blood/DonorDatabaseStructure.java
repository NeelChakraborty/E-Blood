package com.example.android.e_blood;

/**
 * Created by chakr_000 on 24-Mar-17.
 */

public class DonorDatabaseStructure {
    private String name;
    private long phone;
    private int age;
    private String bloodGroup;
    private Double lat;
    private Double lng;

    public DonorDatabaseStructure() {

    }

    DonorDatabaseStructure(String name, long phone, int age, String bloodGroup, Double lat, Double lng) {
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.bloodGroup = bloodGroup;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public long getPhone() {
        return phone;
    }

    public int getAge() {
        return age;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public  Double getLat(){ return lat;}

    public  Double getLng(){ return lng;}
}
