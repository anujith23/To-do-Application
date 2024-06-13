package com.example.todoap;

import android.os.Bundle;
import android.widget.TextView;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Set today's date in the TextView
        TextView todayDate = findViewById(R.id.todayDate);
        todayDate.setText(getCurrentDate());

        // Initialize BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                // Handle Home item click
                return true;
            } else if (itemId == R.id.settings) {
                // Handle Settings item click
                return true;
            } else if (itemId == R.id.add_new) {
                showDatePickerDialog();
                return true;
            }
            return true;
        });
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    selectedYear = year;
                    selectedMonth = month;
                    selectedDay = dayOfMonth;
                    showTimePickerDialog();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    selectedHour = hourOfDay;
                    selectedMinute = minute;
                    showTitleInputDialog();
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }

    private void showTitleInputDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_task, null);
        EditText editTextTaskName = dialogView.findViewById(R.id.editTextTaskName);
        Button buttonSave = dialogView.findViewById(R.id.buttonSave);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        buttonSave.setOnClickListener(view -> {
            String taskName = editTextTaskName.getText().toString().trim();
            if (taskName.isEmpty()) {
                Toast.makeText(HomeActivity.this, "Please enter a task name", Toast.LENGTH_SHORT).show();
            } else {
                // Save the task (For demonstration, just showing a toast)
                String taskDetails = "Task Name: " + taskName + "\nDate: " + selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay + "\nTime: " + selectedHour + ":" + selectedMinute;
                Toast.makeText(HomeActivity.this, "Task added:\n" + taskDetails, Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });

        buttonCancel.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }
}
