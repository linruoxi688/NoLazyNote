package com.example.nolazynote.DaoHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDaoHelper extends SQLiteOpenHelper {
    public NoteDaoHelper(Context context){
        super(context,"noLazy.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        StringBuffer sql = new StringBuffer();
        sql.append("create table note(");
        sql.append("userId varchar(20),");
        sql.append("content varchar(20),");
        sql.append("date varchar(20),");
        sql.append("taskId integer primary key autoincrement)");
        sqLiteDatabase.execSQL(sql.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
