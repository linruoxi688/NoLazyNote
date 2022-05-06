package com.example.nolazynote.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.nolazynote.DaoHelper.userSql;

public class userDao {
    private userSql helper;

    public userDao(Context context) {
        helper = new userSql(context);
    }

    public long add(String name, String num) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userId", name);
        values.put("password", num);
        long id = db.insert("user", null, values);
        db.close();
        return id;
    }

    public void select() {
        SQLiteDatabase db = helper.getReadableDatabase();
        //Cursor cursor = db.query("student", null, null, null, null, null, null);
        Cursor cursor = db.rawQuery("select * from user", null);

        String userId;
        String password;
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.move(1);
            userId = cursor.getString(1);
            password = cursor.getString(2);
            System.out.println("---------------------------------"+userId+" "+password);
        }
        System.out.println(cursor.getCount());
        cursor.close();
        db.close();
    }
}