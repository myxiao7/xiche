package com.zh.xiche.entity;

/**
 * Created by zhanghao on 2016/10/9.
 */

public class ResultEntity {
    private boolean message;
    private UserInfoEntity operatorDTO;

    public boolean getMessage() {
        return message;
    }

    public void setMessage(boolean message) {
        this.message = message;
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
    public boolean isSuccee(){
        if(message){
            return true;
        }else{
            return false;
        }
    }

}
