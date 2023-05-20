package com.example.todo.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todo.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 2;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String USER_TABLE = "user";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER)";
    private static final String CREATE_USER_TABLE = "CREATE TABLE " + USER_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USERNAME + " TEXT, "
            + PASSWORD + " TEXT)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void closeDatabase() {
        db.close();
    }

    public void insertTask(ToDoModel task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        db.insert(TODO_TABLE, null, cv);
    }

    public List<ToDoModel> getAllTasks() {
        List<ToDoModel> taskList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = db.query(TODO_TABLE, null, null, null, null, null, null);
            if (cursor != null) {
                int idIndex = cursor.getColumnIndex(ID);
                int taskIndex = cursor.getColumnIndex(TASK);
                int statusIndex = cursor.getColumnIndex(STATUS);

                while (cursor.moveToNext()) {
                    ToDoModel task = new ToDoModel();
                    task.setId(cursor.getInt(idIndex));
                    task.setTask(cursor.getString(taskIndex));
                    task.setStatus(cursor.getInt(statusIndex));
                    taskList.add(task);
                }
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return taskList;
    }

    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id) {
        db.delete(TODO_TABLE, ID + "=?", new String[]{String.valueOf(id)});
    }

    public void insertUser(ToDoModel user) {
        ContentValues cv = new ContentValues();
        cv.put(USERNAME, user.getUsername());
        cv.put(PASSWORD, user.getPassword());
        db.insert(USER_TABLE, null, cv);
    }

    public ToDoModel getUser(String username, String password) {
        ToDoModel user = null;
        Cursor cursor = null;
        try {
            cursor = db.query(USER_TABLE, null, USERNAME + "=? AND " + PASSWORD + "=?", new String[]{username, password}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(ID);
                int usernameIndex = cursor.getColumnIndex(USERNAME);
                int passwordIndex = cursor.getColumnIndex(PASSWORD);

                user = new ToDoModel();
                user.setId(cursor.getInt(idIndex));
                user.setUsername(cursor.getString(usernameIndex));
                user.setPassword(cursor.getString(passwordIndex));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return user;
    }

    public void updateStatus(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "=?", new String[]{String.valueOf(id)});
    }

    public boolean checkUserCredentials(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {USERNAME};
        String selection = USERNAME + " = ? AND " + PASSWORD + " = ?";
        String[] selectionArgs = {username, password};
        Cursor cursor = db.query(USER_TABLE, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public boolean validateInputs(String username, String password) {
        openDatabase();
        boolean isValid = checkUserCredentials(username, password);
        closeDatabase();
        return isValid;
    }
}
