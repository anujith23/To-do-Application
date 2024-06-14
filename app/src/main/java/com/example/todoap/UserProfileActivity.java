package com.example.todoap;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class UserProfileActivity extends AppCompatActivity {

    private TextView nameTextView;
    private TextView emailTextView;
    private SharedPreferences sharedPreferences;

    private static final String USER_DETAILS_PREFS = "UserDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(USER_DETAILS_PREFS, MODE_PRIVATE);

        // Initialize views
        nameTextView = findViewById(R.id.name);
        emailTextView = findViewById(R.id.email);

        // Display current user details
        displayUserDetails();

        // Back Button
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        // Edit Info Button
        Button editInfoButton = findViewById(R.id.editInfoButton);
        editInfoButton.setOnClickListener(v -> showEditInfoDialog());

        // Change Password Button
        Button changePasswordButton = findViewById(R.id.changePasswordButton);
        changePasswordButton.setOnClickListener(v -> showChangePasswordDialog());
    }

    private void displayUserDetails() {
        String username = sharedPreferences.getString("Username", "");
        String email = sharedPreferences.getString("Email", "");

        nameTextView.setText(username);
        emailTextView.setText(email);
    }

    private void showEditInfoDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_edit_info, null);
        EditText editTextUsername = dialogView.findViewById(R.id.editTextUsername);
        EditText editTextEmail = dialogView.findViewById(R.id.editTextEmail);

        // Pre-fill EditText fields with current values
        String currentUsername = sharedPreferences.getString("Username", "");
        String currentEmail = sharedPreferences.getString("Email", "");

        editTextUsername.setText(currentUsername);
        editTextEmail.setText(currentEmail);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Edit User Info");

        builder.setPositiveButton("Save", (dialog, which) -> {
            // Save edited info to SharedPreferences
            String newUsername = editTextUsername.getText().toString().trim();
            String newEmail = editTextEmail.getText().toString().trim();

            if (!newUsername.isEmpty() && !newEmail.isEmpty()) {
                saveUserInfo(newUsername, newEmail);
                Toast.makeText(UserProfileActivity.this, "User info updated", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserProfileActivity.this, "Username and email cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void saveUserInfo(String username, String email) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Username", username);
        editor.putString("Email", email);
        editor.apply();

        // Update displayed user info
        nameTextView.setText(username);
        emailTextView.setText(email);
    }

    private void showChangePasswordDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_change_password, null);
        EditText editTextCurrentPassword = dialogView.findViewById(R.id.editTextCurrentPassword);
        EditText editTextNewPassword = dialogView.findViewById(R.id.editTextNewPassword);
        EditText editTextConfirmPassword = dialogView.findViewById(R.id.editTextConfirmPassword);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Change Password");

        builder.setPositiveButton("Change", (dialog, which) -> {
            // Validate and change password
            String currentPassword = editTextCurrentPassword.getText().toString().trim();
            String newPassword = editTextNewPassword.getText().toString().trim();
            String confirmPassword = editTextConfirmPassword.getText().toString().trim();

            if (validatePasswords(currentPassword, newPassword, confirmPassword)) {
                savePassword(newPassword);
                Toast.makeText(UserProfileActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(UserProfileActivity.this, "Invalid password details", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private boolean validatePasswords(String currentPassword, String newPassword, String confirmPassword) {
        // Retrieve the stored password from SharedPreferences
        String storedPassword = sharedPreferences.getString("Password", "");

        // Check if current password entered by the user matches the stored password
        return storedPassword.equals(currentPassword) && !newPassword.isEmpty() && newPassword.equals(confirmPassword);
    }

    private void savePassword(String newPassword) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Password", newPassword);
        editor.apply();
    }
}
