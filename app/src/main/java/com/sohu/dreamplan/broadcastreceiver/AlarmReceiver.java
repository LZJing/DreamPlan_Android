package com.sohu.dreamplan.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sohu.dreamplan.activity.AlarmActivity;

/**
 * Created by lizha on 2015/8/26.
 */
public class AlarmReceiver extends BroadcastReceiver {
//private NotificationManager manager;

    @Override
    public void onReceive(Context context, Intent intent) {

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context,AlarmActivity.class);
        context.startActivity(intent);


//        manager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
//        builder.setContentTitle("title").setContentText("提醒内容").setSmallIcon(R.drawable.meinv).setDefaults(Notification.DEFAULT_ALL).setAutoCancel(true).setSubText("二级text");
//        manager.notify(1, builder.build());
    }
}
