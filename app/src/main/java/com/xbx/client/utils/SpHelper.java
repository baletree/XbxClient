package com.xbx.client.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by EricYuan on 2016/3/30.
 */
public class SpHelper {

    private Context context;
    private String spName;

    public SpHelper(Context context,String spName){
        this.context = context;
        this.spName = spName;
    }

    public void setSP(String sKey, String sValue) {
        SharedPreferences s = context.getSharedPreferences(spName, 0);
        SharedPreferences.Editor editor = s.edit();
        editor.putString(sKey, sValue);
        editor.commit();
    }

    public void setSP(String sKey, boolean sValue) {
        SharedPreferences s = context.getSharedPreferences(spName, 0);
        SharedPreferences.Editor editor = s.edit();
        editor.putBoolean(sKey, sValue);
        editor.commit();
    }

    public String getSP(String sKey) {
        String str;
        SharedPreferences s = context.getSharedPreferences(spName, 0);
        str = s.getString(sKey, "");
        return str;
    }

    public boolean getSPBoolean(String key){
        SharedPreferences s = context.getSharedPreferences(spName, 0);
        return s.getBoolean(key, false);
    }
}
