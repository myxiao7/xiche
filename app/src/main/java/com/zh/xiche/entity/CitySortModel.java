package com.zh.xiche.entity;

/**
 * 省市检索
 * Created by zhanghao on 2016/8/30.
 */
public class CitySortModel {

    private int rid;
    private String region; //省市名称
    private String sortLetters; //首字母

    public CitySortModel() {
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSortLetters() {
        return sortLetters;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
