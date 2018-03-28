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

    DonorDatabaseStructure(String name, long phone, int age, String bloodGroup) {
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.bloodGroup = bloodGroup;
        //this.lat = lat;
        //this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
}
