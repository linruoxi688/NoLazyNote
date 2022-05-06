package com.example.nolazynote.activity;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;
import java.util.Random;
        import android.app.DatePickerDialog;
        import android.app.TimePickerDialog;
        import android.content.Intent;
        import android.net.Uri;
        import android.os.Build;
        import android.provider.Settings;
        import android.os.Bundle;
import android.util.Log;
import android.view.View;
        import android.widget.Button;
        import android.widget.CompoundButton;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.Switch;
        import android.widget.TextView;
        import android.widget.TimePicker;
        import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.example.nolazynote.R;
import com.example.nolazynote.entity.ToDos;
import com.example.nolazynote.Dao.ToDoDao;
import com.example.nolazynote.Service.ToDoAlarmService;


public class NewToDoActivity extends AppCompatActivity {

    private String todoTitle,todoDsc;
    private String todoDate = null, todoTime = null;
    private Button ok;
    private TextView nv_todo_date,nv_todo_time;
    private EditText nv_todo_title,nv_todo_dsc;
    private Switch nv_repeat;
    private int mYear,mMonth,mDay;
    private int mHour,mMin;
    private long remindTime;
    private Calendar ca;
    private static final String TAG = "time";
    private int isRepeat = 0;
    private ImageView new_bg;
    private static int[] imageArray = new int[]{R.drawable.img_1,
            R.drawable.img_2,
            R.drawable.img_3,
            R.drawable.img_4,
            R.drawable.img_5,
            R.drawable.img_6,
            R.drawable.img_7,
            R.drawable.img_8,};
    private int imgId;
    private ToDos todos;
    private String userId;
//    private MaterialDialog voice;
//    private ButtonStyle done;
//    private PermissionPageUtils permissionPageUtils;

    private int[] data={
            0,0,0,0,1,0,0,0,18,19,
            21,18,9,9,16,20,18,11,17,13,
            17,12,16,16,20,16,5,1,0,4,
            16,17,9,16,20,11,6,16,16,11,
            6,14,16,8,5,13,13,6,2,16,
            18,12,7,13,15,13,4,1,18,15,
            7,3,14,13,6,4,12,10,15,12,
            1,1,15,20,0,3,10,1,8,17};
    private Button mic_title,mic_dsc;
    private boolean enableOffline = true;
//    private EventManager manager;
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_todo);
        Bundle bundle = this.getIntent().getBundleExtra("users");
        userId = bundle.getString("user");
        Log.i("user!!!",userId+"&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
        ca = Calendar.getInstance();
        getDate();
        getTime();
        initView();
        initHeadImage();
//        checkNotificationPermission();
    }

    private void initHeadImage(){

        Random random = new Random();
        imgId = imageArray[random.nextInt(8)];
        new_bg.setImageResource(imgId);
    }

    private void getDate(){
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
    }

    private void getTime(){
        mHour = ca.get(Calendar.HOUR_OF_DAY);
        mMin = ca.get(Calendar.MINUTE);
    }

    private void initView() {

        ok = (Button) findViewById(R.id.ok);
        nv_todo_title = (EditText) findViewById(R.id.new_todo_title);
        nv_todo_dsc = (EditText) findViewById(R.id.new_todo_dsc);
        nv_todo_time = (TextView) findViewById(R.id.new_todo_time);
        nv_repeat = (Switch) findViewById(R.id.repeat);
        new_bg = (ImageView) findViewById(R.id.new_bg);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (todoTime==null) {
                    Toast.makeText(NewToDoActivity.this, "没有设置提醒时间", Toast.LENGTH_SHORT).show();
                } else {
                    todoTitle = nv_todo_title.getText().toString();
                    todoDsc = nv_todo_dsc.getText().toString();
                    Calendar calendarTime = Calendar.getInstance();
                    calendarTime.setTimeInMillis(System.currentTimeMillis());
                    calendarTime.set(Calendar.YEAR, mYear);
                    calendarTime.set(Calendar.MONTH, mMonth);
                    calendarTime.set(Calendar.DAY_OF_MONTH, mDay);
                    calendarTime.set(Calendar.HOUR_OF_DAY, mHour);
                    calendarTime.set(Calendar.MINUTE, mMin);
                    calendarTime.set(Calendar.SECOND, 0);
                    remindTime = calendarTime.getTimeInMillis();
                    todos = new ToDos();
//                    todos.setUser(user);
                    todos.setTitle(todoTitle);
                    todos.setDesc(todoDsc);
                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH)+1;
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    todos.setDate(year + "年" + month + "月" + day + "日");
                    todos.setTime(todoTime);
                    todos.setRemindTime(remindTime);
                    todos.setRemindTimeNoDay(remindTime);
                    todos.setisAlerted(0);
                    todos.setIsRepeat(isRepeat);
                    todos.setImgId(imgId);
                    todos.setIsFinshed(0);
                    todos.setUserId(userId);
                    Date date = new Date(remindTime);
                    new ToDoDao(NewToDoActivity.this).create(todos);
                    Intent intent = new Intent(NewToDoActivity.this, ToDoAlarmService.class);
                    intent.putExtra("userId", userId);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startService(intent);
                    finish();
                }
            }
        });

        nv_todo_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(NewToDoActivity.this, onTimeSetListener, mHour,mMin, true);
                timePickerDialog.setCancelable(true);
                timePickerDialog.setCanceledOnTouchOutside(true);
                timePickerDialog.show();
            }
        });

        nv_repeat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked){
                    isRepeat = 1;
                } else {
                    isRepeat = 0;
                }
            }
        });
        Log.i("ii","2");
    }

    public TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            mHour = hour;
            mMin = minute;
            if (minute < 10){
                todoTime = hour + ":" + "0" + minute;
            } else {
                todoTime = hour + ":" + minute;
            }
            nv_todo_time.setText(todoTime);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
