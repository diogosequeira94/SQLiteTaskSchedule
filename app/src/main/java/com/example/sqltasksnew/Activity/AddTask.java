package com.example.sqltasksnew.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allyants.notifyme.NotifyMe;
import com.example.sqltasksnew.Helper.TaskDAO;
import com.example.sqltasksnew.Model.Task;
import com.example.sqltasksnew.R;
import com.google.android.material.textfield.TextInputEditText;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Calendar;

public class AddTask extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

    private TextInputEditText addTask;
    private Task actualTask;
    private CheckBox importantCheck;
    private Button buttonDelete;
    private EditText notes;
    private TextView deadline;
    private String currentDate;
    private boolean hasNotification;
    private ImageView clipboard;

    //Notifications
    private ImageView notification;
    private Calendar now = Calendar.getInstance();
    private TimePickerDialog timePickerDialog;
    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog;

    //Gallery
    private ImageView placeholder;
    private static final int PICK_IMAGE = 1;
    private Uri imageURI;
    private boolean isLoaded;
    private int originalHeight;
    private int originalWidth;
    private Dialog myDialog;
    private ImageView popUpImage;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        addTask = findViewById(R.id.task);
        importantCheck = findViewById(R.id.importantCheck);
        buttonDelete = findViewById(R.id.buttonDelete);
        notes = findViewById(R.id.myNotes);
        deadline = findViewById(R.id.deadline);
        notification = findViewById(R.id.notification);
        placeholder = findViewById(R.id.placeholder);
        clipboard = findViewById(R.id.clipboard);

        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.custompopup);
        popUpImage = myDialog.findViewById(R.id.popUpImage);



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

            if(actualTask.getNotification() == 1){
                notification.setImageResource(R.drawable.ic_alarm_off_black_24dp);
                hasNotification = true;
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


        //New Notification Dialogs

        datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                AddTask.this,
                now.get(Calendar.YEAR), // Initial year selection
                now.get(Calendar.MONTH), // Initial month selection
                now.get(Calendar.DAY_OF_MONTH) // Inital day selection
        );

        timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                AddTask.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),
                false


        );

        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(hasNotification){

                    AlertDialog.Builder dialog = new AlertDialog.Builder(AddTask.this);
                    dialog.setTitle("Remove alert");
                    dialog.setMessage("Are you sure you want to remove this reminder?");
                    dialog.setNegativeButton("No",null);
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            NotifyMe.cancel(getApplicationContext(),"test");
                            hasNotification = false;
                            notification.setImageResource(R.drawable.ic_add_alarm_black_24dp);
                            Toast.makeText(AddTask.this, "Removed successfully", Toast.LENGTH_SHORT).show();

                        }
                    });

                    dialog.create();
                    dialog.show();

                } else {
                    datePickerDialog.show(getFragmentManager(), "Datepicker");
                }


            }
        });

        //Setting image from gallery

        placeholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isLoaded){

                        //Image gets bigger on click CHANGE THIS


                        showPopUp();


                } else {

                    Intent gallery = new Intent();
                    gallery.setType("image/*");
                    gallery.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
                }



            }
        });

        placeholder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

            if(isLoaded){
                AlertDialog.Builder dialog = new AlertDialog.Builder(AddTask.this);
                dialog.setTitle("Remove image");
                dialog.setMessage("Are you sure you want to remove this image?");

                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NotifyMe.cancel(getApplicationContext(),"test");
                        placeholder.setImageResource(R.drawable.phicon);
                        isLoaded = false;
                        Toast.makeText(AddTask.this, "Removed successfully", Toast.LENGTH_SHORT).show();

                    }

                });
                dialog.setNegativeButton("No",null);
                dialog.create();
                dialog.show();
            }


                return true;
            }
        });

        clipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (actualTask != null){
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Note", allData());
                    clipboardManager.setPrimaryClip(clip);

                    Toast.makeText(getApplicationContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                } else {

                    Toast.makeText(AddTask.this, "You should save a task before copying it", Toast.LENGTH_SHORT).show();
                }


            }
        });


        popUpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });


    }



    //Method to show pop-up image

    public void showPopUp(){

        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        myDialog.show();


    }

    // This method will return me all the current data in my task

    public String allData(){

        String info = "";
        String notas;
        String deadline;

        if(actualTask != null){
            if(actualTask.getNotes() == null || actualTask.getNotes().equals("")) {
                notas = "No notes for this task";

            } else {
                notas = actualTask.getNotes();
            }

            if (actualTask.getDeadline() == null || actualTask.getDeadline().equals("Set a deadline")){
                deadline = "No deadline";

            } else{
                deadline = actualTask.getDeadline();
            }

            info = "Task name: \n" + actualTask.getTaskName() +  "\n\n" + "Deadline: \n"  + deadline+ "\n\n" + "Notes: \n" + notas;

        }


        return info;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            imageURI = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageURI);
                placeholder.setImageBitmap(bitmap);
                popUpImage.setImageBitmap(bitmap);

                originalHeight = placeholder.getLayoutParams().height;
                originalWidth = placeholder.getLayoutParams().width;

                System.out.println(originalHeight);
                System.out.println(originalWidth);

                isLoaded = true;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
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

                        if(hasNotification) {

                            task.setNotification(1);

                        }  else {

                            task.setNotification(0);
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

                        if(hasNotification){

                            task.setNotification(1);
                        } else {
                            task.setNotification(0);
                        }

                        task.setNotes(notes.getText().toString());
                        task.setTaskName(addTask.getText().toString());
                        task.setDeadline(currentDate);

                        if( taskDAO.save(task)){
                            Toast.makeText(getApplicationContext(), "Success adding task!", Toast.LENGTH_SHORT).show();
                            System.out.println("saved with: " + task.getNotification());
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


    //Notifications

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {

        now.set(Calendar.YEAR, year);
        now.set(Calendar.MONTH, monthOfYear);
        now.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        timePickerDialog.show(getFragmentManager(), "Timepickerdialog");
    }

    @Override
    public void onTimeSet(com.wdullaer.materialdatetimepicker.time.TimePickerDialog view, int hourOfDay, int minute, int second) {

        now.set(Calendar.HOUR_OF_DAY, hourOfDay);
        now.set(Calendar.MINUTE, minute);
        now.set(Calendar.SECOND, second);

        //Initialize notification

        NotifyMe notifyMe = new NotifyMe.Builder(getApplicationContext())
                .title("Task Reminder")
                .content(addTask.getText().toString())
                .color(255,192,76,1)
                .led_color(255,255,255,255)
                .time(now)
                .addAction(new Intent(), "Snooze", false)
                .key("test")
                .addAction(new Intent(), "Dismiss", true, false)
                .addAction(new Intent(), "Done")
                .large_icon(R.drawable.maincion)
                .build();

        Toast.makeText(getApplicationContext(), "Notification added", Toast.LENGTH_SHORT).show();
        notification.setImageResource(R.drawable.ic_alarm_off_black_24dp);
        hasNotification = true;



    }





}
