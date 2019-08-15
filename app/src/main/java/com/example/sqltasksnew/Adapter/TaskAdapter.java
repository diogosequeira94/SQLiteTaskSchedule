package com.example.sqltasksnew.Adapter;

import android.gesture.Gesture;
import android.media.Image;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sqltasksnew.Model.Task;
import com.example.sqltasksnew.R;
import com.example.sqltasksnew.Util.ItemTouchHelperAdapter;

import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.MyViewHolder> implements ItemTouchHelperAdapter {

    private List<Task> taskList;
    private ItemTouchHelper touchHelper;


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
    public void onItemMove(int fromPosition, int toPosition) {
        Task fromTask = taskList.get(fromPosition);
        taskList.remove(fromTask);
        taskList.add(toPosition, fromTask);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemSwiped(int position) {
        taskList.remove(position);
        notifyItemRemoved(position);

    }

    public void setTouchHelper(ItemTouchHelper touchHelper){
        this.touchHelper = touchHelper;
    }


    //Inner Class

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener, GestureDetector.OnGestureListener {

        TextView task;
        ImageView imageView;
        ImageView notes;
        GestureDetector gestureDetector;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            task = itemView.findViewById(R.id.textTask);
            imageView = itemView.findViewById(R.id.priority);
            notes = itemView.findViewById(R.id.notes);

            gestureDetector = new GestureDetector(itemView.getContext(), this);


        }

        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {

            return false;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {
        touchHelper.startDrag(this);
        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            gestureDetector.onTouchEvent(motionEvent);
            return true;
        }
    }
}
