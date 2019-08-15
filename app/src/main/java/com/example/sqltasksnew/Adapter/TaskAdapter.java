package com.example.sqltasksnew.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqltasksnew.Model.Task;
import com.example.sqltasksnew.R;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> implements Filterable {

    private List<Task> taskList;
    private List<Task> taskListSearch;

    //We should create a copy of the list to use for the Search Box, to manipulate it


    public TaskAdapter(List<Task> list) {
        this.taskList = list;
        taskListSearch = new ArrayList<>(list);

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

        if(task.getImage() == 1) {
            holder.imageView.setImageResource(R.drawable.ic_priority_high_black_24dp);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }

        if(!task.getNotes().isEmpty()){
            holder.notes.setImageResource(R.drawable.ic_chat_black_24dp);
        } else {
            holder.notes.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return this.taskList.size();
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }

    private Filter myFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {

        List<Task> filteredList = new ArrayList<>();

        if(charSequence == null || charSequence.length() == 0){
            filteredList.addAll(taskListSearch);

        } else {

            String filterPattern = charSequence.toString().toLowerCase().trim();

            for (Task task : taskListSearch){
                if(task.getTaskName().toLowerCase().contains(filterPattern)){
                    filteredList.add(task);
                }
            }
        }

        FilterResults results = new FilterResults();
        results.values = filteredList;

        return results;

        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

            taskList.clear();
            taskList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };

    //Inner Class

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView task;
        ImageView imageView;
        ImageView notes;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            task = itemView.findViewById(R.id.textTask);
            imageView = itemView.findViewById(R.id.priority);
            notes = itemView.findViewById(R.id.notes);


        }
    }



}
