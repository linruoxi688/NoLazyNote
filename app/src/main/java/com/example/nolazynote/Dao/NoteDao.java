package com.example.nolazynote.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nolazynote.DaoHelper.NoteDaoHelper;
import com.example.nolazynote.MainActivity;
import com.example.nolazynote.entity.Task;

import java.util.ArrayList;
import java.util.List;

public class NoteDao {
    private NoteDaoHelper helper;

    public NoteDao(Context context){
        helper = new NoteDaoHelper(context);
    }

    public long add(String date,String content){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", MainActivity.userId);
        values.put("content",content);
        values.put("date",date);
        long id = db.insert("note",null,values);
        db.close();
        return id;
    }

    public long change(String date,String content,String taskId){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", MainActivity.userId);
        values.put("content",content);
        values.put("date",date);
        long id = db.insert("note",null,values);
        db.close();
        return id;
    }

    public List<Task> getAll(String userId){
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("note",null,"userId=?",new String[]{userId},null,null,null);
        List<Task> list= new ArrayList<>();
        while (cursor.moveToNext()){
            String id  = cursor.getString(0);
            String content =  cursor.getString(1);
            String date = cursor.getString(2);
            int taskId = cursor.getInt(3);
            list.add(new Task(id,content,date,taskId));
        }
        cursor.close();
        db.close();
        return list;
    }

    public int delete(long id){
        SQLiteDatabase db = helper.getWritableDatabase();
        int count = db.delete("note","taskId=?",new String[]{String.valueOf(id)});
        db.close();
        return count;
    }
}
