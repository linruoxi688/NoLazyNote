package com.example.nolazynote.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.nolazynote.Dao.NoteDao;
import com.example.nolazynote.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Intent intent = this.getIntent();
        String taskContent = intent.getStringExtra("content");

        Button btn = findViewById(R.id.btn_back);
        final EditText editText = findViewById(R.id.et_content);

        editText.setText(taskContent);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoteDao noteDao = new NoteDao(AddTaskActivity.this);
                noteDao.add(getTime(),editText.getText().toString());
                finish();
            }
        });
    }

    String getTime(){
        long time=System.currentTimeMillis();//long now = android.os.SystemClock.uptimeMillis();

        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1=new Date(time);

        String t1=format.format(d1);
        return t1;


    }
}
