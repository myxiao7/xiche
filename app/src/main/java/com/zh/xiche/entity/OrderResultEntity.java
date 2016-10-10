package com.zh.xiche.entity;

import java.util.List;

/**
 * 订单结果实体
 * Created by zhanghao on 2016/10/10.
 */

public class OrderResultEntity {
    /**
     * dataList : [{"acceptdate":"2016-09-20 21:49:19","appointment":"明天8:00-10:00","appointmentId":0,"assigndate":null,"carbrank":"宝马","carcolor":"灰色","carno":"鲁MX3333","carstyle":"X7","finishDate":null,"lat":37.854,"location":"山东省滨州市","lon":114.392,"mobile":"18800006666","monthIncome":0,"monthOrdersNum":0,"name":"李四","operator":"胡技师","operid":114,"opmobile":"18809125687","orderamount":50,"orderdate":"2016-09-21 17:01:00","orderid":"89182fbeace248f99352cd3bcaebdeda","paydate":null,"paystyle":null,"remark":"需要洗车","servicetype":null,"servicetypename":"汽车精细","status":null,"uname":"小李子","userid":4942},{"acceptdate":"2016-09-20 23:43:38","appointment":"明天8:00-10:00","appointmentId":0,"assigndate":null,"carbrank":"宝马","carcolor":"灰色","carno":"鲁MX1111","carstyle":"X6","finishDate":null,"lat":37.854,"location":"山东省滨州市","lon":114.392,"mobile":"18800006666","name":"李四","operator":"胡技师","operid":114,"opmobile":"18809125687","orderamount":50,"orderdate":"2016-09-20 17:01:00","orderid":"7f1c6fae59984c658a256be39f63cf52","paydate":null,"remark":"需要洗车","servicetype":null,"servicetypename":"汽车精细","uname":"小李子","userid":4942}]
     * page : 1
     * record : 3
     * rows : 10
     * total : 1
     * message=True
     */
    private boolean message;
    private String page;
    private String record;
    private String rows;
    private String total;
    private List<OrderEntity> dataList;

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

    public List<OrderEntity> getDataList() {
        return dataList;
    }

    public void setDataList(List<OrderEntity> dataList) {
        this.dataList = dataList;
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
