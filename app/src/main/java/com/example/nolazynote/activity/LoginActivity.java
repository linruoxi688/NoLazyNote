package com.example.nolazynote.activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.example.nolazynote.Dao.userDao;
import com.example.nolazynote.DaoHelper.userSql;
import com.example.nolazynote.MainActivity;
import com.example.nolazynote.R;

import static android.widget.Toast.LENGTH_SHORT;

public class LoginActivity extends AppCompatActivity {

    public String userId = "";
    public String password = "";
    private userSql helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void onClick(View view) {
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);

        password = passwordEditText.getText().toString();
        userId = usernameEditText.getText().toString();
        int error;

        if (userId.length() == 0) {
            Toast.makeText(this, "请输入用户名", LENGTH_SHORT).show();
        } else if (password.length() == 0) {
            Toast.makeText(this, "请输入密码", LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(this, "密码长度不足6位", LENGTH_SHORT).show();
        } else {
            //loadingProgressBar.setVisibility(View.VISIBLE);//转圈圈
            error = jianyan(userId, password);
            if (error == 0) {
                //跳转界面
                //..
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Bundle mBundle = new Bundle();
                mBundle.putString("ID",userId);
                intent.putExtra("loginMsg",mBundle);
                startActivity(intent);
            }
        }
    }

    public void send()//注册新用户
    {
        userDao userdao = new userDao(this);
        long id = userdao.add(userId, password);
    }

    public int jianyan(String aid, String bpass)//如果已经注册则判断密码用户名是否一致，否则添加新用户
    {
        int err = 0;
        int count = 0;
        String id;
        String pass;
        helper = new userSql(this);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from user", null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.move(1);
            id = cursor.getString(1);
            pass = cursor.getString(2);
            System.out.println("——————————————————用户名：" + id + "密码：" + pass);
            if (aid.equals(id) && !bpass.equals(pass)) {
                Toast.makeText(this, "密码错误", Toast.LENGTH_SHORT).show();
                count = 1;
                err = 1;
            }
            if (aid.equals(id) && bpass.equals(pass)) {
                Toast.makeText(this, "登陆成功，欢迎你" + id, Toast.LENGTH_SHORT).show();
                count = 1;
            }
        }
        if (count != 1) {
            Toast.makeText(this, "注册成功，欢迎你", Toast.LENGTH_SHORT).show();
            send();//注册（添加新用户）
        }
        return err;
    }
}