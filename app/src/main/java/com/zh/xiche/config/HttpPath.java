package com.zh.xiche.config;

/**
 * 接口地址
 * Created by win7 on 2016/9/18.
 */
public class HttpPath {
    public static final String HOST = "";
    /**
     * 获取接口路径
     *
     * @return
     */
    public static String getPath(String action) {
        String host = SharedData.getHost();
        if (host == null) {
            host = HttpPath.HOST;
        }
        return host + action;
    }


}
