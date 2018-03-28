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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.location.LocationListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class DonorRegistration extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "Map";
    private EditText emailEditText, passwordEditText, nameEditText, phoneEditText, ageEditText;
    private Spinner bloodgroupSpinner;
    private DatabaseReference donorDatabase;
    private String userBloodGroup;
    private FirebaseUser user;
    private TextView temp;
    private Button registerButton;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Double lat;
    private Double lng;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        //initializing Firebase Authentication Object
        mAuth = FirebaseAuth.getInstance();

        //initializing GoogleApiClient
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();



        //initializing Firebase Database Object
        donorDatabase = FirebaseDatabase.getInstance().getReference();

        //initializing Views
        temp = (TextView) findViewById(R.id.fetching_location);
        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        phoneEditText = (EditText) findViewById(R.id.phone_edit_text);
        ageEditText = (EditText) findViewById(R.id.age_edit_text);
        bloodgroupSpinner = (Spinner) findViewById(R.id.blood_group_spinner);
        registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setVisibility(View.INVISIBLE);

        //ArrayAdapter for the Blood Group Spinner
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.blood_group, R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodgroupSpinner.setAdapter(adapter);
        bloodgroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userBloodGroup = bloodgroupSpinner.getItemAtPosition(position).toString();
                Toast.makeText(parent.getContext(), "Bloood group is: " + userBloodGroup, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

        //Register On-Click
        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if( emailEditText.getText().toString().isEmpty() ||
                    nameEditText.getText().toString().isEmpty() ||
                    phoneEditText.getText().toString().isEmpty() ||
                    ageEditText.getText().toString().isEmpty() ||
                    passwordEditText.getText().toString().isEmpty()){
                    Toast.makeText(DonorRegistration.this, "Please fill up all the fields",
                            Toast.LENGTH_LONG).show();
                }
                else {
                    registerUser();
                }
            }
        });
    }

    //RegisterUser
    private void registerUser() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(DonorRegistration.this, "You are not eligible. Sorry",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            writeNewDonor(task.getResult().getUser());
                            Intent userDetails = new Intent(DonorRegistration.this, DonorDetails.class);
                            startActivity(userDetails);
                        }
                    }
                });
    }


    //Add Details to Database
    private void writeNewDonor(FirebaseUser user) {
        String name = nameEditText.getText().toString();
        long phone = Long.parseLong(phoneEditText.getText().toString());
        int age = Integer.parseInt(ageEditText.getText().toString());
        String bloodGroup = userBloodGroup;

        DonorDatabaseStructure donorDatabaseStructure = new DonorDatabaseStructure(name, phone, age, bloodGroup, lat, lng);
        donorDatabase.child("Donors").child(user.getUid()).setValue(donorDatabaseStructure);
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
        Toast.makeText(this, "Location Updated", Toast.LENGTH_LONG).show();

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            String tmp=addresses.get(0).getSubLocality()+" , "+addresses.get(0).getLocality();
            temp.setText("Detected city is : "+tmp+".");
        } catch (IOException e) {
            e.printStackTrace();
        }

        registerButton.setVisibility(View.VISIBLE);
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
}