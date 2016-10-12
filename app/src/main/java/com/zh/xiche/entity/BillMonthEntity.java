package com.zh.xiche.entity;

/**
 * 月份类型账单
 * Created by zhanghao on 2016/10/12.
 */

public class BillMonthEntity {

    /**
     * monthdate : 2016-10
     * monthincome : 100
     */

    private String monthdate;
    private String monthincome;

    public String getMonthdate() {
        return monthdate;
    }

    public void setMonthdate(String monthdate) {
        this.monthdate = monthdate;
    }

    public String getMonthincome() {
        return monthincome;
    }

    public void setMonthincome(String monthincome) {
        this.monthincome = monthincome;
    }
}
