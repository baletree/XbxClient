package com.xbx.client.beans;

import java.io.Serializable;

/**
 * Created by EricYuan on 2016/4/18.
 */
public class OrderDetailBean implements Serializable{
    private String guideName;
    private String headImg;
    private String guideNumber;
    private String guidePhone;
    private String orderNum;
    private String orderPayState;
    private String orderStartTime;
    private String orderEndtTime;
    private String guideTag;
    private String guideCotent;
    private String guideStart;
    private String userAddress;
    private String serverDate;
    private double orderOrignalPay;//原始价格
    private double rebateMoney;// 优惠价格
    private double rewardMoney;//打赏
    private double orderPay;//实际支付
    private int guideType; //导游类型
    private int orderState; //订单状态
    private int serverType;//服务类型
    private int orderPayType;
    private String orderCancelTime;

    public String getOrderCancelTime() {
        return orderCancelTime;
    }

    public void setOrderCancelTime(String orderCancelTime) {
        this.orderCancelTime = orderCancelTime;
    }

    public String getServerDate() {
        return serverDate;
    }

    public void setServerDate(String serverDate) {
        this.serverDate = serverDate;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
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

    public String getOrderPayState() {
        return orderPayState;
    }

    public void setOrderPayState(String orderPayState) {
        this.orderPayState = orderPayState;
    }

    public double getOrderOrignalPay() {
        return orderOrignalPay;
    }

    public void setOrderOrignalPay(double orderOrignalPay) {
        this.orderOrignalPay = orderOrignalPay;
    }

    public double getRebateMoney() {
        return rebateMoney;
    }

    public void setRebateMoney(double rebateMoney) {
        this.rebateMoney = rebateMoney;
    }

    public double getRewardMoney() {
        return rewardMoney;
    }

    public void setRewardMoney(double rewardMoney) {
        this.rewardMoney = rewardMoney;
    }

    public double getOrderPay() {
        return orderPay;
    }

    public void setOrderPay(double orderPay) {
        this.orderPay = orderPay;
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

    public int getGuideType() {
        return guideType;
    }

    public void setGuideType(int guideType) {
        this.guideType = guideType;
    }

    public int getOrderState() {
        return orderState;
    }

    public void setOrderState(int orderState) {
        this.orderState = orderState;
    }

    public int getOrderPayType() {
        return orderPayType;
    }

    public void setOrderPayType(int orderPayType) {
        this.orderPayType = orderPayType;
    }

    public int getServerType() {
        return serverType;
    }

    public void setServerType(int serverType) {
        this.serverType = serverType;
    }
}
