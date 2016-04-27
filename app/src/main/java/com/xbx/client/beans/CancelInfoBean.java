package com.xbx.client.beans;

import java.io.Serializable;

/**
 * Created by EricYuan on 2016/4/27.
 */
public class CancelInfoBean implements Serializable{
    private String cancelTime;
    private String cancelPay;
    private String orderNum;

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getCancelPay() {
        return cancelPay;
    }

    public void setCancelPay(String cancelPay) {
        this.cancelPay = cancelPay;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }
}
