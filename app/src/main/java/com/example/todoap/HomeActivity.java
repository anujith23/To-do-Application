package com.example.todoap;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity implements TaskAdapter.TaskItemClickListener {

    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList;

    private int selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize RecyclerView and TaskAdapter
        recyclerView = findViewById(R.id.recyclerViewTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        taskList = loadTasks();
        taskAdapter = new TaskAdapter(taskList, this);
        recyclerView.setAdapter(taskAdapter);

        // Set today's date in the TextView
        TextView todayDate = findViewById(R.id.todayDate);
        todayDate.setText(getCurrentDate());

        // Initialize BottomNavigationView
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.home) {
                // Handle Home item click
                showTaskList();
                return true;
            } else if (itemId == R.id.settings) {
                // Handle Settings item click
                showSettingsDialog();
                return true;
            } else if (itemId == R.id.add_new) {
                // Show add task dialog
                showDatePickerDialog();
                return true;
            }
            return true;
        });
    }

    public static class Task {
        private String date;
        private String time;
        private String title;

        public Task(String date, String time, String title) {
            this.date = date;
            this.time = time;
            this.title = title;
        }

        public String getDate() {
            return date;
        }

        public String getTime() {
            return time;
        }

        public String getTitle() {
            return title;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }


    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private void showTaskList() {
        recyclerView.setVisibility(View.VISIBLE);
        // Add any logic to hide other views if necessary
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
                // Save the task
                addTask(taskName);
                dialog.dismiss();
            }
        });

        buttonCancel.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private void addTask(String taskName) {
        String date = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
        String time = selectedHour + ":" + selectedMinute;
        Task newTask = new Task(date, time, taskName);
        taskList.add(newTask);
        taskAdapter.updateTasks(taskList);
        saveTasks(taskList);
    }

    public void editTask(int position) {
        Task taskToEdit = taskList.get(position);

        // Example: Show an edit dialog similar to add dialog
        showEditTaskDialog(taskToEdit, position);
    }

    private void showEditTaskDialog(Task taskToEdit, int position) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_task, null);
        EditText editTextTaskName = dialogView.findViewById(R.id.editTextTaskName);
        Button buttonSave = dialogView.findViewById(R.id.buttonSave);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancel);

        // Pre-fill the EditText with existing task name
        editTextTaskName.setText(taskToEdit.getTitle());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        buttonSave.setOnClickListener(view -> {
            String editedTaskName = editTextTaskName.getText().toString().trim();
            if (editedTaskName.isEmpty()) {
                Toast.makeText(HomeActivity.this, "Please enter a task name", Toast.LENGTH_SHORT).show();
            } else {
                // Update the task
                taskToEdit.setTitle(editedTaskName);
                taskAdapter.updateTasks(taskList);
                saveTasks(taskList);
                dialog.dismiss();
            }
        });

        buttonCancel.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    public void deleteTask(int position) {
        taskList.remove(position);
        taskAdapter.updateTasks(taskList);
        saveTasks(taskList);
    }

    private void saveTasks(List<Task> tasks) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tasks);
        editor.putString("task_list", json);
        editor.apply();
    }

    private List<Task> loadTasks() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("task_list", null);
        Type type = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> tasks = gson.fromJson(json, type);
        return tasks != null ? tasks : new ArrayList<>();
    }

    @Override
    public void onEditButtonClick(int position) {
        editTask(position);
    }

    @Override
    public void onDeleteButtonClick(int position) {
        deleteTask(position);
    }

    private void showSettingsDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_settings, null);
        Button buttonMyProfile = dialogView.findViewById(R.id.buttonMyProfile);
        Button buttonDeveloperProfile = dialogView.findViewById(R.id.buttonDeveloperProfile);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();

        buttonMyProfile.setOnClickListener(view -> {
            Toast.makeText(HomeActivity.this, "My Profile selected", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        buttonDeveloperProfile.setOnClickListener(view -> {
            Toast.makeText(HomeActivity.this, "Developer Profile selected", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        dialog.show();
    }
}
