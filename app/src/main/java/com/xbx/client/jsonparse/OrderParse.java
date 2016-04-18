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
                    if(UtilParse.checkTag(jObject,"server_status"))
                        orderBean.setOrderState(jObject.getString("server_status"));
                    if(UtilParse.checkTag(jObject,"order_time"))
                        orderBean.setOrderTime(jObject.getString("order_time"));
                    if(UtilParse.checkTag(jObject,"end_addr"))
                        orderBean.setOrderAddress(jObject.getString("end_addr"));
                    if(UtilParse.checkTag(jObject,"server_type"))
                        orderBean.setOrderType(jObject.getString("server_type"));
                    if(UtilParse.checkTag(jObject,"user_type"))
                        orderBean.setGuideType(jObject.getString("user_type"));
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return oDetailBean;
    }
}
