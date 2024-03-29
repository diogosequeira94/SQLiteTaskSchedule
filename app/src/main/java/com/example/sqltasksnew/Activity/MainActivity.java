package com.example.sqltasksnew.Activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.example.sqltasksnew.Adapter.TaskAdapter;
import com.example.sqltasksnew.Helper.DatabaseHelper;
import com.example.sqltasksnew.Helper.RecyclerItemClickListener;
import com.example.sqltasksnew.Helper.TaskDAO;
import com.example.sqltasksnew.Model.Task;
import com.example.sqltasksnew.R;
import com.example.sqltasksnew.Utils.MyCalendar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TaskAdapter taskAdapter;
    private List<Task> taskList = new ArrayList<>();
    private Task selectedTask;
    private TextView taskNumber;
    private boolean isDecorated;
    private TaskDAO taskDAO;

    //getting the list to display the number of tasks
    private List<Task> taskArrayList = new ArrayList<>();
    private MyCalendar myCalendar;
    private int numberOfTasks;
    private String currentData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        taskNumber = findViewById(R.id.taskNumber);
        taskDAO = new TaskDAO(getApplicationContext());
        taskArrayList = taskDAO.list();

        // Get current data
        myCalendar = new MyCalendar();
        currentData = myCalendar.getCurrentDate();

        // Change text header



        //RecyclerViewConfig
        recyclerView = findViewById(R.id.recyclerView);

        //Add OnClick

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(
                getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //Edit

                selectedTask = taskList.get(position);

                //Sending task to activity

                Intent intent = new Intent(MainActivity.this, AddTask.class);
                intent.putExtra("chosenTask", selectedTask);


                startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

                selectedTask = taskList.get(position);

                //Delete
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);

                dialog.setTitle("Delete Confimation");
                dialog.setMessage("Would like to delete: " + selectedTask.getTaskName() + "?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        TaskDAO taskDAO = new TaskDAO(getApplicationContext());


                        if (taskDAO.delete(selectedTask)){

                            loadTaskList();
                            Toast.makeText(MainActivity.this, "Success deleting task", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(MainActivity.this, "Error deleting task", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                dialog.setNegativeButton("No", null);

                dialog.create();
                dialog.show();
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
    //    updateTaskNumber();

        //New bottom nav

      /* BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNav);
       bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
               switch (menuItem.getItemId()){

                   case R.id.ic_arrow:

                   case R.id.ic_add:
                       startActivity(new Intent(getApplicationContext(), AddTask.class));

                   case R.id.ic_settings:

               }


               return false;
           }
       });
*/

    }

    @Override
    protected void onStart() {

        loadTaskList();
     //   updateTaskNumber();
        super.onStart();
    }

    public void loadTaskList(){

        //Tasks

        TaskDAO taskDAO = new TaskDAO(getApplicationContext());
        taskList = taskDAO.list();


        //Adapter
        taskAdapter = new TaskAdapter(taskList);

        //Configuration of RecyclerView

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //Adds a Line
        if(!isDecorated){
            recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
            isDecorated = true;
        }

        //List Reverse
        Collections.reverse(taskList);


        recyclerView.setAdapter(taskAdapter);

        //CallBack and attach to recyclerView

        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                taskAdapter.getFilter().filter(s);
                return false;
            }
        });






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

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {



            TaskDAO taskDAO = new TaskDAO(getApplicationContext());

            selectedTask = taskList.get(viewHolder.getAdapterPosition());

            taskDAO.delete(selectedTask);
            taskAdapter.notifyDataSetChanged();
            loadTaskList();


        }
    };

    //Method to updateNumberofTasks;

  /*  public void updateTaskNumber(){


        for (Task task : taskArrayList){

            String taskDate = task.getDeadline();

            System.out.println(task.getDeadline());
            System.out.println(currentData);

            if (!currentData.equals(taskDate))
            {
                numberOfTasks++;

            }
        }

        taskNumber.setText("You have: " + numberOfTasks + " tasks for today.");

    }

    */
}
