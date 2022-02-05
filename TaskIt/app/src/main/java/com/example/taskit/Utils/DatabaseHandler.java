package com.example.taskit.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.taskit.model.taskit_model;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "taskDatabase";
    private static final String TASK_TABLE = "todotask";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String CREATE_TASK_TABLE = "CREATE TABLE " + TASK_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                                            + TASK + " TEXT, " + STATUS + " INTEGER) ";

    private SQLiteDatabase db;
    private Cursor cursor;


    public DatabaseHandler(Context context){
        super(context, NAME, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Drop the old tables
        db.execSQL("DROP TABLE IF EXISTS " + TASK_TABLE);
        //Create new table
        onCreate(db);
    }

    public void openDatabase(){
        db = this.getWritableDatabase();
    }

    public void insertTask(taskit_model task){
        ContentValues cv = new ContentValues();
        cv.put(TASK,task.getTask());
        cv.put(STATUS, 0);
        db.insert(TASK_TABLE, null, cv);
    }

    public List<taskit_model> getAllTasks(){
        List<taskit_model> taskitModelList = new ArrayList<>();
        cursor = null;
        db.beginTransaction();
        try{
            cursor = db.query(TASK_TABLE, null, null, null, null, null, null, null);
            if (cursor != null){
                if (cursor.moveToFirst()){
                    do {
                        taskit_model task = new taskit_model();
                        task.setId(cursor.getColumnIndex(ID));
                        task.setTask(cursor.getString(cursor.getColumnIndex(TASK)));
                        task.setStatus(cursor.getInt(cursor.getColumnIndex(STATUS)));
                        taskitModelList.add(task);
                    }while (cursor.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cursor != null;
            cursor.close();
        }
        return taskitModelList;
    }

    public void  updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TASK_TABLE, cv,ID + "=?", new String[]{String.valueOf(id)});
    }

    public void updateTask(int id, String task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TASK_TABLE, cv, ID + "+?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TASK_TABLE, ID + "=?", new String[]{String.valueOf(id)});
    }
}
