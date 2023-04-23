package edu.uga.cs.roomateshoppingapp;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class FirebaseHelper {

    private FirebaseAuth mAuth;
    FirebaseHelper() {
        mAuth = FirebaseAuth.getInstance();
    } // FirebaseHelper Constructor

    public void login(String email, String password, Activity activity) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User logged in successfully, start homeActivity
                            Intent intent = new Intent(activity, HomeActivity.class);
                            activity.startActivity(intent);
                            activity.finish();
                        } else {
                            // Login failed, display error message and clear password field
                            Toast.makeText(activity, "Authentication failed. Please check your email and password and try again.", Toast.LENGTH_SHORT).show();
                            EditText passwordEditText = activity.findViewById(R.id.loginPassword);
                            passwordEditText.getText().clear();
                        }
                    }
                });
    } // login()

    public void createAccount(String name, String email, String password, Activity activity) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // User account created successfully, update user's display name
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();
                            user.updateProfile(profileUpdates);

                            // Redirect to home activity
                            Intent intent = new Intent(activity, HomeActivity.class);
                            activity.startActivity(intent);
                            activity.finish();
                        } else {
                            // Account creation failed, display error message
                            Toast.makeText(activity, "Account creation failed. Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


} // FirebaseHelper Class
