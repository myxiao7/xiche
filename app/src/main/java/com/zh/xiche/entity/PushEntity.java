package com.zh.xiche.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 2016/11/7.
 */

public class PushEntity implements Parcelable {

    /**
     * remark : 1
     * appointment : 2016-11-11  16:00-18:00
     * car_brand : 大众
     * order_id : dfc372d6b2174e8bacadaa30218417b2
     * lon : 36.099506
     * order_amount : 1.0
     * lat : 120.374321
     * car_style : 迈腾
     * custom_message : 你有新的订单信息，请注意查收！
     * service_type : 测试,
     * message_type : 2
     * audit_conclusion :
     */

    private String remark;
    private String appointment;
    private String car_brand;
    private String order_id;
    private String lon;
    private String order_Location;
    private String order_amount;
    private String lat;
    private String car_style;
    private String custom_message;
    private String service_type;
    private String message_type;
    private String audit_conclusion;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public String getCar_brand() {
        return car_brand;
    }

    public void setCar_brand(String car_brand) {
        this.car_brand = car_brand;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getOrder_Location() {
        return order_Location;
    }

    public void setOrder_Location(String order_Location) {
        this.order_Location = order_Location;
    }

    public String getCar_style() {
        return car_style;
    }

    public void setCar_style(String car_style) {
        this.car_style = car_style;
    }

    public String getCustom_message() {
        return custom_message;
    }

    public void setCustom_message(String custom_message) {
        this.custom_message = custom_message;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getAudit_conclusion() {
        return audit_conclusion;
    }

    public void setAudit_conclusion(String audit_conclusion) {
        this.audit_conclusion = audit_conclusion;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.remark);
        dest.writeString(this.appointment);
        dest.writeString(this.car_brand);
        dest.writeString(this.order_id);
        dest.writeString(this.lon);
        dest.writeString(this.order_Location);
        dest.writeString(this.order_amount);
        dest.writeString(this.lat);
        dest.writeString(this.car_style);
        dest.writeString(this.custom_message);
        dest.writeString(this.service_type);
        dest.writeString(this.message_type);
        dest.writeString(this.audit_conclusion);
    }

    public PushEntity() {
    }

    protected PushEntity(Parcel in) {
        this.remark = in.readString();
        this.appointment = in.readString();
        this.car_brand = in.readString();
        this.order_id = in.readString();
        this.lon = in.readString();
        this.order_Location = in.readString();
        this.order_amount = in.readString();
        this.lat = in.readString();
        this.car_style = in.readString();
        this.custom_message = in.readString();
        this.service_type = in.readString();
        this.message_type = in.readString();
        this.audit_conclusion = in.readString();
    }

    public static final Creator<PushEntity> CREATOR = new Creator<PushEntity>() {
        @Override
        public PushEntity createFromParcel(Parcel source) {
            return new PushEntity(source);
        }

        @Override
        public PushEntity[] newArray(int size) {
            return new PushEntity[size];
        }
    };
}
