package com.example.sqltasksnew.Helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.sqltasksnew.Model.Task;

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

            write.insert(DatabaseHelper.getNameDb(),null, cv);


        } catch (Exception e){
            e.printStackTrace();

            return false;
        }




        return true;
    }

    @Override
    public boolean update(Task task) {
        return false;
    }

    @Override
    public boolean delete(Task task) {
        return false;
    }

    @Override
    public List<Task> list() {
        return null;
    }
}
