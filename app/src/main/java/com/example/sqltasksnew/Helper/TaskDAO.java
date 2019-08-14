package com.example.sqltasksnew.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.sqltasksnew.Model.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskDAO implements  TaskDAOImplementation {

    //We need to access DbHelper

    private SQLiteDatabase write;
    private SQLiteDatabase read;


    public TaskDAO(Context context) {
        DatabaseHelper db = new DatabaseHelper(context);
        write = db.getWritableDatabase();
        read = db.getReadableDatabase();
    }

    @Override
    public boolean save(Task task) {

        try{
            ContentValues cv = new ContentValues();
            cv.put("nome", task.getTaskName());

            write.insert(DatabaseHelper.TABLE_TASKS,null, cv);

            Log.i("INFO", "SAVED WITH SUCESS");


        } catch (Exception e){
            e.printStackTrace();

            Log.i("INFO", "SAVED WITHOUT SUCESS");

            return false;
        }




        return true;
    }

    @Override
    public boolean update(Task task) {

        ContentValues cv = new ContentValues();
        cv.put("nome", task.getTaskName());

        try{

            String[] args = {task.getId().toString()};

            write.update(DatabaseHelper.TABLE_TASKS, cv, "id=?", args  );


            Log.i("INFO", "UPDATED WITH SUCESS");


        } catch (Exception e){
            e.printStackTrace();

            Log.i("INFO", "UPDATED WITHOUT SUCESS");

            return false;
        }


        return true;
    }

    @Override
    public boolean delete(Task task) {

     //   read.delete()

        try {

            String[] args = {task.getId().toString()};

            write.delete(DatabaseHelper.TABLE_TASKS,"id=?", args );

            Log.i("INFO", "DELETED WITH SUCESS");


        } catch (Exception e){
            e.printStackTrace();

            Log.i("INFO", "DELETED WITHOUT SUCESS");

            return false;
        }



        return true;
    }

    @Override
    public List<Task> list() {

        List<Task> myTasks = new ArrayList<>();

        String sql = "SELECT * FROM " + DatabaseHelper.TABLE_TASKS + " ;";
        Cursor c = read.rawQuery(sql, null);

        while(c.moveToNext()){

            Task task = new Task();

            Long id = c.getLong(c.getColumnIndex("id"));
            String name = c.getString(c.getColumnIndex("nome"));

            task.setId(id);
            task.setTaskName(name);

            myTasks.add(task);
        }



        return myTasks;
    }
}
