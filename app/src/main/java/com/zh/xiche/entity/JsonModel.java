package com.zh.xiche.entity;

import java.util.List;

/**
 * 基础Json数据模型
 * Created by zhanghao on 2016/10/12.
 */

public class JsonModel<T> {
    private boolean message;
    private String page;
    private String record;
    private String rows;
    private String total;
    private T dataList;

    public boolean isMessage() {
        return message;
    }

    public void setMessage(boolean message) {
        this.message = message;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getRows() {
        return rows;
    }

    public void setRows(String rows) {
        this.rows = rows;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public T getDataList() {
        return dataList;
    }

    public void setDataList(T dataList) {
        this.dataList = dataList;
    }

    /**
     * 请求是否成功
     * @return
     */
    public boolean isSuccess(){
        if(message){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 是否有数据
     * @return
     */
    public boolean hasData(){
        if(dataList == null){
            return false;
        }
        if(dataList instanceof List && ((List) dataList).size() <= 0){
            return false;
        }
        return true;
    }
}
