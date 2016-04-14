package com.xbx.client.beans;

/**
 * Created by EricYuan on 2016/4/13.
 */
public class PayInfoBean {
    private String payName;// 名称
    private String payIntro;// 介绍
    private String payPrice;// 价格
    private String payOutTradeNum;// 商户外联

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getPayIntro() {
        return payIntro;
    }

    public void setPayIntro(String payIntro) {
        this.payIntro = payIntro;
    }

    public String getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(String payPrice) {
        this.payPrice = payPrice;
    }

    public String getPayOutTradeNum() {
        return payOutTradeNum;
    }

    public void setPayOutTradeNum(String payOutTradeNum) {
        this.payOutTradeNum = payOutTradeNum;
    }
}
