package com.example.android.e_blood;

import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.jar.Attributes;

public class DonorDetails extends AppCompatActivity {

    private DatabaseReference donorDatabase;
    private String TAG = "DonorDetails";
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private TextView nameTextView, ageTextView, contactTextView, bloodGroupTextView, addressTextView;
    private Button update;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_details);

        //Initializing views
        nameTextView = (TextView) findViewById(R.id.name_details_text_view);
        ageTextView = (TextView) findViewById(R.id.age_details_text_view);
        contactTextView = (TextView) findViewById(R.id.number_details_text_view);
        bloodGroupTextView = (TextView) findViewById(R.id.blood_group_details_text_view);
        addressTextView = (TextView) findViewById(R.id.address_details_text_view);

        //initializing Firebase Database Objects
        mAuth = FirebaseAuth.getInstance();
        donorDatabase = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();
        userID = user.getUid();

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
            break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
