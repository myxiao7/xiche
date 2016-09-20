package com.zh.xiche.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.zh.xiche.R;
import com.zh.xiche.base.BaseApplication;

/**
 * 网络检查工具类
 * Created by zhanghao on 2016/8/23.
 */
public class NetWorkUtil {

    private static ConnectivityManager manager;

    private NetWorkUtil(){
        manager = (ConnectivityManager) BaseApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
    }
    private static final NetWorkUtil netWorkUtil = new NetWorkUtil();

    public static NetWorkUtil getInstance(){
        return netWorkUtil;
    }

    /**
     * 检查是否有网络连接
     * @return
     */
    public static boolean isNetWorkConnected(){
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo != null){
            return true;
        }else{
            ToastUtil.showShort(R.string.network_fail);
            return false;
        }
    }

    /**
     * 检查是否WIFI连接
     * @return
     */
    public static boolean isWIFIConnected(){
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if(networkInfo != null){
            if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * 检查是否移动网络连接
     * @return
     */
    public static boolean isMobileConnected() {
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 检查是否移动网络连接
     * @return TYPE_WIFI  1
     *         TYPE_MOBILE 0
     */
    public static int getConnectType() {
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isAvailable()) {
            return networkInfo.getType();
        }
        return -1;
    }


}
