package com.example.todoap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<HomeActivity.Task> taskList;
    private TaskItemClickListener clickListener;

    public TaskAdapter(List<HomeActivity.Task> taskList, TaskItemClickListener clickListener) {
        this.taskList = taskList;
        this.clickListener = clickListener;
    }

    public interface TaskItemClickListener {
        void onEditButtonClick(int position);
        void onDeleteButtonClick(int position);
    }

    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewDateTime;
        ImageButton buttonEdit, buttonDelete;

        public TaskViewHolder(@NonNull View itemView, final TaskItemClickListener listener) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDateTime = itemView.findViewById(R.id.textViewDateTime);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);

            buttonEdit.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onEditButtonClick(position);
                    }
                }
            });

            buttonDelete.setOnClickListener(v -> {
                if (listener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        listener.onDeleteButtonClick(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        HomeActivity.Task currentTask = taskList.get(position);
        holder.textViewTitle.setText(currentTask.getTitle());
        holder.textViewDateTime.setText(currentTask.getDate() + " " + currentTask.getTime());
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    public void updateTasks(List<HomeActivity.Task> tasks) {
        this.taskList = tasks;
        notifyDataSetChanged();
    }
}
