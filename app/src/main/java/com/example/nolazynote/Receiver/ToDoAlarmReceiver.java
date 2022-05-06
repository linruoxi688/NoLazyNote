package com.example.nolazynote.Receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import com.example.nolazynote.MainActivity;
import com.example.nolazynote.R;
import com.example.nolazynote.Service.ToDoAlarmService;

public class ToDoAlarmReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID_1 = 0x00113;
    private String title;
    private String dsc;
    private static final String TAG = "receiver";
    private static final String KEY_RINGTONE = "ring_tone";
    private static final String KEY_VIBRATE = "vibrator";
    private String ringTone;
    private String CHANNEL_ID = "todo";
    private String CHANNEL_NAME = "Todo Notification";

    public ToDoAlarmReceiver() {
        super();
        Log.i("receiver", "new");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        title = intent.getStringExtra("title");
        dsc = intent.getStringExtra("dsc");
        Log.i("receiver", "alarm");
        showNormal(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, ToDoAlarmService.class);
        context.startService(intent);
    }

    private void showNormal(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setTicker(title)
                .setContentInfo("事项提醒")
                .setContentTitle(title)
                .setContentText(dsc)
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setFullScreenIntent(pi, true)
                .setContentIntent(pi);
        if (Build.VERSION.SDK_INT >= 21) {
            builder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(notificationChannel);
            builder.setChannelId(CHANNEL_ID);
        }
        builder.build();
        manager.notify(NOTIFICATION_ID_1, builder.build());
    }
}
