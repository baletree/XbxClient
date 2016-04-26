package com.xbx.client.jsonparse;

import com.xbx.client.beans.OrderBean;
import com.xbx.client.beans.OrderDetailBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/4/18.
 */
public class OrderParse {
    public static List<OrderBean> orderListParse(String json){
        List<OrderBean> orderList = null;
        try {
            JSONArray jArray = new JSONArray(json);
            if(jArray.length() > 0){
                orderList = new ArrayList<>();
                for(int i = 0;i<jArray.length();i++){
                    OrderBean orderBean = new OrderBean();
                    JSONObject jObject = (JSONObject) jArray.get(i);
                    if(UtilParse.checkTag(jObject,"order_number"))
                        orderBean.setOrderNum(jObject.getString("order_number"));
                    if(UtilParse.checkTag(jObject,"pay_money"))
                        orderBean.setOrderPay(jObject.getString("pay_money"));
                    if(UtilParse.checkTag(jObject,"order_time"))
                        orderBean.setOrderTime(jObject.getString("order_time"));
                    if(UtilParse.checkTag(jObject,"end_addr"))
                        orderBean.setOrderAddress(jObject.getString("end_addr"));
                    if(UtilParse.checkTag(jObject,"server_type"))
                        orderBean.setOrderType(jObject.getInt("server_type"));
                    if(UtilParse.checkTag(jObject,"user_type"))
                        orderBean.setGuideType(jObject.getInt("user_type"));
                    if(UtilParse.checkTag(jObject,"order_status"))
                        orderBean.setOrderState(jObject.getInt("order_status"));
                    orderList.add(orderBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderList;
    }

    public static OrderDetailBean getDetailOrder(String json){
        OrderDetailBean oDetailBean = new OrderDetailBean();
        try {
            JSONObject jObject = new JSONObject(json);
            if(UtilParse.checkTag(jObject,"realname"))
                oDetailBean.setGuideName(jObject.getString("realname"));
            if(UtilParse.checkTag(jObject,"head_image"))
                oDetailBean.setHeadImg(jObject.getString("head_image"));
            if(UtilParse.checkTag(jObject,"user_type"))
                oDetailBean.setGuideType(jObject.getInt("user_type"));
            if(UtilParse.checkTag(jObject,"guide_number"))
                oDetailBean.setGuideNumber(jObject.getString("guide_number"));
            if(UtilParse.checkTag(jObject,"mobile"))
                oDetailBean.setGuidePhone(jObject.getString("mobile"));
            if(UtilParse.checkTag(jObject,"order_number"))
                oDetailBean.setOrderNum(jObject.getString("order_number"));
            if(UtilParse.checkTag(jObject,"order_status"))
                oDetailBean.setOrderState(jObject.getInt("order_status"));
            if(UtilParse.checkTag(jObject,"pay_status"))
                oDetailBean.setOrderPayState(jObject.getString("pay_status"));
            if(UtilParse.checkTag(jObject,"order_money"))
                oDetailBean.setOrderOrignalPay(jObject.getDouble("order_money"));
            if(UtilParse.checkTag(jObject,"rebate_money"))
                oDetailBean.setRebateMoney(jObject.getDouble("rebate_money"));
            if(UtilParse.checkTag(jObject,"tip_money"))
                oDetailBean.setRewardMoney(jObject.getDouble("tip_money"));
            if(UtilParse.checkTag(jObject,"pay_money"))
                oDetailBean.setOrderPay(jObject.getDouble("pay_money"));
            if(UtilParse.checkTag(jObject,"pay_type"))
                oDetailBean.setOrderPayType(jObject.getInt("pay_type"));
            if(UtilParse.checkTag(jObject,"server_start_time"))
                oDetailBean.setOrderStartTime(jObject.getString("server_start_time"));
            if(UtilParse.checkTag(jObject,"server_end_time"))
                oDetailBean.setOrderEndtTime(jObject.getString("server_end_time"));
            if(UtilParse.checkTag(jObject,"server_type"))
                oDetailBean.setServerType(jObject.getInt("server_type"));
            if(UtilParse.checkTag(jObject,"content"))
                oDetailBean.setGuideCotent(jObject.getString("content"));
            if(UtilParse.checkTag(jObject,"tag"))
                oDetailBean.setGuideTag(jObject.getString("tag"));
            if(UtilParse.checkTag(jObject,"server_date"))
                oDetailBean.setServerDate(jObject.getString("server_date"));
            if(UtilParse.checkTag(jObject,"cancel_time"))
                oDetailBean.setOrderCancelTime(jObject.getString("cancel_time"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return oDetailBean;
    }
}
