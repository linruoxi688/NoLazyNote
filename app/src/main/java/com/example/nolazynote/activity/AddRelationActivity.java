package com.example.nolazynote.activity;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.example.nolazynote.Dao.AutoTouch;
import com.example.nolazynote.DaoHelper.MyDatabaseHelper;
import com.example.nolazynote.MainActivity;
import com.example.nolazynote.R;

public class AddRelationActivity extends Activity {
    private EditText addtitle,des;
    private TextView addtime;
    private String date;
    private int repeated;
    private AutoTouch autoTouch = new AutoTouch();
    private Switch aSwitch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addrelation);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        date = bundle.getString("id").toString();
        addtitle=(EditText) findViewById(R.id.new_todo_title);
        addtime=(TextView) findViewById(R.id.new_todo_time);
        des = (EditText) findViewById(R.id.new_todo_dsc);
        aSwitch = (Switch) findViewById(R.id.repeat);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    repeated = 1;
                }else {
                    repeated = 0;
                }
            }
        });

    }
    public void save(View view)
    {
        final ContentValues values=new ContentValues();
        values.put("tododate",date);
        values.put("todotitle",addtitle.getText().toString());
        values.put("todotime",addtime.getText().toString());
        values.put("tododsc",des.getText().toString());
        values.put("isRepeat",repeated);
        values.put("userId", MainActivity.userId);
        final MyDatabaseHelper dbHelper=new MyDatabaseHelper(AddRelationActivity.this,"Data.db");
        final AlertDialog.Builder adBuilder=new AlertDialog.Builder(this);
        adBuilder.setMessage("确认保存记录吗？").setPositiveButton("确认",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelper.insert(values);
                Intent intent=getIntent();
                setResult(0x111,intent);
                AddRelationActivity.this.finish();
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        AlertDialog aleraDialog=adBuilder.create();
        aleraDialog.show();
    }

    public void add_time(View view){
        if (view.getId() == R.id.new_todo_time) {
            //弹窗
            TimePickerDialog timePickerDialog = new TimePickerDialog(AddRelationActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String date = String.format("%d:%d", hourOfDay, minute);
                            addtime.setText(date);
                        }
                    }, 0, 0, true);
            timePickerDialog.show();
        }


    }


}

