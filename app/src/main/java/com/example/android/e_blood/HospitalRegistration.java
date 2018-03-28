package com.example.android.e_blood;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HospitalRegistration extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "HospitalRegistration";
    private EditText emailEditText, passwordEditText, nameEditText, locationEditText;
    private DatabaseReference hospitalDatabase;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_registratiom);

        //initializing Firebase Authentication Object
        mAuth = FirebaseAuth.getInstance();

        //initializing Firebase Database Object
        hospitalDatabase = FirebaseDatabase.getInstance().getReference();

        //initializing Views
        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        locationEditText = (EditText)findViewById(R.id.location_edit_text);
        View registerButton = findViewById(R.id.register_button);

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
                    locationEditText.getText().toString().isEmpty() ||
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
        String location = locationEditText.getText().toString();

        Hospital hospital = new Hospital(name, location);

        hospitalDatabase.child("Hospitals").child(user.getUid()).child("Name").setValue(hospital.getName());
        hospitalDatabase.child("Hospitals").child(user.getUid()).child("Location").setValue(hospital.getLocation());
    }
}
