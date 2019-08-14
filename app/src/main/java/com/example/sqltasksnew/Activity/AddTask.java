package com.example.sqltasksnew.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.sqltasksnew.Helper.TaskDAO;
import com.example.sqltasksnew.Model.Task;
import com.example.sqltasksnew.R;
import com.google.android.material.textfield.TextInputEditText;

public class AddTask extends AppCompatActivity {

    private TextInputEditText addTask;
    private Task actualTask;
    private CheckBox importantCheck;
    private Button buttonDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        addTask = findViewById(R.id.task);
        importantCheck = findViewById(R.id.importantCheck);
        buttonDelete = findViewById(R.id.buttonDelete);

        //Recovers actual Task
        actualTask = (Task) getIntent().getSerializableExtra("chosenTask");

        //EditText configuration
        if(actualTask != null){
            addTask.setText(actualTask.getTaskName());
        }

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TaskDAO taskDAO = new TaskDAO(getApplicationContext());
                taskDAO.delete(actualTask);
                Toast.makeText(AddTask.this, "Delete successfully", Toast.LENGTH_SHORT).show();
                finish();


            }
        });

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
                        task.setId(actualTask.getId());

                        if(importantCheck.isChecked()) {

                            task.setImage(1);
                        }

                        //Update method

                        if(taskDAO.update(task)){

                            finish();
                            Toast.makeText(this, "Sucess updationg task", Toast.LENGTH_SHORT).show();

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


                        task.setTaskName(addTask.getText().toString());
                        if( taskDAO.save(task)){
                            Toast.makeText(getApplicationContext(), "Success adding task!", Toast.LENGTH_SHORT).show();
                            System.out.println("Adding the Task Activity : " + importantCheck.isChecked());
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
