package com.example.android.e_blood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View userButton = findViewById(R.id.donor);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Start Saving Lives...", Toast.LENGTH_SHORT).show();
                Intent userSignInIntent = new Intent(MainActivity.this, UserLoginActivity.class);
                startActivity(userSignInIntent);
            }
        });

        View hopitalButton = findViewById(R.id.hospital);
        hopitalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hospitalSignInIntent = new Intent(MainActivity.this, HopitalSignIn.class);
                startActivity(hospitalSignInIntent);
            }
        });
    }
}
