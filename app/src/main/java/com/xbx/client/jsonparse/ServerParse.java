package com.xbx.client.jsonparse;

import com.xbx.client.beans.ServerListBean;
import com.xbx.client.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/4/21.
 */
public class ServerParse {
    public static List<ServerListBean> getServerList(String json) {
        List<ServerListBean> sList = null;
        try {
            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() > 0) {
                sList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    ServerListBean sListBean = new ServerListBean();
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    if(UtilParse.checkTag(jsonObject,"uid"))
                        sListBean.setServerId(jsonObject.getString("uid"));
                    if(UtilParse.checkTag(jsonObject,"realname"))
                        sListBean.setServerName(jsonObject.getString("realname"));
                    if(UtilParse.checkTag(jsonObject,"head_image"))
                        sListBean.setServerHead(jsonObject.getString("head_image"));
                    if(UtilParse.checkTag(jsonObject,"guide_reserve_price"))
                        sListBean.setServerPrice(jsonObject.getString("guide_reserve_price"));
                    if(UtilParse.checkTag(jsonObject,"server_times"))
                        sListBean.setServerTimes(jsonObject.getString("server_times"));
                    if(UtilParse.checkTag(jsonObject,"stars"))
                        sListBean.setServerStar(jsonObject.getString("stars"));
                    sList.add(sListBean);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sList;
    }
}
