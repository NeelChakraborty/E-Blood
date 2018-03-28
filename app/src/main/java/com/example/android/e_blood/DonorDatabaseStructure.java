package com.example.android.e_blood;

/**
 * Created by chakr_000 on 24-Mar-17.
 */

public class DonorDatabaseStructure {
    private String Name;
    private long Phone;
    private int Age;
    private String BloodGroup;
    private Double Latitude;
    private Double Longitude;

    public DonorDatabaseStructure() {

    }

    DonorDatabaseStructure(String Name, long Phone, int Age, String BloodGroup, Double Latitude, Double Longitude) {
        this.Name = Name;
        this.Phone = Phone;
        this.Age = Age;
        this.BloodGroup = BloodGroup;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
    }

    public String getName() {
        return Name;
    }

    public long getPhone() {
        return Phone;
    }

    public int getAge() {
        return Age;
    }

    public String getBloodGroup() {
        return BloodGroup;
    }

    public  Double getLatitude(){ return Latitude;}
    
    public  Double getLongitude(){ return Longitude;}
}
