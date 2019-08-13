package com.example.sqltasksnew.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqltasksnew.Model.Task;
import com.example.sqltasksnew.R;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> {

    private List<Task> taskList;


    public TaskAdapter(List<Task> list) {
        this.taskList = list;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //This Create View Holder Creates each item layout

        View taskList = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_list_adapter, parent, false);




        return new MyViewHolder(taskList);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //Binds Views

        Task task = taskList.get(position);
        holder.task.setText(task.getTaskName());

    }

    @Override
    public int getItemCount() {
        return this.taskList.size();
    }


    //Inner Class

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView task;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            task = itemView.findViewById(R.id.textTask);


        }
    }
}
