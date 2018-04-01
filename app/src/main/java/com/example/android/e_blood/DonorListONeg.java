package com.example.android.e_blood;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class DonorListONeg extends Fragment {


    public DonorListONeg() {
        // Required empty public constructor
    }

    DatabaseReference donorDatabase = FirebaseDatabase.getInstance().getReference().child("Donors");
    DatabaseReference hospitalDatabase = FirebaseDatabase.getInstance().getReference().child("Hospitals");
    private FirebaseUser user;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private String userIDHospital;
    private String hospitalCity;
    String TAG = "DonorListONeg";
    DonorAdapter donorAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_donor_list_oneg, container, false);
        final ArrayList<DonorListStructure> donorsONeg = new ArrayList<>();

        //initializing Firebase Database Objects
        mAuth = FirebaseAuth.getInstance();
        hospitalDatabase = FirebaseDatabase.getInstance().getReference().child("Hospitals");
        user = mAuth.getCurrentUser();
        userIDHospital = user.getUid();

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

        //Fetch data from Hospital Database
        hospitalDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    hospitalCity = String.valueOf(ds.child("city").getValue());
                    Log.d(TAG, "DS inside getData: "+hospitalCity);

                    break;
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //Fetch data from Donor Database
        donorDatabase.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String name = (String) ds.child("name").getValue();
                    String phone = String.valueOf(ds.child("phone").getValue());
                    String bloodGroup = (String) ds.child("bloodGroup").getValue();
                    Double lat = (Double) ds.child("latitude").getValue();
                    Double lng = (Double) ds.child("longitude").getValue();
                    String donor_city = (String) ds.child("city").getValue();

                    if (Objects.equals(hospitalCity, donor_city)) {
                        if (Objects.equals(bloodGroup, "O-")) {
                            donorsONeg.add(new DonorListStructure(name, phone, bloodGroup, lat, lng));
                            Log.d(TAG, "Donors is: " + donorsONeg);
                        }
                    }
                }
                donorAdapter = new DonorAdapter(DonorListONeg.this, donorsONeg);
                ListView listView = (ListView) view.findViewById(R.id.list_oneg);
                listView.setAdapter(donorAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.page_oneg);

        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        ((DonorList) getActivity()).refreshNow();
                        Toast.makeText(getContext(), "Refresh Layout working", Toast.LENGTH_LONG).show();
                    }
                }
        );


        return view;
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
}
