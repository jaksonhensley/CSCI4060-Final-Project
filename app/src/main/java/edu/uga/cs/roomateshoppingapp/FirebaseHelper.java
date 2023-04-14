package edu.uga.cs.roomateshoppingapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseHelper {
    private DatabaseReference db;
    private FirebaseAuth mAuth;

    public FirebaseHelper() {
        // Initialize the database reference
        db = FirebaseDatabase.getInstance().getReference();
    }

    public void writeData(String userId, String data) {
        // Write data to the database for the given user ID
        db.child("users").child(userId).setValue(data);
    }

    public void readData(String userId, ValueEventListener listener) {
        // Read data from the database for the given user ID
        db.child("users").child(userId).addListenerForSingleValueEvent(listener);
    }
}
