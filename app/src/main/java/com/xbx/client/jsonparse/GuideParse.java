package com.xbx.client.jsonparse;

import com.xbx.client.beans.GuideBean;
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
    public static int getDataType(String json){
        int dataType = -1;
        try {
            JSONObject jObject = new JSONObject(json);
            if(UtilParse.checkTag(jObject, "data_type"))
                dataType = jObject.getInt("data_type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataType;
    }

    public static List<GuideBean> getGuideList(String json){
        List<GuideBean> guideList = null;
        try {
            JSONObject jObject = new JSONObject(json);
            if(UtilParse.checkTag(jObject, "info_guide")){
                JSONArray jArray = jObject.getJSONArray("info_guide");
                if(jArray.length() > 0){
                    guideList =  new ArrayList<>();
                    for(int i = 0;i<jArray.length();i++){
                        GuideBean guideBean = new GuideBean();
                        JSONObject jsonObject = (JSONObject) jArray.get(i);
                        if(UtilParse.checkTag(jsonObject,"uid"))
                            guideBean.setGuideId(jsonObject.getString("uid"));
                        if(UtilParse.checkTag(jsonObject,"lon"))
                            guideBean.setLongitude(jsonObject.getDouble("lon"));
                        if(UtilParse.checkTag(jsonObject,"lat"))
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
}
