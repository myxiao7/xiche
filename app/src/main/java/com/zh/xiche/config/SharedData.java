package com.zh.xiche.config;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.zh.xiche.base.BaseApplication;

/**
 * 本地存储相关
 * Created by win7 on 2016/9/18.
 */
public class SharedData {
    /**
     * 服务器地址
     */
    public static final String HOST = "host";

    /**
     * 用户名
     */
    public static final String USERNAME = "username";

    /**
     * 密码
     */
    public static final String USERPWD = "userpwd";

    /**
     * 电话
     */
    public static final String USERPHONE = "userphone";

    /**
     * 经杜
     */
    public static final String CURRENTLON = "CURRENTLON";
    /**
     * 纬度
     */
    public static final String CURRENTLAT = "CURRENTLAT";


    private static SharedPreferences sharedPreferences;

    public static SharedPreferences getShare() {
        if (sharedPreferences == null) {
            sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(BaseApplication.getInstance());

        }
        return sharedPreferences;
    }

    /**
     * 获取服务地址
     *
     * @return
     */
    public static String getHost() {
        String host = getShare().getString(HOST, null);
        return host;
    }

    /**
     * 保存服务地址
     *
     * @return
     */
    public static void saveHost(String host) {
        getShare().edit().putString(HOST, host).commit();
    }

    /**
     * 获取用户名
     *
     * @return
     */
    public static String getUserName() {
        String host = getShare().getString(USERNAME, null);
        return host;
    }

    /**
     * 保存用户名
     *
     * @return
     */
    public static void saveUserName(String name) {
        getShare().edit().putString(USERNAME, name).commit();
    }

    /**
     * 获取密码
     *
     * @return
     */
    public static String getUserPwd() {
        String host = getShare().getString(USERPWD, null);
        return host;
    }

    /**
     * 保存密码
     *
     * @return
     */
    public static void saveUserPwd(String pwd) {
        getShare().edit().putString(USERPWD, pwd).commit();
    }

    /**
     * 获取电话
     *
     * @return
     */
    public static String getUserPhone() {
        String host = getShare().getString(USERPHONE, null);
        return host;
    }

    /**
     * 保存电话
     *
     * @return
     */
    public static void saveUserPhone(String phone) {
        getShare().edit().putString(USERPHONE, phone).commit();
    }

    /**
     * 获取当前经度
     *
     * @return
     */
    public static String getCurrentlon() {
        String host = getShare().getString(CURRENTLON, null);
        return host;
    }

    /**
     * 保存当前经度
     *
     * @return
     */
    public static void saveCurrentlon(String lon) {
        getShare().edit().putString(CURRENTLON, lon).commit();
    }

    /**
     * 获取当前纬度
     *
     * @return
     */
    public static String getCurrentlat() {
        String host = getShare().getString(CURRENTLAT, null);
        return host;
    }

    /**
     * 保存当前纬度
     *
     * @return
     */
    public static void saveCurrentlat(String lat) {
        getShare().edit().putString(CURRENTLAT, lat).commit();
    }

}
