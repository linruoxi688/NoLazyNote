package com.example.nolazynote.DaoHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private static final String TABLE_NAME="Todo";
    public static final String TODO = "create table Todo ("
            + "_id integer primary key autoincrement, "
            + "todotitle String, "
            + "tododsc String,"
            + "tododate String,"
            + "todotime String,"
            + "remindTime long,"
            + "remindTimeNoDay long,"
            + "isAlerted int,"
            + "imgId int,"
            + "isRepeat int,"
            + "isFinished boolean,"
            + "userId String)";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name){
        super(context, name, null, 1);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(TODO);
        Log.i("database","a");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("drop table if exists Todo");
        db.execSQL("drop table if exists Clock");
        db.execSQL("drop table if exists timer_schedule");
        onCreate(db);
    }

    public void insert(ContentValues values){
        SQLiteDatabase db=getWritableDatabase();
        db.insert(TABLE_NAME,null,values);
        db.close();
    }
    public void del(int data){
        if(null==db)
            db=getWritableDatabase();
        db.delete(TABLE_NAME,"_id=?",new String[]{String.valueOf(data)});
    }
    public Cursor query(String data1,String userId){
        SQLiteDatabase db=getWritableDatabase();
        Cursor cursor=db.query(TABLE_NAME,null,"tododate=? and userId=?",new String[]{data1,userId},null,null,null);
        return cursor;
    }

    public Cursor query1(int id){
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(TABLE_NAME,null,"_id=?",new String[]{String.valueOf(id)},null,null,null);
        return cursor;
    }

    public void close()
    {
        if(db!=null)
            db.close();
    }

}
