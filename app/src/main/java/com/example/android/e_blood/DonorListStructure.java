package com.example.android.e_blood;

/**
 * Created by chakr_000 on 27-Mar-17.
 */

public class DonorListStructure {
    private String name;
    private String phone;
    private String bloodGroup;

    DonorListStructure(String name, String phone, String bloodGroup) {
        this.name = name;
        this.phone = phone;
        this.bloodGroup = bloodGroup;
    }

    String getName() {
        return name;
    }

    String getPhone() {

        return phone;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }
}
