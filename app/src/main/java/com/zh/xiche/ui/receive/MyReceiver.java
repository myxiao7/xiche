package com.zh.xiche.ui.receive;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.zh.xiche.entity.JsonModel;
import com.zh.xiche.entity.OrderEntity;
import com.zh.xiche.utils.DbUtils;
import com.zh.xiche.utils.GsonUtil;

import java.lang.reflect.Type;
import java.util.List;

import cn.jpush.android.api.JPushInterface;


/**
 * Created by zhanghao on 2016/9/28.
 */
public class MyReceiver extends BroadcastReceiver{
    private static final String TAG = "MyReceiver";
    private String str = "{\n" +
            "    \"dataList\": [\n" +
            "        {\n" +
            "            \"acceptdate\": \"2016-09-20 21:49:19\",\n" +
            "            \"appointment\": \"明天8:00-10:00\",\n" +
            "            \"appointmentId\": 0,\n" +
            "            \"assigndate\": null,\n" +
            "            \"carbrank\": \"宝马\",\n" +
            "            \"carcolor\": \"灰色\",\n" +
            "            \"carno\": \"鲁MX3333\",\n" +
            "            \"carstyle\": \"X7\",\n" +
            "            \"finishDate\": null,\n" +
            "            \"lat\": 37.854,\n" +
            "            \"location\": \"山东省滨州市\",\n" +
            "            \"lon\": 114.392,\n" +
            "            \"mobile\": \"18800006666\",\n" +
            "            \"monthIncome\": 0,\n" +
            "            \"monthOrdersNum\": 0,\n" +
            "            \"name\": \"李四\",\n" +
            "            \"operator\": \"胡技师\",\n" +
            "            \"operid\": 114,\n" +
            "            \"opmobile\": \"18809125687\",\n" +
            "            \"orderamount\": 50,\n" +
            "            \"orderdate\": \"2016-09-21 17:01:00\",\n" +
            "            \"orderid\": \"89182fbeace248f99352cd3bcaebdeda\",\n" +
            "            \"paydate\": null,\n" +
            "            \"paystyle\": null,\n" +
            "            \"remark\": \"系统派单\",\n" +
            "            \"servicetype\": null,\n" +
            "            \"servicetypename\": \"汽车精细\",\n" +
            "            \"status\": null,\n" +
            "            \"uname\": \"小李子\",\n" +
            "            \"userid\": 4942\n" +
            "        },\n" +
            "        {\n" +
            "            \"acceptdate\": \"2016-09-20 23:43:38\",\n" +
            "            \"appointment\": \"明天8:00-10:00\",\n" +
            "            \"appointmentId\": 0,\n" +
            "            \"assigndate\": null,\n" +
            "            \"carbrank\": \"宝马\",\n" +
            "            \"carcolor\": \"灰色\",\n" +
            "            \"carno\": \"鲁MX1111\",\n" +
            "            \"carstyle\": \"X6\",\n" +
            "            \"finishDate\": null,\n" +
            "            \"lat\": 37.854,\n" +
            "            \"location\": \"山东省滨州市\",\n" +
            "            \"lon\": 114.392,\n" +
            "            \"mobile\": \"18800006666\",\n" +
            "            \"name\": \"李四\",\n" +
            "            \"operator\": \"胡技师\",\n" +
            "            \"operid\": 114,\n" +
            "            \"opmobile\": \"18809125687\",\n" +
            "            \"orderamount\": 50,\n" +
            "            \"orderdate\": \"2016-09-20 17:01:00\",\n" +
            "            \"orderid\": \"7f1c6fae59984c658a256be39f63cf52\",\n" +
            "            \"paydate\": null,\n" +
            "            \"remark\": \"需要洗车\",\n" +
            "            \"servicetype\": null,\n" +
            "            \"servicetypename\": \"汽车精细\",\n" +
            "            \"uname\": \"小李子\",\n" +
            "            \"userid\": 4942\n" +
            "        }\n" +
            "    ],\n" +
            "    \"page\": 1,\n" +
            "    \"record\": 3,\n" +
            "    \"rows\": 10,\n" +
            "    \"total\": 1\n" +
            "}";
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
            Type type = new TypeToken<JsonModel<List<OrderEntity>>>(){}.getType();
            JsonModel<List<OrderEntity>> jsonModel = GsonUtil.GsonToBean(str, type);

            MyNotificationManager.getInstance().showNotifi(context, "您有一条新的订单", "系统给您推荐个一条新的订单");
            Intent intent1 = new Intent(context, GiveOrderActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent1.putExtra("order", jsonModel.getDataList().get(0));
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
