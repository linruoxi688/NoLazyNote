package com.example.nolazynote.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.nolazynote.R;

public class starting extends Activity{
    private final int SPLASH_DISPLAY_LENGHT = 2000;  //延迟2秒
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(starting.this, LoginActivity.class);
                starting.this.startActivity(intent);
                starting.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }
}

