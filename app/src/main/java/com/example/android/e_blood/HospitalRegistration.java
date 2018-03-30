package com.example.android.e_blood;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class HospitalRegistration extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "HospitalRegistration";
    private EditText emailEditText, passwordEditText, nameEditText;
    private DatabaseReference hospitalDatabase;
    private FirebaseUser user;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private TextView temp;
    private Double lat;
    private Double lng;
    private String city;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_registratiom);

        //initializing Firebase Authentication Object
        mAuth = FirebaseAuth.getInstance();

        //initializing Firebase Database Object
        hospitalDatabase = FirebaseDatabase.getInstance().getReference();

        //initializing GoogleApiClient
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        //initializing Views
        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        temp = (TextView) findViewById(R.id.fetching_location_hospital);
        registerButton = findViewById(R.id.register_button);
        registerButton.setVisibility(View.INVISIBLE);

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

        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if( emailEditText.getText().toString().isEmpty() ||
                    nameEditText.getText().toString().isEmpty() ||
                    passwordEditText.getText().toString().isEmpty()){

                    Toast.makeText(HospitalRegistration.this, "Please fill up all the fields",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    registerHospital();
                }

            }
        });
    }

    //RegisterHospital
    private void registerHospital() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(HospitalRegistration.this, "You are not eligible. Sorry",
                                    Toast.LENGTH_LONG).show();
                        }else {
                            writeNewHospital(task.getResult().getUser());
                            Intent donorList =  new Intent(HospitalRegistration.this, DonorList.class);
                            startActivity(donorList);
                        }
                    }
                });
    }

    private void writeNewHospital(FirebaseUser user) {
        String name = nameEditText.getText().toString();

        Hospital hospital = new Hospital(name);

        hospitalDatabase.child("Hospitals").child(user.getUid()).setValue(hospital);
        hospitalDatabase.child("Hospitals").child(user.getUid()).child("city").setValue(city);
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
        Log.i(TAG, "GoogleApiClient connection has been suspend");
        Log.i(TAG, "GoogleApiClient connection has failed");
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = Double.valueOf(Double.toString(location.getLatitude()));
        lng = Double.valueOf(Double.toString(location.getLongitude()));
        Toast.makeText(this, "Location Updated", Toast.LENGTH_LONG).show();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            String tmp = addresses.get(0).getLocality();
            city = addresses.get(0).getLocality();
            temp.setText("Detected locality is : "+tmp+".");
            registerButton.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
