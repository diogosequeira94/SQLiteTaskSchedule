package com.example.sqltasksnew.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static int VERSION = 1;
    private static String NAME_DB = "DB_TASK";
    private static String TABLE_TASKS = "tasks";



    public DatabaseHelper(Context context) {
        super(context, NAME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        //Create database first time on users phone


        String sql = "CREATE TABLE IF NOT EXISTS " +
                TABLE_TASKS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " + " nome TEXT NOT NULL ); ";


        try{

            sqLiteDatabase.execSQL(sql);

            Log.i("INFO DB", "Success creating Table");

        }catch (Exception e){

            Log.i("INFO DB", "Error creating Table" + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static int getVERSION() {
        return VERSION;
    }

    public static String getNameDb() {
        return NAME_DB;
    }

    public static String getTableTasks() {
        return TABLE_TASKS;
    }
}
