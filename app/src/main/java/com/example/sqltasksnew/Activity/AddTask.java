package com.example.sqltasksnew.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sqltasksnew.Helper.TaskDAO;
import com.example.sqltasksnew.Model.Task;
import com.example.sqltasksnew.R;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.Calendar;

public class AddTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private TextInputEditText addTask;
    private Task actualTask;
    private CheckBox importantCheck;
    private Button buttonDelete;
    private EditText notes;
    private TextView deadline;
    private String currentDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        addTask = findViewById(R.id.task);
        importantCheck = findViewById(R.id.importantCheck);
        buttonDelete = findViewById(R.id.buttonDelete);
        notes = findViewById(R.id.myNotes);
        deadline = findViewById(R.id.deadline);

        // To prevent keyboard from popping

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //gets the deadline text

        deadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new com.example.sqltasksnew.Utils.DatePicker();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        deadline.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(AddTask.this);
                dialog.setTitle("Remove deadline");
                dialog.setMessage("Are you sure you want to remove this deadline?");
                dialog.setNegativeButton("No",null);
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deadline.setText("Set a deadline");
                    }
                });

                dialog.create();
                dialog.show();

                return true;
            }
        });

        //Recovers actual Task
        actualTask = (Task) getIntent().getSerializableExtra("chosenTask");


        //EditText configuration
        if(actualTask != null){
            addTask.setText(actualTask.getTaskName());

            //Notes Configuration
            notes.setText(actualTask.getNotes());

            //deadline Configuration

            if(actualTask.getDeadline() != null){

                deadline.setText(actualTask.getDeadline());
            }



            System.out.println(actualTask.getImage());
            if(actualTask.getImage() == 1){
                importantCheck.setChecked(true);
            }

        }


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(AddTask.this);
                dialog.setTitle("Delete task");
                dialog.setMessage("Are you sure you want to delete this task?");
                dialog.setNegativeButton("No",null);
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TaskDAO taskDAO = new TaskDAO(getApplicationContext());
                        taskDAO.delete(actualTask);
                        Toast.makeText(AddTask.this, "Delete successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

                dialog.create();
                dialog.show();


            }
        });

    }

    //Calender

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, i);
        c.set(Calendar.MONTH, i1);
        c.set(Calendar.DAY_OF_MONTH, i2);

        currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        deadline.setText(currentDate);

        System.out.println("New deadline: " + currentDate);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //inflate layout

        getMenuInflater().inflate(R.menu.menu_add_task, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.done:
                //DAO Data Access Object , usada para salvar e recuperar dados
                TaskDAO taskDAO = new TaskDAO(getApplicationContext());



                if(actualTask != null){

                    //Edition

                    if(!addTask.getText().toString().equals("")){

                        Task task = new Task();


                        task.setTaskName(addTask.getText().toString());
                        task.setNotes(notes.getText().toString());
                        task.setId(actualTask.getId());
                        task.setDeadline(deadline.getText().toString());
                        System.out.println("Gravado com: " + currentDate);

                        System.out.println(actualTask.getDeadline());

                        if(importantCheck.isChecked()) {

                            task.setImage(1);

                        }  else {

                        task.setImage(0);
                    }

                        //Update method

                        if(taskDAO.update(task)){

                            finish();

                        } else {

                        }


                    }


                } else {

                    //Save

                    if(!addTask.getText().toString().equals("")){

                        Task task = new Task();

                        if(importantCheck.isChecked()){

                            task.setImage(1);

                        } else {

                            task.setImage(0);
                        }

                        task.setNotes(notes.getText().toString());
                        task.setTaskName(addTask.getText().toString());
                        task.setDeadline(currentDate);

                        if( taskDAO.save(task)){
                            Toast.makeText(getApplicationContext(), "Success adding task!", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        }

                    } else{
                        Toast.makeText(getApplicationContext(), "Please insert a task!", Toast.LENGTH_SHORT).show();
                    }

                }




        }
        return super.onOptionsItemSelected(item);
    }
}
