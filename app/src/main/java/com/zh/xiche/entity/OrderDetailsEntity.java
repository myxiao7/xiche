package com.zh.xiche.entity;

/**
 * Created by dell on 2016/11/9.
 */

public class OrderDetailsEntity {
    /**
     * message : true
     * ordersDTO : {"appointment":"2016-11-05  16:00-18:00","appointmentId":1,"carbrank":"大众","carcolor":"棕色","carno":"鲁f567000","carstyle":"迈腾","lat":37.537548,"location":"芝罘区只楚凤良巷小区西(凤凰台二街西)","lon":121.328804,"mobile":"13953577193","monthIncome":0,"monthOrdersNum":0,"name":"杨","operid":0,"orderamount":1,"orderdate":"2016-11-04 17:12:52","orderid":"fb9e5f7042eb4c7289ebdf7b1bab5376","paydate":"2016-11-04 17:13:01","paystyle":3,"remark":"路边","servicetype":1,"servicetypename":"测试,","status":0,"uname":"","userid":4954}
     */

    private boolean message;
    private OrderEntity ordersDTO;

    public boolean isMessage() {
        return message;
    }

    public void setMessage(boolean message) {
        this.message = message;
    }

    public OrderEntity getOrdersDTO() {
        return ordersDTO;
    }

    public void setOrdersDTO(OrderEntity ordersDTO) {
        this.ordersDTO = ordersDTO;
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
