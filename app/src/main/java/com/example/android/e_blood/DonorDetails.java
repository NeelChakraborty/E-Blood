package com.example.android.e_blood;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DonorDetails extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private DatabaseReference donorDatabase;
    private String TAG = "DonorDetails";
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private TextView nameTextView, ageTextView, contactTextView, bloodGroupTextView, addressTextView, lastDonatedView, donationCountTextView;
    private String userID;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Button pushLocation, pushDonation;
    private Double lat;
    private Double lng;
    private String city;
    private String locality;
    private String full_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_details);

        //initializing GoogleApiClient
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        //Initializing views
        nameTextView = (TextView) findViewById(R.id.name_details_text_view);
        ageTextView = (TextView) findViewById(R.id.age_details_text_view);
        contactTextView = (TextView) findViewById(R.id.number_details_text_view);
        bloodGroupTextView = (TextView) findViewById(R.id.blood_group_details_text_view);
        addressTextView = (TextView) findViewById(R.id.address_details_text_view);
        lastDonatedView = (TextView) findViewById(R.id.update_donation_text);
        pushLocation = (Button) findViewById(R.id.push_location_button);
        pushDonation = (Button) findViewById(R.id.update_donation_button);
        donationCountTextView = (TextView) findViewById(R.id.donation_count_text);

        //initializing Firebase Database Objects
        mAuth = FirebaseAuth.getInstance();
        donorDatabase = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();

        String fromActivity = getIntent().getStringExtra("fromActivity");

        if (fromActivity.equals("DonorLogin") || fromActivity.equals("DonorRegistration")){
            userID = user.getUid();
        }
        else {
            userID = fromActivity;
            pushDonation.setVisibility(View.INVISIBLE);
            pushLocation.setVisibility(View.INVISIBLE);
        }


        //Authentication Listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };

        //UPDATE DONATION

        pushDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String currentDate = DateFormat.getDateTimeInstance().format(new Date());
                donorDatabase.child("Donors").child(user.getUid()).child("lastDonated").setValue(currentDate);

                donorDatabase.child("Donors").child(user.getUid()).child("donationCount").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long nOD = (long) dataSnapshot.getValue();
                        nOD = nOD + 1;
                        dataSnapshot.getRef().setValue(nOD);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //donorDatabase.child("Donors").child(user.getUid()).child("donationCount").setValue("" + donationNumber);
                Toast.makeText(DonorDetails.this, "Updated Successfully", Toast.LENGTH_SHORT).show();

            }
        });

        // Read from the database
        donorDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                showData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        pushLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donorDatabase.child("Donors").child(userID).child("latitude").setValue(lat);
                donorDatabase.child("Donors").child(userID).child("longitude").setValue(lng);
                donorDatabase.child("Donors").child(userID).child("city").setValue(city);
                donorDatabase.child("Donors").child(userID).child("locality").setValue(locality);
                donorDatabase.child("Donors").child(userID).child("fullAddress").setValue(full_address);
                Toast.makeText(DonorDetails.this, "Location Updated", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            Log.d(TAG, "UserID inside ShowData: "+userID);
            Log.d(TAG, "User Name inside ShowData: "+ds.child(userID).child("name").getValue());
            Log.d(TAG, "DS inside ShowData: "+ds.child(userID));

            nameTextView.setText(String.valueOf(ds.child(userID).child("name").getValue()));
            contactTextView.setText(String.valueOf(ds.child(userID).child("phone").getValue()));
            ageTextView.setText(String.valueOf(ds.child(userID).child("age").getValue()));
            bloodGroupTextView.setText(String.valueOf(ds.child(userID).child("bloodGroup").getValue()));
            addressTextView.setText(String.valueOf(ds.child(userID).child("fullAddress").getValue()));
            donationCountTextView.setText(String.valueOf(ds.child(userID).child("donationCount").getValue()));
            if(ds.child(userID).hasChild("lastDonated")) {
                lastDonatedView.setText(String.valueOf(ds.child(userID).child("lastDonated").getValue()));
            }
            else
                lastDonatedView.setText("Not Donated");
            break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        //connecting API
        googleApiClient.connect();

    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        //disconnecting API
        googleApiClient.disconnect();
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = Double.valueOf(Double.toString(location.getLatitude()));
        lng = Double.valueOf(Double.toString(location.getLongitude()));

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            city = addresses.get(0).getLocality();
            locality = addresses.get(0).getSubLocality();
            full_address = addresses.get(0).getAddressLine(0);
            Log.d(TAG, "Location Rahul "+location.getLatitude());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(100000);

        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection has failed");
    }
}
