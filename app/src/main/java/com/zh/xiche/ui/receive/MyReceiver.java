package com.zh.xiche.ui.receive;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.zh.xiche.utils.DbUtils;

import java.lang.reflect.Type;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by zhanghao on 2016/9/28.
 */
public class MyReceiver extends BroadcastReceiver{
    private static final String TAG = "MyReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "onReceive - " + intent.getAction());

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
        }else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
            String result = intent.getAction();
           /* Type type = new TypeToken<PushEntity>(){}.getType();
            PushEntity entity = GsonUtil.gsonToBean(result, type);*/

            MyNotificationManager.getInstance().showNotifi(context, "您有一条新的订单", "系统给您推荐个一条新的订单");
            Intent intent1 = new Intent(context, GiveOrderActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            System.out.println("收到了通知");
            // 在这里可以做些统计，或者做些其他工作
        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            System.out.println("用户点击打开了通知");
            // 在这里可以自己写代码去定义用户点击后的行为
           /* Intent i = new Intent(context, TestActivity.class);  //自定义打开的界面
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);*/
        } else {
            Log.d(TAG, "Unhandled intent - " + intent.getAction());
        }
    }
}
