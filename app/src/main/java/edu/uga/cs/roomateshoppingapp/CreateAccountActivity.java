package edu.uga.cs.roomateshoppingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        Button createAccountButton = findViewById(R.id.createAccountButton);
        EditText createAccountName = findViewById(R.id.createAccountName);
        EditText createAccountEmail = findViewById(R.id.createAccountEmail);
        EditText createAccountPassword = findViewById(R.id.createAccountPassword);
        EditText createAccountConfirmPassword = findViewById(R.id.createAccountConfirmPassword);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = createAccountName.getText().toString().trim();
                String email = createAccountEmail.getText().toString().trim();
                String password = createAccountPassword.getText().toString().trim();
                String confirmPassword = createAccountConfirmPassword.getText().toString().trim();

                if (password.equals(confirmPassword)) {
                    FirebaseHelper authHelper = new FirebaseHelper();
                    authHelper.createAccount(name, email, password, CreateAccountActivity.this);
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}