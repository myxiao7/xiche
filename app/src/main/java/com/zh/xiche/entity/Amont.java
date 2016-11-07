package com.zh.xiche.entity;

/**
 * 今日统计
 * Created by dell on 2016/11/7.
 */

public class Amont {
    /**
     * message : true
     * operStatisticsDTO : {"ammount":"1.0","num":"1"}
     */
    private boolean message;
    /**
     * ammount : 1.0
     * num : 1
     */
    private data operStatisticsDTO;

    public boolean isMessage() {
        return message;
    }

    public void setMessage(boolean message) {
        this.message = message;
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
    public data getOperStatisticsDTO() {
        return operStatisticsDTO;
    }

    public void setOperStatisticsDTO(data operStatisticsDTO) {
        this.operStatisticsDTO = operStatisticsDTO;
    }

    public static class data {
        private String ammount;
        private String num;

        public String getAmmount() {
            return ammount;
        }

        public void setAmmount(String ammount) {
            this.ammount = ammount;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }
    }


}
