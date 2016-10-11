package com.zh.xiche.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 订单实体
 * Created by zhanghao on 2016/10/10.
 */

public class OrderEntity implements Parcelable {

    /**
     * acceptdate : 2016-09-21 00:51:15
     * appointment : 明天8:00-10:00
     * appointmentId : 0
     * assigndate :
     * avartar : images/avartar.png
     * carbrank : 奥迪
     * carcolor : 黑色
     * carno : 鲁MX2222
     * carstyle : A8
     * finishDate :
     * lat : 37.858
     * location : 山东省滨州市
     * lon : 114.394
     * mobile : 18790996688
     * monthIncome : 0
     * monthOrdersNum : 0
     * name : 王五
     * operator : 566666
     * operid : 142
     * opmobile : 18562653050
     * orderamount : 100
     * orderdate : 2016-09-20 17:05:00
     * orderid : 61c6b9f2569f4effaca3a0d4caf701a4
     * paydate : 2016-09-21 00:57:50
     * paystyle :
     * remark :
     * servicetype :
     * servicetypename : 汽车打蜡
     * status : 2
     * uname : 小旺旺
     * userid : 4943
     */

    private String acceptdate;
    private String appointment;
    private int appointmentId;
    private String assigndate;
    private String avartar;
    private String carbrank;
    private String carcolor;
    private String carno;
    private String carstyle;
    private String finishDate;
    private double lat;
    private String location;
    private double lon;
    private String mobile;
    private int monthIncome;
    private int monthOrdersNum;
    private String name;
    private String operator;
    private int operid;
    private String opmobile;
    private int orderamount;
    private String orderdate;
    private String orderid;
    private String paydate;
    private String paystyle;
    private String remark;
    private String servicetype;
    private String servicetypename;
    private int status;
    private String uname;
    private int userid;

    public String getAcceptdate() {
        return acceptdate;
    }

    public void setAcceptdate(String acceptdate) {
        this.acceptdate = acceptdate;
    }

    public String getAppointment() {
        return appointment;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getAssigndate() {
        return assigndate;
    }

    public void setAssigndate(String assigndate) {
        this.assigndate = assigndate;
    }

    public String getAvartar() {
        return avartar;
    }

    public void setAvartar(String avartar) {
        this.avartar = avartar;
    }

    public String getCarbrank() {
        return carbrank;
    }

    public void setCarbrank(String carbrank) {
        this.carbrank = carbrank;
    }

    public String getCarcolor() {
        return carcolor;
    }

    public void setCarcolor(String carcolor) {
        this.carcolor = carcolor;
    }

    public String getCarno() {
        return carno;
    }

    public void setCarno(String carno) {
        this.carno = carno;
    }

    public String getCarstyle() {
        return carstyle;
    }

    public void setCarstyle(String carstyle) {
        this.carstyle = carstyle;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getMonthIncome() {
        return monthIncome;
    }

    public void setMonthIncome(int monthIncome) {
        this.monthIncome = monthIncome;
    }

    public int getMonthOrdersNum() {
        return monthOrdersNum;
    }

    public void setMonthOrdersNum(int monthOrdersNum) {
        this.monthOrdersNum = monthOrdersNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public int getOperid() {
        return operid;
    }

    public void setOperid(int operid) {
        this.operid = operid;
    }

    public String getOpmobile() {
        return opmobile;
    }

    public void setOpmobile(String opmobile) {
        this.opmobile = opmobile;
    }

    public int getOrderamount() {
        return orderamount;
    }

    public void setOrderamount(int orderamount) {
        this.orderamount = orderamount;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getPaydate() {
        return paydate;
    }

    public void setPaydate(String paydate) {
        this.paydate = paydate;
    }

    public String getPaystyle() {
        return paystyle;
    }

    public void setPaystyle(String paystyle) {
        this.paystyle = paystyle;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getServicetype() {
        return servicetype;
    }

    public void setServicetype(String servicetype) {
        this.servicetype = servicetype;
    }

    public String getServicetypename() {
        return servicetypename;
    }

    public void setServicetypename(String servicetypename) {
        this.servicetypename = servicetypename;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.acceptdate);
        dest.writeString(this.appointment);
        dest.writeInt(this.appointmentId);
        dest.writeString(this.assigndate);
        dest.writeString(this.avartar);
        dest.writeString(this.carbrank);
        dest.writeString(this.carcolor);
        dest.writeString(this.carno);
        dest.writeString(this.carstyle);
        dest.writeString(this.finishDate);
        dest.writeDouble(this.lat);
        dest.writeString(this.location);
        dest.writeDouble(this.lon);
        dest.writeString(this.mobile);
        dest.writeInt(this.monthIncome);
        dest.writeInt(this.monthOrdersNum);
        dest.writeString(this.name);
        dest.writeString(this.operator);
        dest.writeInt(this.operid);
        dest.writeString(this.opmobile);
        dest.writeInt(this.orderamount);
        dest.writeString(this.orderdate);
        dest.writeString(this.orderid);
        dest.writeString(this.paydate);
        dest.writeString(this.paystyle);
        dest.writeString(this.remark);
        dest.writeString(this.servicetype);
        dest.writeString(this.servicetypename);
        dest.writeInt(this.status);
        dest.writeString(this.uname);
        dest.writeInt(this.userid);
    }

    public OrderEntity() {
    }

    protected OrderEntity(Parcel in) {
        this.acceptdate = in.readString();
        this.appointment = in.readString();
        this.appointmentId = in.readInt();
        this.assigndate = in.readString();
        this.avartar = in.readString();
        this.carbrank = in.readString();
        this.carcolor = in.readString();
        this.carno = in.readString();
        this.carstyle = in.readString();
        this.finishDate = in.readString();
        this.lat = in.readDouble();
        this.location = in.readString();
        this.lon = in.readDouble();
        this.mobile = in.readString();
        this.monthIncome = in.readInt();
        this.monthOrdersNum = in.readInt();
        this.name = in.readString();
        this.operator = in.readString();
        this.operid = in.readInt();
        this.opmobile = in.readString();
        this.orderamount = in.readInt();
        this.orderdate = in.readString();
        this.orderid = in.readString();
        this.paydate = in.readString();
        this.paystyle = in.readString();
        this.remark = in.readString();
        this.servicetype = in.readString();
        this.servicetypename = in.readString();
        this.status = in.readInt();
        this.uname = in.readString();
        this.userid = in.readInt();
    }

    public static final Parcelable.Creator<OrderEntity> CREATOR = new Parcelable.Creator<OrderEntity>() {
        @Override
        public OrderEntity createFromParcel(Parcel source) {
            return new OrderEntity(source);
        }

        @Override
        public OrderEntity[] newArray(int size) {
            return new OrderEntity[size];
        }
    };
}
