package com.example.android.e_blood;

/**
 * Created by chakr_000 on 27-Mar-17.
 */

public class DonorListStructure {
    private String name;
    private String phone;
    private String address;
    private String bloodGroup;

    DonorListStructure(String name, String phone, String address, String bloodGroup) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.bloodGroup = bloodGroup;
    }

    String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    String getPhone() {

        return phone;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }
}
