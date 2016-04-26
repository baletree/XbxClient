package com.xbx.client.jsonparse;

import android.content.Context;

import com.xbx.client.beans.UserInfo;
import com.xbx.client.beans.UserStateBean;
import com.xbx.client.utils.SharePrefer;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by EricYuan on 2016/4/25.
 * 主页状态解析
 */
public class MainStateParse {
    public static void resetToken(Context context,UserInfo userInfo, String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            if(UtilParse.checkTag(jsonObject,"login_token"))
                userInfo.setLoginToken(jsonObject.getString("login_token"));
            SharePrefer.saveUserInfo(context,userInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String checkDataType(String json) {
        String dataType = "";
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (UtilParse.checkTag(jsonObject, "user_unfinished_order")) {
                JSONObject job = jsonObject.getJSONObject("user_unfinished_order");
                if (UtilParse.checkTag(job, "data_type"))
                    dataType = job.getString("data_type");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataType;
    }

    public static UserStateBean checkUserState(String json, String key) {
        UserStateBean stateBean = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject job = jsonObject.getJSONObject("user_unfinished_order");
            if (UtilParse.checkTag(job, key)) {
                stateBean = new UserStateBean();
                JSONObject job2 = job.getJSONObject(key);
                if (UtilParse.checkTag(job2, "order_number"))
                    stateBean.setOrderNum(job2.getString("order_number"));
                if (UtilParse.checkTag(job2, "guide_type"))
                    stateBean.setGuideType(job2.getInt("guide_type"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return stateBean;
    }
}
