package com.example.android.e_blood;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Printer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HopitalSignIn extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG =  "HospitalSignIn";
    private EditText emailEditText, passwordEditText;
    private View signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hopital_sign_in);

        //To go to Register
        TextView registerTextView = (TextView)findViewById(R.id.register);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent hospitalRegisterIntent = new Intent(HopitalSignIn.this, HospitalRegistratiom.class);
                startActivity(hospitalRegisterIntent);
            }
        });

        //Retrieve email and password and button
        emailEditText = (EditText)findViewById(R.id.email_sign_in_hospital);
        passwordEditText = (EditText)findViewById(R.id.password_sign_in_hospital);
        signInButton = findViewById(R.id.sign_in);

        //Firebase Authentication Object Instantiation
        mAuth = FirebaseAuth.getInstance();
        //Authentication Listener
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
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

        //Sign In onClickListener
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInHospital();
            }
        });

    }

    private void signInHospital(){
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(HopitalSignIn.this, "Wrong email or password",
                                     Toast.LENGTH_SHORT).show();
                        }else {
                            Intent donorList =  new Intent(HopitalSignIn.this, DonorList.class);
                            startActivity(donorList);
                        }

                        // ...
                    }
                });
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
