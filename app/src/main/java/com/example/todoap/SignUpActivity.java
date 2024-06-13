package com.example.todoap;  // Replace with your actual package name

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText emailInput;
    private EditText passwordInput;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);  // Ensure this matches your XML layout file name

        // Initialize the views
        usernameInput = findViewById(R.id.usernameInput);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        signUpButton = findViewById(R.id.signUpButton);
        TextView signInText = findViewById(R.id.signInText);

        // Set up the sign-up button click listener
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSignUp();
            }
        });

        // Set up the sign-in text click listener
        signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void handleSignUp() {
        String username = usernameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
        } else {
            // Save user details
            saveUserDetails(username, email, password);

            // Display success message
            Toast.makeText(this, "Sign Up successful!", Toast.LENGTH_SHORT).show();

            // Optionally, navigate to another activity if sign-up is successful
            // Intent intent = new Intent(SignUpActivity.this, AnotherActivity.class);
            // startActivity(intent);
            // finish();
        }
    }

    private void saveUserDetails(String username, String email, String password) {
        // Save user details in SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Username", username);
        editor.putString("Email", email);
        editor.putString("Password", password); // Note: In a real application, do not store passwords in plain text
        editor.apply();

        Toast.makeText(this, "User details saved successfully", Toast.LENGTH_SHORT).show();
    }
}
