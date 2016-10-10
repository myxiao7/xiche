package com.zh.xiche.entity;

import com.zh.xiche.R;

/**
 * Created by zhanghao on 2016/10/10.
 */

public enum BaseEditEnum {
    perinfo_name(1,"name", R.string.per_name_title, R.string.per_name_title),
    perinfo_card(2,"cardno", R.string.per_card_title, R.string.per_card_title),
    perinfo_add(3,"location", R.string.per_add_title, R.string.per_add_title);

    private int id;
    private String paramName;//修改的参数名
    private int paramHint;//修改的提示
    private int title;//标题

    BaseEditEnum(int id, String paramName, int paramHint, int title) {
        this.id = id;
        this.paramName = paramName;
        this.paramHint = paramHint;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public int getParamHint() {
        return paramHint;
    }

    public void setParamHint(int paramHint) {
        this.paramHint = paramHint;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }

    /**
     * 根据ID获取当前枚举
     * @param id
     * @return
     */
    public static BaseEditEnum getTypebyId(int id){
        BaseEditEnum []values = values();
        for (int i = 0; i < values.length; i++) {
            if(values[i].id == id){
                return values[i];
            }
        }
        return perinfo_name;
    }
}
