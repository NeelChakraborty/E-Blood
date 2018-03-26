package com.example.android.e_blood;

import android.location.Address;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

import java.util.jar.Attributes;

public class DonorDetails extends AppCompatActivity {

    private DatabaseReference donorDatabase;
    private String TAG = "DonorDetails";
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private TextView nameTextView, ageTextView, contactTextView, bloodGroupTextView;
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
            Log.d(TAG, "User Name inside ShowData: "+ds.child(userID).child("Name").getValue());

            nameTextView.setText(String.valueOf(ds.child(userID).child("Name").getValue()));
            contactTextView.setText(String.valueOf(ds.child(userID).child("Phone").getValue()));
            ageTextView.setText(String.valueOf(ds.child(userID).child("Age").getValue()));
            bloodGroupTextView.setText(String.valueOf(ds.child(userID).child("BloodGroup").getValue()));
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
