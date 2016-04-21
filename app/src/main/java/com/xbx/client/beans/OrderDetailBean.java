package com.xbx.client.beans;

/**
 * Created by EricYuan on 2016/4/18.
 */
public class OrderDetailBean {
    private String guideName;
    private String headImg;
    private String guideType; //导游类型
    private String guideNumber;
    private String guidePhone;
    private String orderNum;
    private String orderState; //订单状态
    private String orderPayState;
    private String orderOrignalPay;//原始价格
    private String rebateMoney;// 优惠价格
    private String rewardMoney;//打赏
    private String orderPay;//实际支付
    private String orderPayType;
    private String orderStartTime;
    private String orderEndtTime;
    private String guideTag;
    private String guideCotent;
    private String guideStart;
    private String serverType;

    public String getServerType() {
        return serverType;
    }

    public void setServerType(String serverType) {
        this.serverType = serverType;
    }

    public String getGuideTag() {
        return guideTag;
    }

    public void setGuideTag(String guideTag) {
        this.guideTag = guideTag;
    }

    public String getGuideCotent() {
        return guideCotent;
    }

    public void setGuideCotent(String guideCotent) {
        this.guideCotent = guideCotent;
    }

    public String getGuideStart() {
        return guideStart;
    }

    public void setGuideStart(String guideStart) {
        this.guideStart = guideStart;
    }

    public String getGuideName() {
        return guideName;
    }

    public void setGuideName(String guideName) {
        this.guideName = guideName;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getGuideType() {
        return guideType;
    }

    public void setGuideType(String guideType) {
        this.guideType = guideType;
    }

    public String getGuideNumber() {
        return guideNumber;
    }

    public void setGuideNumber(String guideNumber) {
        this.guideNumber = guideNumber;
    }

    public String getGuidePhone() {
        return guidePhone;
    }

    public void setGuidePhone(String guidePhone) {
        this.guidePhone = guidePhone;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getOrderState() {
        return orderState;
    }

    public void setOrderState(String orderState) {
        this.orderState = orderState;
    }

    public String getOrderPayState() {
        return orderPayState;
    }

    public void setOrderPayState(String orderPayState) {
        this.orderPayState = orderPayState;
    }

    public String getOrderOrignalPay() {
        return orderOrignalPay;
    }

    public void setOrderOrignalPay(String orderOrignalPay) {
        this.orderOrignalPay = orderOrignalPay;
    }

    public String getRebateMoney() {
        return rebateMoney;
    }

    public void setRebateMoney(String rebateMoney) {
        this.rebateMoney = rebateMoney;
    }

    public String getRewardMoney() {
        return rewardMoney;
    }

    public void setRewardMoney(String rewardMoney) {
        this.rewardMoney = rewardMoney;
    }

    public String getOrderPay() {
        return orderPay;
    }

    public void setOrderPay(String orderPay) {
        this.orderPay = orderPay;
    }

    public String getOrderPayType() {
        return orderPayType;
    }

    public void setOrderPayType(String orderPayType) {
        this.orderPayType = orderPayType;
    }

    public String getOrderStartTime() {
        return orderStartTime;
    }

    public void setOrderStartTime(String orderStartTime) {
        this.orderStartTime = orderStartTime;
    }

    public String getOrderEndtTime() {
        return orderEndtTime;
    }

    public void setOrderEndtTime(String orderEndtTime) {
        this.orderEndtTime = orderEndtTime;
    }
}
