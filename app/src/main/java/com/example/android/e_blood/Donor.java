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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
}
