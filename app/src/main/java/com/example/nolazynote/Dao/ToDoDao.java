package com.example.nolazynote.Dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.nolazynote.entity.ToDos;
import com.example.nolazynote.DaoHelper.MyDatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class ToDoDao {
    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public ToDoDao(Context context) {
        dbHelper = new MyDatabaseHelper(context.getApplicationContext(), "Data.db");
    }

    public void open() throws SQLException {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long create(ToDos todos) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("todotitle", todos.getTitle());
        values.put("tododsc", todos.getDesc());
        values.put("tododate", todos.getDate());
        values.put("todotime", todos.getTime());
        values.put("remindTime", todos.getRemindTime());
        values.put("remindTimeNoDay", todos.getRemindTimeNoDay());
        values.put("isAlerted", todos.getisAlerted());
        values.put("imgId", todos.getImgId());
        values.put("isRepeat", todos.getIsRepeat());
        values.put("isFinished", todos.getIsFinshed());
        values.put("userId", todos.getUserId());
        long id = db.insert("Todo", null, values);
        db.close();
        return id;
    }

    public void setFinished(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isFinished", 1);
        db.update("Todo", values, "_id = ?", new String[] { String.valueOf(id) });
        db.close();
        return;
    }

    public void delete(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Todo", "_id=?", new String[]{String.valueOf(id)});
        db.close();
        return;
    }

    public List<ToDos> getAllTask(String userId,String date) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<ToDos> todosList = new ArrayList<ToDos>();
        List<ToDos> resTodos = new ArrayList<ToDos>();
        Cursor cursor = db.query("Todo", null, "userId=? and tododate=?",new String[]{userId,date}, null, null, "remindTime desc");
        while(cursor.moveToNext()) {
            ToDos data = new ToDos();
            data.setId(cursor.getInt(0));
            data.setTitle(cursor.getString(1));
            data.setDesc(cursor.getString(2));
            data.setDate(cursor.getString(3));
            data.setTime(cursor.getString(4));
            data.setRemindTime(cursor.getLong(5));
            data.setRemindTimeNoDay(cursor.getLong(6));
            data.setisAlerted(cursor.getInt(7));
            data.setImgId(cursor.getInt(8));
            data.setIsRepeat(cursor.getInt(9));
            data.setIsFinshed(cursor.getInt(10));
            data.setUserId(cursor.getString(11));
            todosList.add(data);
        }
        if (todosList != null && todosList.size()>0){

            for (ToDos todos : todosList){
                if (isToday(todos.getRemindTime()) && todos.getIsFinshed() == 0 ) {
                    resTodos.add(todos);
                }
            }
        }
        cursor.close();
        db.close();
        return resTodos;
    }

    public List<ToDos> getAllFinishedTask(String userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<ToDos> todosList = new ArrayList<ToDos>();
        List<ToDos> resTodos = new ArrayList<ToDos>();
        Cursor cursor = db.query("Todo", null, "userId=?",new String[]{userId}, null, null, "remindTime desc");
        while(cursor.moveToNext()) {
            ToDos data = new ToDos();
            data.setId(cursor.getInt(0));
            data.setTitle(cursor.getString(1));
            data.setDesc(cursor.getString(2));
            data.setDate(cursor.getString(3));
            data.setTime(cursor.getString(4));
            data.setRemindTime(cursor.getLong(5));
            data.setRemindTimeNoDay(cursor.getLong(6));
            data.setisAlerted(cursor.getInt(7));
            data.setImgId(cursor.getInt(8));
            data.setIsRepeat(cursor.getInt(9));
            data.setIsFinshed(cursor.getInt(10));
            data.setUserId(cursor.getString(11));
            todosList.add(data);
        }
        if (todosList != null && todosList.size()>0){
            for (ToDos todos : todosList){
                if (isToday(todos.getRemindTime()) && todos.getIsFinshed() == 1){
                    resTodos.add(todos);
                }
            }
        }
        cursor.close();
        db.close();
        return resTodos;
    }

    public List<ToDos> getTodaysTask(String userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<ToDos> allTodos = new ArrayList<ToDos>();
        List<ToDos> resTodos = new ArrayList<ToDos>();
        Cursor cursor = db.query("Todo",
                null, "isAlerted = ?", new String[] { "0" }, null, null, "remindTime");
        while (cursor.moveToNext()) {
            Log.i("today", "1");
            ToDos data = new ToDos();
            data.setId(cursor.getInt(0));
            data.setTitle(cursor.getString(1));
            data.setDesc(cursor.getString(2));
            data.setDate(cursor.getString(3));
            data.setTime(cursor.getString(4));
            data.setRemindTime(cursor.getLong(5));
            data.setRemindTimeNoDay(cursor.getLong(6));
            data.setisAlerted(cursor.getInt(7));
            data.setImgId(cursor.getInt(8));
            data.setIsRepeat(cursor.getInt(9));
            data.setIsFinshed(cursor.getInt(10));
            data.setUserId(cursor.getString(11));
            allTodos.add(data);
        }
        if (allTodos != null && allTodos.size()>0){
            for (ToDos todos : allTodos){
                if (todos.getRemindTime() >= System.currentTimeMillis() && isToday(todos.getRemindTime()) && todos.getUserId() == userId){
                    resTodos.add(todos);
                }
            }
        }
        cursor.close();
        db.close();
        return resTodos;
    }

    private static boolean isToday(long date){
        long interval = date - System.currentTimeMillis();
        TimeZone timeZone = TimeZone.getDefault();
        return interval < 86400000 && interval > -86400000 && millis2Days(date, timeZone) == millis2Days(System.currentTimeMillis(), timeZone);
    }

    private static long millis2Days(long millis, TimeZone timeZone) {
        return (((long) timeZone.getOffset(millis)) + millis) / 86400000;
    }

    public ToDos getTask(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        ToDos data = new ToDos();
        Cursor cursor = db.rawQuery("SELECT * FROM Todo WHERE _id =" + id, null);
        if (cursor.moveToNext()) {
            data.setId(cursor.getInt(0));
            data.setTitle(cursor.getString(1));
            data.setDesc(cursor.getString(2));
            data.setDate(cursor.getString(3));
            data.setTime(cursor.getString(4));
            data.setRemindTime(cursor.getLong(5));
            data.setRemindTimeNoDay(cursor.getLong(6));
            data.setisAlerted(cursor.getInt(7));
            data.setImgId(cursor.getInt(8));
            data.setIsRepeat(cursor.getInt(9));
            data.setIsFinshed(cursor.getInt(10));
            data.setUserId(cursor.getString(11));
        }
        cursor.close();
        close();
        return data;
    }

    public void setisAlerted(int cur) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("isAlerted", 1);
        db.update("Todo", values, "_id = ?", new String[]{cur + ""});
        close();
    }
}
