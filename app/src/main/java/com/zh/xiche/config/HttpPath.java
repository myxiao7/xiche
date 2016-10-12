package com.zh.xiche.config;

/**
 * 接口地址
 * Created by win7 on 2016/9/18.
 */
public class HttpPath {
    public static final String HOST = "http://139.224.41.12:8080/chuXiaoDing/";
    /**
     * 登录
     */
    public static final String LOGIN = "xtgl_operator_appLogin.action";
    /**
     * 获取验证码
     */
    public static final String GETCODE = "xtgl_operator_checkphone.action";
    /**
     * 注册
     */
    public static final String REGISTER = "xtgl_operator_appAdd.action";
    /**
     * 修改个人信息
     */
    public static final String MODIFYINFO = "xtgl_operator_updateInfo.action";
    /**
     * 修改个人头像
     */
    public static final String MODIFYICON = "uploadFile_execute.action";
    /**
     * 修改个人位置
     */
    public static final String MODIFYLOCATION = "xtgl_operator_updateLocation.action";
    /**
     * 接单
     */
    public static final String ORDERACCEPT = "chuXiaoDing_orders_acceptOrder.action";
    /**
     * 完成订单
     */
    public static final String ORDERFINISH = "chuXiaoDing_orders_acceptOrder.action";
    /**
     * 拒单
     */
    public static final String ORDERREFUSER = "chuXiaoDing_orders_denyOrder.action";
    /**
     * 待接单列表
     */
    public static final String LISTWAIT = "chuXiaoDing_orders_queryAppAsignOrders.action";
    /**
     * 待服务订单
     */
    public static final String ORDERLIST_WAIT = "chuXiaoDing_orders_queryAppAcceptOrders.action";
    /**
     * 已服务订单
     */
    public static final String ORDERLIST_FINISH = "chuXiaoDing_orders_queryAppFinishOrders.action";

    /**
     * 日账单列表
     */
    public static final String BILLBYDAY = "chuXiaoDing_orders_queryAppDyaBillOrders.action";
    /**
     * 月账单列表
     */
    public static final String BILLBYMONUTH = "chuXiaoDing_dayBill_queryAppDayBill.action";
    /**
     * 年账单列表
     */
    public static final String BILLBYYEAR = "chuXiaoDing_monthBill_queryAppMonthBill.action";
    /**
     * 总账单列表
     */
    public static final String BILLBYALL = "chuXiaoDing_yearBill_queryYearAppBill.action";
    /**
     * 消息列表
     */
    public static final String NOTICELIST = "chuXiaoDing_notice_query.action";
    /**
     * 注销
     */
    public static final String LOGINOUT = "xtgl_operator_exsitLogin.action";
    /**
     * 忘记密码_获取验证码
     */
    public static final String FORGETPWD_GETCODE = "xtgl_operator_retrieveGetCode.action";
    /**
     * 忘记密码_验证验证码
     */
//    public static final String FORGETPWD_CHECKCODE = "xtgl_operator_checkePwdCode.action";
    /**
     * 忘记密码_修改密码
     */
    public static final String FORGETPWD = "xtgl_operator_updatePwd.action";


    /**
     * 获取接口路径
     *
     * @return
     */
    public static String getPath(String action) {
        String host = HttpPath.HOST;
        return host + action;
    }


}
