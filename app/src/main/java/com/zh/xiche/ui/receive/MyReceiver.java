package com.zh.xiche.ui.receive;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.zh.xiche.entity.JsonModel;
import com.zh.xiche.entity.OrderEntity;
import com.zh.xiche.entity.PushEntity;
import com.zh.xiche.utils.DbUtils;
import com.zh.xiche.utils.GsonUtil;
import com.zh.xiche.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by zhanghao on 2016/9/28.
 */
public class MyReceiver extends BroadcastReceiver{
    private static final String TAG = "MyReceiver";
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
//        Log.d(TAG, "onReceive - " + intent.getAction());
//        LogUtil.d(printBundle(bundle));
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {

        }else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//            Log.d(TAG, "onReceive - " + bundle.getString(JPushInterface.EXTRA_TITLE));
//            Log.d(TAG, "onReceive - " + bundle.getString(JPushInterface.EXTRA_ALERT));
            Log.d(TAG, "onReceive - " + bundle.getString(JPushInterface.EXTRA_EXTRA));
//            Log.d(TAG, "onReceive - " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
//            System.out.println("收到了自定义消息。消息内容是：" + bundle.getString(JPushInterface.EXTRA_EXTRA));
            // 自定义消息不会展示在通知栏，完全要开发者写代码去处理
            String result = bundle.getString(JPushInterface.EXTRA_EXTRA);
            LogUtil.d(result);
            if(!TextUtils.isEmpty(result)){
                Type type = new TypeToken<PushEntity>(){}.getType();
                PushEntity entity = GsonUtil.GsonToBean(result, type);
                switch (entity.getMessage_type()){
                    case "1":
                        //审核信息
                        if(entity.getAudit_conclusion().equals("1")){
                            MyNotificationManager.getInstance().showNotifiClick(context, "初小丁", "恭喜您，您的信息已经审核通过,点我去接单");
                        }
                        break;
                    case "2":
                        //正常推送订单
                        MyNotificationManager.getInstance().showNotifi(context, "初小丁", "您有一条新的订单");
                        Intent intent1 = new Intent(context, GiveOrderNormalActivity.class);
                        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent1.putExtra("order", entity);
                        context.startActivity(intent1);
                        break;
                    case "3":
                        //管理员派送单
                        MyNotificationManager.getInstance().showNotifi(context, "初小丁", "系统给您派发了一条新的订单");
                        Intent intent2 = new Intent(context, GiveOrderActivity.class);
                        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent2.putExtra("order", entity);
                        context.startActivity(intent2);
                        break;
                    case "4":
                        //自定义消息
                        break;
                }
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
//            System.out.println("收到了通知");
            LogUtil.d("收到了通知222222222");
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

    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it =  json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " +json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

}
