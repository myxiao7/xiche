package com.zh.xiche.base;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import org.xutils.DbManager;
import org.xutils.x;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by win7 on 2016/9/18.
 */
public class BaseApplication extends Application {
    public static final String LOG_TAG = "XC";
    private static BaseApplication application;
    private static DbManager.DaoConfig daoConfig;

    public synchronized static BaseApplication getInstance(){
        return  application;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        SDKInitializer.initialize(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
        x.Ext.init(this);
        x.Ext.setDebug(true); // 是否输出debug日志
        //全局异常捕获
        CrashHandler.getInstance().init(this);

        daoConfig =  new DbManager.DaoConfig()
                .setDbName("jx_db")
                .setDbVersion(1)
                .setDbOpenListener(new DbManager.DbOpenListener() {
                    @Override
                    public void onDbOpened(DbManager db) {
                        // 开启WAL, 对写入加速提升巨大
                        db.getDatabase().enableWriteAheadLogging();
                    }
                });
    }

    public DbManager getDbManger(){
        return  x.getDb(daoConfig);
    }
}
