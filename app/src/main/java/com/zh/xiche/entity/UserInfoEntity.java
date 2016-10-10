package com.zh.xiche.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * 用户信息
 * Created by zhanghao on 2016/9/30.
 */
@Table(name = "UserInfoEntity")
public class UserInfoEntity {
    /**
     * cardno : null
     * avatar :
     * id : 139
     * ispass : 2
     * joinustime : 2016-10-09 23:19:33
     * lat : 0
     * location : null
     * lon : 0
     * mobile : 15266788746
     * monthIncome : 0
     * monthOrdersNum : 0
     * name :
     * password : 12345
     * tockens : 0abdbd326ca04c3495f2eb2f424d6b2b
     */
    @Column(name="ids", isId = true)
    private int ids;
    @Column(name="cardno")
    private String cardno;
    @Column(name="avatar")
    private String avatar;
    @Column(name="id")
    private String id;
    @Column(name="ispass")
    private int ispass;
    @Column(name="joinustime")
    private String joinustime;
    @Column(name="lat")
    private String lat;
    @Column(name="location")
    private String location;
    @Column(name="lon")
    private String lon;
    @Column(name="mobile")
    private String mobile;
    @Column(name="monthIncome")
    private String monthIncome;
    @Column(name="monthOrdersNum")
    private int monthOrdersNum;
    @Column(name="name")
    private String name;
    @Column(name="password")
    private String password;
    @Column(name="tockens")
    private String tockens;

    public int getIds() {
        return ids;
    }

    public void setIds(int ids) {
        this.ids = ids;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIspass() {
        return ispass;
    }

    public void setIspass(int ispass) {
        this.ispass = ispass;
    }

    public String getJoinustime() {
        return joinustime;
    }

    public void setJoinustime(String joinustime) {
        this.joinustime = joinustime;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMonthIncome() {
        return monthIncome;
    }

    public void setMonthIncome(String monthIncome) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTockens() {
        return tockens;
    }

    public void setTockens(String tockens) {
        this.tockens = tockens;
    }
}
