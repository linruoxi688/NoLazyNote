package com.example.nolazynote.Dao;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.nolazynote.DaoHelper.MyDatabaseHelper;
import com.example.nolazynote.R;


public class Matter_update extends Activity {
    private static int data ;
    private static Button delete,update_btn;
    private static EditText des,title;
    private TextView time;
    private Switch repeat;


    @Override
    @SuppressLint("Range")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matters);
        delete = (Button) findViewById(R.id.delete1);
        des = (EditText)findViewById(R.id.new_todo_dsc1);
        title = (EditText) findViewById(R.id.new_todo_title1);
        time = (TextView) findViewById(R.id.new_todo_time1);
        repeat = (Switch) findViewById(R.id.repeat1);
        //update_btn = (Button) findViewById(R.id.update);
        getDataFromDB();
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MyDatabaseHelper dbHelper=new MyDatabaseHelper(Matter_update.this,"Data.db");
                dbHelper.del(data);
                Matter_update.this.finish();
            }
        });


        //cursor.moveToFirst();
    }

    @SuppressLint("Range")
    public void getDataFromDB(){

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        data = Integer.valueOf(bundle.getString("id").toString());
        final MyDatabaseHelper dbHelper=new MyDatabaseHelper(Matter_update.this,"Data.db");
        Cursor cursor = dbHelper.query1(data);
        String title_text="",timetext="",des_text="";
        int repeated = 0;
        if (cursor.moveToFirst()){
            timetext = cursor.getString(cursor.getColumnIndex("todotime"));
            title_text = cursor.getString(cursor.getColumnIndex("todotitle"));
            //position_text = cursor.getString(cursor.getColumnIndex("pos"));
            des_text = cursor.getString(cursor.getColumnIndex("tododsc"));
            repeated = cursor.getInt(cursor.getColumnIndex("isRepeat"));
        }
        cursor.moveToFirst();
        cursor.moveToNext();
        String tem = Integer.toString(data);
        title.setText(title_text);
        des.setText(des_text);
        Boolean te = false;
        time.setText(timetext);
        if (repeated == 1){
            te = true;
        }
        repeat.setChecked(te);
        //place.setText(des_text);

    }
}
