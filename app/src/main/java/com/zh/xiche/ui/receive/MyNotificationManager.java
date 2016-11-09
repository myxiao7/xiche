package com.zh.xiche.ui.receive;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.zh.xiche.R;
import com.zh.xiche.ui.MainActivity;


/**
 * 通知管理
 * Created by zhanghao on 2016/9/28.
 */
public class MyNotificationManager {

    private static MyNotificationManager notificationManager;
    private MyNotificationManager(){
    }

    public static MyNotificationManager getInstance(){
        if(notificationManager == null){
            notificationManager = new MyNotificationManager();
        }
        return notificationManager;
    }

    /**
     * 显示通知
     * @param context
     * @param title
     * @param message
     */
    public void showNotifi(Context context, String title, String message){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentTitle(title)
                .setContentText(message)
                .setTicker("派单")
                .setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.mipmap.ic_car);
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, builder.build());


    }

    /**
     * 显示通知 跳转
     * @param context
     * @param title
     * @param message
     */
    public void showNotifiClick(Context context, String title, String message){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent intent1 = PendingIntent.getActivity(context,1,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentTitle(title)
                .setContentText(message)
                .setTicker("派单")
                .setAutoCancel(true)
                .setContentIntent(intent1)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.mipmap.ic_launcher);
        ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, builder.build());
    }
}
