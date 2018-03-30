package com.example.android.e_blood;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.TabLayout;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DonorList extends AppCompatActivity {

    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager viewPager;
    DonorListAdapter viewPagerAdapter;
    private DatabaseReference hospitalDatabase;
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private String userID;
    private String TAG = "DonorList";
    private String hospitalCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);


        //Initializing views
        mAuth = FirebaseAuth.getInstance();
        hospitalDatabase = FirebaseDatabase.getInstance().getReference();
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
        hospitalDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                getData(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPagerAdapter = new DonorListAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragments(new DonorListOPos(), "O+");
        viewPagerAdapter.addFragments(new DonorListONeg(), "O-");
        viewPagerAdapter.addFragments(new DonorListAPos(), "A+");
        viewPagerAdapter.addFragments(new DonorListANeg(), "A-");
        viewPagerAdapter.addFragments(new DonorListBPos(), "B+");
        viewPagerAdapter.addFragments(new DonorListBNeg(), "B-");
        viewPagerAdapter.addFragments(new DonorListABPos(), "AB+");
        viewPagerAdapter.addFragments(new DonorListABNeg(), "AB-");


        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    public void refreshNow (){
        finish();
        overridePendingTransition( 0, 0);
        startActivity(getIntent());
        overridePendingTransition( 0, 0);
    }

    private void getData(DataSnapshot dataSnapshot) {
        for(DataSnapshot ds : dataSnapshot.getChildren()){
            Log.d(TAG, "UserID inside getData: "+userID);
            Log.d(TAG, "User Name inside getData: "+ds.child(userID).child("name").getValue());
            Log.d(TAG, "DS inside getData: "+ds.child(userID));

            hospitalCity = String.valueOf(ds.child(userID).child("city").getValue());
            Log.d(TAG, "User city inside getData: "+ds.child(userID).child("city").getValue());

            break;
        }
    }

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

    public String getHospitalCity() {
        return hospitalCity;
    }
}
