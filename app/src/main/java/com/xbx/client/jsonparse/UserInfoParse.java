package com.xbx.client.jsonparse;

import com.xbx.client.beans.UserInfo;
import com.xbx.client.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by EricYuan on 2016/4/6.
 */
public class UserInfoParse {
    public static UserInfo getUserInfo(String json){
        if(Util.isNull(json)){
            return null;
        }
        UserInfo userInfo = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            userInfo = new UserInfo();
            if(CommonParse.checkTag(jsonObject,"uid")){
                userInfo.setUid(jsonObject.getString("uid"));
            }
            if(CommonParse.checkTag(jsonObject,"login_token")){
                userInfo.setLoginToken(jsonObject.getString("login_token"));
            }
            if(CommonParse.checkTag(jsonObject,"user_info")){
                JSONObject job = jsonObject.getJSONObject("user_info");
                if(CommonParse.checkTag(job,"mobile")){
                    userInfo.setUserPhone(job.getString("mobile"));
                }
                if(CommonParse.checkTag(job,"nickname")){
                    userInfo.setNickName(job.getString("nickname"));
                }
                if(CommonParse.checkTag(job,"head_image")){
                    userInfo.setUserHead(job.getString("head_image"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userInfo;
    }
}
