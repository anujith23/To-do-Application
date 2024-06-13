package com.example.todoap;

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

public class MainActivity extends AppCompatActivity {

    private EditText usernameInput;
    private EditText passwordInput;
    private Button loginButton;
    private TextView signUpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the views
        usernameInput = findViewById(R.id.usernameinput);
        passwordInput = findViewById(R.id.inputpassword);
        loginButton = findViewById(R.id.button);
        signUpText = findViewById(R.id.signupbtn);

        // Set up the login button click listener
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        // Set up the sign-up text click listener
        signUpText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void handleLogin() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Perform basic validation
        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
        } else {
            // Check if stored credentials match user input
            if (isValidCredentials(username, password)) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show();

                // Navigate to HomeActivity
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
                finish(); // Optional: Close the current activity
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isValidCredentials(String username, String password) {
        // Retrieve stored username and password from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
        String storedUsername = sharedPreferences.getString("Username", "");
        String storedPassword = sharedPreferences.getString("Password", "");

        // Compare with user input
        return username.equals(storedUsername) && password.equals(storedPassword);
    }
}
