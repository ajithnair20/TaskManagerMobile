package com.ajith.taskmanagermobile.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ajith.taskmanagermobile.Model.TaskModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // SQLite DB parameters
    private static final int VERSION = 1;
    private static final String NAME = "TasksDatabase";
    private static final String TASKS_TABLE = "Tasks";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";

    //Queries for CRUD
    private static final String CREATE_TASK_TABLE = "CREATE TABLE " + TASKS_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context){
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREATE_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop the older tables
        db.execSQL( "DROP TABLE IF EXISTS " + TASKS_TABLE);

        //Create tables again
        onCreate(db);
    }

    public void openDatabase(){
        db = this.getWritableDatabase();
    }

    public void insertTask(TaskModel task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        db.insert(TASKS_TABLE, null, cv);
    }

    public List<TaskModel> getAllTasks(){
        List<TaskModel> tasklist = new ArrayList<>();
        Cursor cur = null;

        db.beginTransaction();
        try{
            cur = db.query(TASKS_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        TaskModel task = new TaskModel();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        tasklist.add(task);
                    }while(cur.moveToNext());
                }
            }
        }catch (Exception ex){

        }finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return tasklist;
    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TASKS_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TASKS_TABLE, cv,  ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TASKS_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
}
