package com.zh.xiche.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by dell on 2016/11/7.
 */

public class PushEntity implements Parcelable {
    private int message_type; //1.审核结果 2.正常推送订单 3.管理员拍单 4.自定义消息
    private int audit_conclusion; //审核结果
    private String custom_message;//"这是一条自定义消息"
    private String order_id;
    private String service_type;
    private String order_Location;
    private String lat;
    private String lon;
    private String car_brank;
    private String car_style;
    private String appointment;
    private String order_amount;
    private String remark;

    public int getMessage_type() {
        return message_type;
    }

    public void setMessage_type(int message_type) {
        this.message_type = message_type;
    }

    public int getAudit_conclusion() {
        return audit_conclusion;
    }

    public void setAudit_conclusion(int audit_conclusion) {
        this.audit_conclusion = audit_conclusion;
    }

    public String getCustom_message() {
        return custom_message;
    }

    public void setCustom_message(String custom_message) {
        this.custom_message = custom_message;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getOrder_Location() {
        return order_Location;
    }

    public void setOrder_Location(String order_Location) {
        this.order_Location = order_Location;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getCar_brank() {
        return car_brank;
    }

    public void setCar_brank(String car_brank) {
        this.car_brank = car_brank;
    }

    public String getCar_style() {
        return car_style;
    }

    public void setCar_style(String car_style) {
        this.car_style = car_style;
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public String getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(String order_amount) {
        this.order_amount = order_amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.message_type);
        dest.writeInt(this.audit_conclusion);
        dest.writeString(this.custom_message);
        dest.writeString(this.order_id);
        dest.writeString(this.service_type);
        dest.writeString(this.order_Location);
        dest.writeString(this.lat);
        dest.writeString(this.lon);
        dest.writeString(this.car_brank);
        dest.writeString(this.car_style);
        dest.writeString(this.appointment);
        dest.writeString(this.order_amount);
        dest.writeString(this.remark);
    }

    public PushEntity() {
    }

    protected PushEntity(Parcel in) {
        this.message_type = in.readInt();
        this.audit_conclusion = in.readInt();
        this.custom_message = in.readString();
        this.order_id = in.readString();
        this.service_type = in.readString();
        this.order_Location = in.readString();
        this.lat = in.readString();
        this.lon = in.readString();
        this.car_brank = in.readString();
        this.car_style = in.readString();
        this.appointment = in.readString();
        this.order_amount = in.readString();
        this.remark = in.readString();
    }

    public static final Parcelable.Creator<PushEntity> CREATOR = new Parcelable.Creator<PushEntity>() {
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
