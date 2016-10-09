package com.zh.xiche.utils;

import com.zh.xiche.base.BaseApplication;
import com.zh.xiche.entity.UserInfoEntity;

import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;

/**
 * Created by zhanghao on 2016/9/30.
 */

public class DbUtils {

    private static DbUtils dbUtils = null;
    private static DbManager dbManager = BaseApplication.getInstance().getDbManger();
    private DbUtils(){

    }

    public static  DbUtils getInstance(){
        if(dbUtils == null){
            dbUtils  = new DbUtils();
        }
        return dbUtils;
    }

    /**
     * 保存用户信息
     * @param entity
     */
    public void savePersonInfo(UserInfoEntity entity){
        try {
            dbManager.save(entity);
            LogUtil.d("__________保存用户信息DB");
        } catch (DbException e) {
            LogUtil.d("保存用户信息DB" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     *  清空用户信息
     */
    public void clearPersonInfo(){
        try {
            dbManager.delete(UserInfoEntity.class);
            LogUtil.d("____________清空用户信息");
        } catch (DbException e) {
            LogUtil.d("清空用户信息DB" + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * 获取当前用户信息
     * @return
     */
    public UserInfoEntity getPersonInfo(){
        UserInfoEntity entity = null;
        try {
            entity = dbManager.findFirst(UserInfoEntity.class);
            LogUtil.d("____________获取当前用户信息");
        } catch (DbException e) {
            LogUtil.d("获取当前用户信息DB" + e.getMessage());
            e.printStackTrace();
        }
        return entity;
    }
}
