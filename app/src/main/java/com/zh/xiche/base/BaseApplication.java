package com.zh.xiche.base;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import org.xutils.x;

/**
 * Created by win7 on 2016/9/18.
 */
public class BaseApplication extends Application {
    public static final String LOG_TAG = "XC";
    private static BaseApplication application;

    public synchronized static BaseApplication getInstance(){
        return  application;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        SDKInitializer.initialize(this);
        x.Ext.init(this);
        x.Ext.setDebug(true); // 是否输出debug日志
        //全局异常捕获
        CrashHandler.getInstance().init(this);
    }
}
