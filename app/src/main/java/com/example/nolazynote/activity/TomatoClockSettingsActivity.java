package com.example.nolazynote.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.nolazynote.R;

/**
 * 番茄钟设置页面
 * */
public class TomatoClockSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tomato_clock_settings);

        TextView textView;
        Intent intent =getIntent();
        String tomatoTitle = intent.getStringExtra("title");
        textView = findViewById(R.id.itemsTextView);
        textView.setText(tomatoTitle);
        //隐去自带的actionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        //绑定"开始计时"按钮和监听器
        Button button = (Button) findViewById(R.id.startClockButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转至计时器页面
                Intent intent = new Intent();
                intent.setClass(TomatoClockSettingsActivity.this, TomatoClockActivity.class);
                //获取番茄钟个数，并传给计时器页面
                int tomatoNum = 0;
                try {
                    tomatoNum = Integer.parseInt(((EditText)findViewById(R.id.TomatoNumEdit)).getText().toString());
                } catch (Exception e) {
                    return; //未输入正确的番茄钟个数，不应跳转
                }
                if (tomatoNum <= 0) {
                    return; //番茄钟个数输入出错，不应跳转
                }

                intent.putExtra("tomatoNum", tomatoNum);
                startActivity(intent);
            }
        });

        //设置返回按钮
        ImageButton imageButton = findViewById(R.id.returnButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //...此处编写返回代码
            }
        });

    }




}
