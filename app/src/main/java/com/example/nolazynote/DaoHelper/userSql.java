package com.example.nolazynote.DaoHelper;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class userSql extends SQLiteOpenHelper {
    public userSql(@Nullable Context context) {
        super(context,"user.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuffer sql = new StringBuffer();
        sql.append("create table user(");
        sql.append("id integer primary key autoincrement,");
        sql.append("userId varchar(20),");
        sql.append("password varchar(20))");
        sqLiteDatabase.execSQL(sql.toString());
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("alter table student add account varchar(20)");
    }
}
