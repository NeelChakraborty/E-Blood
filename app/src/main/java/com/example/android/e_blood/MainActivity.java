package com.example.android.e_blood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth=FirebaseAuth.getInstance();
  
        View userButton = findViewById(R.id.donor);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent userSignInIntent = new Intent(MainActivity.this, DonorLogin.class);
                startActivity(userSignInIntent);
            }
        });

        View hopitalButton = findViewById(R.id.hospital);
        hopitalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hospitalSignInIntent = new Intent(MainActivity.this, HospitalLogin.class);
                startActivity(hospitalSignInIntent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
    }
}
