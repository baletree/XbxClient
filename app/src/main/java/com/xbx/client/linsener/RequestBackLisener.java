package com.xbx.client.linsener;

import android.content.Context;

import com.android.volley.VolleyError;
import com.xbx.client.http.RequestListener;
import com.xbx.client.utils.Util;

/**
 * Created by EricYuan on 2016/4/5.
 */
public class RequestBackLisener implements RequestListener {

    private Context context;

    public RequestBackLisener(Context context){
        this.context = context;
    }
    @Override
    public void requestSuccess(String json) {

    }

    @Override
    public void requestError(VolleyError e) {
        Util.checkNetError(context, e);
    }
}
