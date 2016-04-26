package com.xbx.client.beans;

/**
 * Created by EricYuan on 2016/4/24.
 * 用户进入首页后的状态
 */
public class UserStateBean {
    private String orderNum;
    private int guideType;

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public int getGuideType() {
        return guideType;
    }

    public void setGuideType(int guideType) {
        this.guideType = guideType;
    }
}
