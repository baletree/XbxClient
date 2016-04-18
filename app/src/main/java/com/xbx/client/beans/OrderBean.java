package com.xbx.client.beans;

import java.io.Serializable;

/**
 * Created by EricYuan on 2016/4/6.
 */
public class OrderBean implements Serializable{
    private String orderNum;
    private String orderTime;
    private String orderType;
    private String orderState;
    private String orderAddress;
    private String GuideType;
    private String orderPay;

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getGuideType() {
        return GuideType;
    }

    public void setGuideType(String guideType) {
        GuideType = guideType;
    }

    public String getOrderPay() {
        return orderPay;
    }

    public void setOrderPay(String orderPay) {
        this.orderPay = orderPay;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }
}
