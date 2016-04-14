package com.xbx.client.jsonparse;

import com.xbx.client.beans.GuideBean;
import com.xbx.client.beans.MyGuideInfoBean;
import com.xbx.client.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/4/7.
 */
public class GuideParse {
    public static int getDataType(String json) {
        int dataType = -1;
        try {
            JSONObject jObject = new JSONObject(json);
            if (UtilParse.checkTag(jObject, "data_type"))
                dataType = jObject.getInt("data_type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataType;
    }

    public static List<GuideBean> getGuideList(String json) {
        List<GuideBean> guideList = null;
        try {
            JSONObject jObject = new JSONObject(json);
            if (UtilParse.checkTag(jObject, "info_guide")) {
                JSONArray jArray = jObject.getJSONArray("info_guide");
                if (jArray.length() > 0) {
                    guideList = new ArrayList<>();
                    for (int i = 0; i < jArray.length(); i++) {
                        GuideBean guideBean = new GuideBean();
                        JSONObject jsonObject = (JSONObject) jArray.get(i);
                        if (UtilParse.checkTag(jsonObject, "uid"))
                            guideBean.setGuideId(jsonObject.getString("uid"));
                        if (UtilParse.checkTag(jsonObject, "lon"))
                            guideBean.setLongitude(jsonObject.getDouble("lon"));
                        if (UtilParse.checkTag(jsonObject, "lat"))
                            guideBean.setLatitude(jsonObject.getDouble("lat"));
                        guideList.add(guideBean);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return guideList;
    }

    public static String[] getChoiceNum(String json) {
        String[] choicNum = null;
        try {
            JSONObject jObject = new JSONObject(json);
            if (UtilParse.checkTag(jObject, "conf")) {
                JSONArray jsonArray = jObject.getJSONArray("conf");
                Util.pLog("getChoicenum:" + jsonArray.toString());
                if(jsonArray != null && jsonArray.length() > 0){
                    choicNum = new String[jsonArray.length()];
                    for(int i = 0;i<jsonArray.length();i++){
                        String key = (String) jsonArray.get(i);
//                        Util.pLog("getChoicenumKey:" + key);
                        choicNum[i] = key;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return choicNum;
    }

    public static String getImmdiaOrder(String json){
        String orderNum = "";
        try {
            JSONObject jObject = new JSONObject(json);
            if (UtilParse.checkTag(jObject, "order_number")) {
                orderNum = jObject.getString("order_number");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderNum;
    }
    public static MyGuideInfoBean parseMyGuide(String json){
        MyGuideInfoBean guideInfoBean = new MyGuideInfoBean();
        try {
            JSONObject jObject = new JSONObject(json);
            if(UtilParse.checkTag(jObject,"mobile"))
                guideInfoBean.setGuidePhone(jObject.getString("mobile"));
            if(UtilParse.checkTag(jObject,"head_image"))
                guideInfoBean.setGuideHeadImg(jObject.getString("head_image"));
            if(UtilParse.checkTag(jObject,"realname"))
                guideInfoBean.setGuideName(jObject.getString("realname"));
            if(UtilParse.checkTag(jObject,"lon"))
                guideInfoBean.setGuideLon(jObject.getDouble("lon"));
            if(UtilParse.checkTag(jObject,"lat"))
                guideInfoBean.setGuideLat(jObject.getDouble("lat"));
            if(UtilParse.checkTag(jObject,"guide_number"))
                guideInfoBean.setGuideNum(jObject.getString("guide_number"));
            if(UtilParse.checkTag(jObject,"stars"))
                guideInfoBean.setGuideStarts(jObject.getString("stars"));
            if(UtilParse.checkTag(jObject,"server_start_time"))
                guideInfoBean.setStartTime(jObject.getLong("server_start_time"));
            if(UtilParse.checkTag(jObject,"now_time"))
                guideInfoBean.setCurrentTime(jObject.getLong("now_time"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return guideInfoBean;
    }
}
