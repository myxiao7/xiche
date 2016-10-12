package com.zh.xiche.entity;

/**
 * 日类型账单
 * Created by zhanghao on 2016/10/12.
 */

public class BillDayEntity {
    /**
     * daydate : 2016-09-24
     * dayincome : 400
     */

    private String daydate;
    private String dayincome;

    public String getDaydate() {
        return daydate;
    }

    public void setDaydate(String daydate) {
        this.daydate = daydate;
    }

    public String getDayincome() {
        return dayincome;
    }

    public void setDayincome(String dayincome) {
        this.dayincome = dayincome;
    }
}
