package com.example.sqltasksnew.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;

import com.example.sqltasksnew.Adapter.TaskAdapter;
import com.example.sqltasksnew.Helper.DatabaseHelper;
import com.example.sqltasksnew.Helper.RecyclerItemClickListener;
import com.example.sqltasksnew.Model.Task;
import com.example.sqltasksnew.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //RecyclerViewConfig
        recyclerView = findViewById(R.id.recyclerView);

        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());

        ContentValues cv = new ContentValues();
        cv.put("nome" , "Teste");

        databaseHelper.getWritableDatabase().insert("tasks", null, cv );

        //Add OnClick

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Edit

            }

            @Override
            public void onLongItemClick(View view, int position) {

                //Delete

            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        }
        ));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddTask.class);
                startActivity(intent);
            }
        });

        loadTaskList();


    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    public void loadTaskList(){

        //Tasks
        Task task1 = new Task();
        task1.setTaskName("Go to Market");
        taskList.add(task1);

        Task task2 = new Task();
        task2.setTaskName("Go to Barber");
        taskList.add(task2);

        Task task3 = new Task();
        task3.setTaskName("Go to Mall");
        taskList.add(task3);





        //Adapter
        taskAdapter = new TaskAdapter(taskList);

        //Configuration of RecyclerView

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Adds a Line
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));

        recyclerView.setAdapter(taskAdapter);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
