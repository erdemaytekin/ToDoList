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

    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
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
        db.beginTransaction();
        try {
            cursor = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if (cursor != null) {
                int idIndex = cursor.getColumnIndex(ID);
                int taskIndex = cursor.getColumnIndex(TASK);
                int statusIndex = cursor.getColumnIndex(STATUS);

                if (idIndex >= 0 && taskIndex >= 0 && statusIndex >= 0) {
                    if (cursor.moveToFirst()) {
                        do {
                            ToDoModel task = new ToDoModel();
                            task.setId(cursor.getInt(idIndex));
                            task.setTask(cursor.getString(taskIndex));
                            task.setStatus(cursor.getInt(statusIndex));
                            taskList.add(task);
                        } while (cursor.moveToNext());
                    }
                } else {
                    // Sütun indeksleri geçerli değil
                    // Gerekli işlemleri yapabilir veya hata işleme yapabilirsiniz
                }
            }
        } finally {
            db.endTransaction();
            if (cursor != null) {
                cursor.close();
            }
        }
        return taskList;
    }

    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        Cursor cursor = db.query(TODO_TABLE, null, ID + "= ?", new String[]{String.valueOf(id)}, null, null, null);
        try {
            int taskIndex = cursor.getColumnIndex(TASK);
            if (taskIndex >= 0 && cursor.moveToFirst()) {
                db.update(TODO_TABLE, cv, ID + "= ?", new String[]{String.valueOf(id)});
            } else {
                // Sütun indeksi geçerli değil veya kayıt bulunamadı
                // Gerekli işlemleri yapabilir veya hata işleme yapabilirsiniz
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void updateStatus(int id, int status) {
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "=?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(int id) {
        db.delete(TODO_TABLE, ID + "=?", new String[]{String.valueOf(id)});
    }
}