package com.example.nolazynote.Service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.nolazynote.Dao.ToDoDao;
import com.example.nolazynote.Receiver.ToDoAlarmReceiver;
import com.example.nolazynote.entity.ToDos;

import java.util.Calendar;
import java.util.List;

public class ToDoAlarmService extends Service {
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Intent startNotification;
    private String userId;
    public ToDoAlarmService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("service", "create###############");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userId = intent.getStringExtra("userId");
        Log.i("service", "start###############"+userId);
        Calendar calendarTime = Calendar.getInstance();
        calendarTime.setTimeInMillis(System.currentTimeMillis());
        List<ToDos> todosList = new ToDoDao(this).getTodaysTask(userId);
        if (todosList != null) {
            try {
                for (ToDos todos : todosList) {
                    if (todos.getRemindTime() - System.currentTimeMillis() > 0 ) {
                        startNotification = new Intent(ToDoAlarmService.this, ToDoAlarmReceiver.class);
                        startNotification.putExtra("title", todos.getTitle());
                        startNotification.putExtra("dsc", todos.getDesc());
                        startNotification.setAction("AlarmBroadcast");
                        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        pendingIntent = PendingIntent.getBroadcast(this, todos.getId(), startNotification, PendingIntent.FLAG_UPDATE_CURRENT);
                        if (todos.getIsRepeat() == 0){
                            alarmManager.set(AlarmManager.RTC_WAKEUP, todos.getRemindTime(), pendingIntent);
                            Log.i("service", "alarm1");
                        }else if (todos.getIsRepeat() == 1){
                            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, todos.getRemindTimeNoDay(), 1000 * 60 * 60 * 24, pendingIntent);
                            Log.i("service", "alarm2");
                        }
                        ToDos cur = new ToDoDao(this).getTask(todos.getId());
                        if (cur != null) {
                            new ToDoDao(this).setisAlerted(todos.getId());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("service", "destory###############");
    }
}