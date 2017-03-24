package com.example.android.e_blood;

/**
 * Created by chakr_000 on 24-Mar-17.
 */

public class Donor {
    String name;
    long phone;
    int age;
    String address;
    String bloodGroup;
    String occupation;
    public Donor(String name, long phone, int age, String address, String bloodGroup, String occupation){
        this.name = name;
        this.phone = phone;
        this.age = age;
        this.address = address;
        this.bloodGroup = bloodGroup;
        this.occupation = occupation;
    }

}
