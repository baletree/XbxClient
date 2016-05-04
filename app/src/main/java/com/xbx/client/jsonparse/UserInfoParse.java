package com.xbx.client.jsonparse;

import com.xbx.client.beans.UserInfo;
import com.xbx.client.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by EricYuan on 2016/4/6.
 */
public class UserInfoParse {
    public static UserInfo getUserInfo(String json) {
        if (Util.isNull(json)) {
            return null;
        }
        UserInfo userInfo = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            userInfo = new UserInfo();
            if (UtilParse.checkTag(jsonObject, "uid")) {
                userInfo.setUid(jsonObject.getString("uid"));
            }
            if (UtilParse.checkTag(jsonObject, "login_token")) {
                userInfo.setLoginToken(jsonObject.getString("login_token"));
            }
            if (UtilParse.checkTag(jsonObject, "user_info")) {
                JSONObject job = jsonObject.getJSONObject("user_info");
//                Util.pLog("user_info:"+job.toString());
                if (UtilParse.checkTag(job, "mobile"))
                    userInfo.setUserPhone(job.getString("mobile"));
                if (UtilParse.checkTag(job, "nickname"))
                    userInfo.setNickName(job.getString("nickname"));
                if (UtilParse.checkTag(job, "head_image"))
                    userInfo.setUserHead(job.getString("head_image"));
                if (UtilParse.checkTag(job, "birthday"))
                    userInfo.setUserBirthday(job.getString("birthday"));
                if (UtilParse.checkTag(job, "sex"))
                    userInfo.setUserSex(job.getString("sex"));
                if (UtilParse.checkTag(job, "realname"))
                    userInfo.setUserRealName(job.getString("realname"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    public static UserInfo modifyUserInfo(UserInfo userInfo,String json){
        try {
            JSONObject job = new JSONObject(json);
            if (UtilParse.checkTag(job, "mobile"))
                userInfo.setUserPhone(job.getString("mobile"));
            if (UtilParse.checkTag(job, "nickname"))
                userInfo.setNickName(job.getString("nickname"));
            if (UtilParse.checkTag(job, "head_image"))
                userInfo.setUserHead(job.getString("head_image"));
            if (UtilParse.checkTag(job, "birthday"))
                userInfo.setUserBirthday(job.getString("birthday"));
            if (UtilParse.checkTag(job, "sex"))
                userInfo.setUserSex(job.getString("sex"));
            if (UtilParse.checkTag(job, "realname"))
                userInfo.setUserRealName(job.getString("realname"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return userInfo;
    }
}
