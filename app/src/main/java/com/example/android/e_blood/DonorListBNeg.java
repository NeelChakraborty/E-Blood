package com.example.android.e_blood;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
public class DonorListBNeg extends Fragment {


    public DonorListBNeg() {
        // Required empty public constructor
    }

    DatabaseReference donorDatabase = FirebaseDatabase.getInstance().getReference().child("Donors");
    String TAG = "DonorListBNeg";
    DonorAdapter donorAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.activity_donor_list_bneg, container, false);
        final ArrayList<DonorListStructure> donorsBNeg = new ArrayList<>();

        donorDatabase.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String name = (String) ds.child("Name").getValue();
                    String phone = String.valueOf(ds.child("Phone").getValue());
                    String bloodGroup = (String) ds.child("BloodGroup").getValue();

                    if (Objects.equals(bloodGroup, "B-")){
                        donorsBNeg.add(new DonorListStructure(name, phone, bloodGroup));
                        Log.d(TAG, "Donors is: " + donorsBNeg);
                    }

                }
                donorAdapter = new DonorAdapter(DonorListBNeg.this, donorsBNeg);
                ListView listView = (ListView) view.findViewById(R.id.list_bneg);
                listView.setAdapter(donorAdapter);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.page_bneg);

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

}
