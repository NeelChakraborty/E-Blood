package com.example.android.e_blood;

/**
 * Created by chakr_000 on 26-Mar-17.
 */

public class Hospital {
    String name;
    String location;

    Hospital(){

    }

    Hospital(String name, String location){
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
