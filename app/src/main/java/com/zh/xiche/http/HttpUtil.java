package com.zh.xiche.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import org.xutils.HttpManager;
import org.xutils.http.RequestParams;
import org.xutils.x;


/**
 * Created by zcw on 2015-11-25.
 */
public class HttpUtil {
    private static final String TAG = "HttpUtil";
    public  final static   String CHARSET_UTF8="UTF-8";
    public  final static   String CHARSET="GBK";

    /**
     * 获取请求对象
     * @return
     */
    public  static HttpManager http(){
        HttpManager httpManager = x.http();
        return  httpManager;
    }

    /**
     *获取请求参数
     */
    public  static RequestParams params(){
        RequestParams params = new RequestParams();
        params.setCharset(CHARSET_UTF8);
        return  params;
    }

    /**
     * 获取请求参数
     * @param url 路径
     * @return
     */
    public  static RequestParams params(String url){
        RequestParams params = new RequestParams(url);
        params.setCharset(CHARSET_UTF8);
        return  params;
    }


    /**
     * 是否有网络
     * @param context
     * @return
     */
    public static boolean isNetWorkHave(Context context) {
        boolean netstate = false;
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        netstate = true;
                        break;
                    }
                }
            }
        }
        return netstate;
    }

    /**
     * 网络是否为漫游
     * @param context
     * @return
     */
    public static boolean isNetworkRoaming(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            Log.w(TAG, "couldn't get connectivity manager");
        } else {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null
                    && info.getType() == ConnectivityManager.TYPE_MOBILE) {
                TelephonyManager tm = (TelephonyManager) context
                        .getSystemService(Context.TELEPHONY_SERVICE);
                if (tm != null && tm.isNetworkRoaming()) {
                    Log.d(TAG, "network is roaming");
                    return true;
                } else {
                    Log.d(TAG, "network is not roaming");
                }
            } else {
                Log.d(TAG, "not using mobile network");
            }
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static boolean isMobileDataEnable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isMobileDataEnable = false;

        isMobileDataEnable = connectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();

        return isMobileDataEnable;
    }

    /**
     * 判断wifi 是否可用
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static boolean isWifiDataEnable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiDataEnable = false;
        isWifiDataEnable = connectivityManager.getNetworkInfo(
                ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        return isWifiDataEnable;
    }


}
