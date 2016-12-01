package com.zh.xiche.entity;

import android.app.Activity;
import android.content.Intent;

import com.zh.xiche.R;
import com.zh.xiche.config.SharedData;
import com.zh.xiche.ui.LoginActivity;
import com.zh.xiche.utils.DialogUtils;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

/**
 * Created by zhanghao on 2016/10/9.
 */

public class ResultEntity {
    private boolean message;
    private int error_code;
    private String error_desc;
    private UserInfoEntity operatorDTO;

    public boolean getMessage() {
        return message;
    }

    public void setMessage(boolean message) {
        this.message = message;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getError_desc() {
        return error_desc;
    }

    public void setError_desc(String error_desc) {
        this.error_desc = error_desc;
    }

    public UserInfoEntity getOperatorDTO() {
        return operatorDTO;
    }

    public void setOperatorDTO(UserInfoEntity operatorDTO) {
        this.operatorDTO = operatorDTO;
    }
    /**
     * 是否请求成功
     * @return
     */
    public boolean isSuccee(final Activity activity){
        if(message){
            return true;
        }else{
            if(error_code ==1){
                DialogUtils.showProgress(activity, R.string.logout);
                JPushInterface.setAlias(activity, "", new TagAliasCallback() {
                    @Override
                    public void gotResult(int i, String s, Set<String> set) {
                        DialogUtils.stopProgress(activity);
                        SharedData.saveUserPwd("");
                        System.exit(0);
                        Intent intent1 = new Intent(activity, LoginActivity.class);
                        activity.startActivity(intent1);
                        activity.finish();
                    }
                });
            }
        }
        return false;
    }
}
