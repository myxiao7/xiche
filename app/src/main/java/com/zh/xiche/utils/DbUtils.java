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

    /**
     * 更新用户状态
     * @return
     */
    public void updateState(int state){
        UserInfoEntity entity = null;
        try {
            entity = getPersonInfo();
            entity.setIspass(state);
            dbManager.update(entity, "ispass");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    /**
     * 更新用户头像
     * @return
     */
    public void updateIcon(String url){
        UserInfoEntity entity = null;
        try {
            entity = getPersonInfo();
            entity.setAvatar(url);
            dbManager.update(entity, "avatar");
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    /**
     * 更新用户信息
     * @return
     */
    public void updateUserInfo(int id, String str){
        UserInfoEntity entity = null;
        try {
            entity = getPersonInfo();
            switch (id){
                case 1:
                    entity.setName(str);
                    dbManager.update(entity, "name");
                    break;
                case 2:
                    entity.setCardno(str);
                    dbManager.update(entity, "cardno");
                    break;
                case 3:
                    entity.setLocation(str);
                    dbManager.update(entity, "location");
                    break;
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}
