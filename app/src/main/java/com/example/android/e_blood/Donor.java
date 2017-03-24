package com.example.android.e_blood;

/**
 * Created by chakr_000 on 24-Mar-17.
 */

public class Donor {
    private String name;
    private long phone;
    private int age;
    private String address;
    private String bloodGroup;
    private String occupation;

    public Donor() {

    }

    Donor(String name, long phone, int age, String address, String bloodGroup, String occupation) {
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.address = address;
        this.bloodGroup = bloodGroup;
        this.occupation = occupation;
    }

    public String getName() {
        return name;
    }

    long getPhone() {
        return phone;
    }

    int getAge() {
        return age;
    }

    String getAddress() {
        return address;
    }

    String getBloodGroup() {
        return bloodGroup;
    }

    String getOccupation() {
        return occupation;
    }

}
