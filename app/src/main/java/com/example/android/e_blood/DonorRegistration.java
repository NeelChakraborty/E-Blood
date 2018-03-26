package com.example.android.e_blood;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DonorRegistration extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "Registration";
    private EditText emailEditText, passwordEditText, nameEditText, phoneEditText, ageEditText;
    private Spinner bloodgroupSpinner;
    private DatabaseReference donorDatabase;
    private String userBloodGroup;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        //initializing Firebase Authentication Object
        mAuth = FirebaseAuth.getInstance();

        //initializing Firebase Database Object
        donorDatabase = FirebaseDatabase.getInstance().getReference();

        //initializing Views
        emailEditText = (EditText) findViewById(R.id.email_edit_text);
        passwordEditText = (EditText) findViewById(R.id.password_edit_text);
        nameEditText = (EditText) findViewById(R.id.name_edit_text);
        phoneEditText = (EditText) findViewById(R.id.phone_edit_text);
        ageEditText = (EditText) findViewById(R.id.age_edit_text);
        bloodgroupSpinner = (Spinner) findViewById(R.id.blood_group_spinner);
        View registerButton = (View) findViewById(R.id.register_button);

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
                registerUser();
            }
        });
    }

    //RegisterUser
    private void registerUser() {
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

        DonorDatabaseStructure donorDatabaseStructure = new DonorDatabaseStructure(name, phone, age, bloodGroup);
        donorDatabase.child("Donors").child(user.getUid()).child("Name").setValue(donorDatabaseStructure.getName());
        donorDatabase.child("Donors").child(user.getUid()).child("Phone").setValue(donorDatabaseStructure.getPhone());
        donorDatabase.child("Donors").child(user.getUid()).child("Age").setValue(donorDatabaseStructure.getAge());
        donorDatabase.child("Donors").child(user.getUid()).child("BloodGroup").setValue(donorDatabaseStructure.getBloodGroup());

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