package com.zh.xiche.entity;

/**
 * 图片上传
 * Created by zhanghao on 2016/10/9.
 */

public class UploadEntity {
    private boolean message;
    private String url;

    public boolean isMessage() {
        return message;
    }

    public void setMessage(boolean message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 是否请求成功
     * @return
     */
    public boolean isSuccee(){
        if(message){
            return true;
        }else{
            return false;
        }
    }

}
