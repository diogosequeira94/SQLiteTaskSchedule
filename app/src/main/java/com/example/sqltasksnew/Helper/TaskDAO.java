package com.example.sqltasksnew.Helper;

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
            write.insert()
        } catch (Exception e){

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
