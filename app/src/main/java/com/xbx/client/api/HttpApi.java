package com.xbx.client.api;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.xbx.client.R;
import com.xbx.client.jsonparse.CommonParse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by EricYuan on 2016/3/25.
 */
public class HttpApi {
    protected RequestManager requestManager;
    protected Context context;
    protected String url;
    protected Handler mHandler;

    public HttpApi(Context context, Handler mHandler) {
        this.context = context;
        this.mHandler = mHandler;
        requestManager = RequestManager.getInstance(context);
    }

    /**
     * 登錄
     * @param phone
     * @param pwd
     */
    public void login(final String phone, final String pwd) {
        url = context.getString(R.string.url_http).concat(context.getString(R.string.url_login));
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        resultDataDeal(response,RequestConstant.REQUEST_SUC);
                    }

                }, errorListener) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("account",phone);
                map.put("password", pwd);
                return map;
            }
        };
        requestManager.addToRequestQueue(request);
    }

    private void resultDataDeal(String response,int successCode){
        Message msg = mHandler.obtainMessage();
        if(CommonParse.getRequest(response)){
            msg.obj = CommonParse.getDataResult(response);
            msg.what = successCode;
        }else {
            msg.obj = CommonParse.getRequestMsg(response);
            msg.what = RequestConstant.REQUEST_ERROR;
        }
        mHandler.sendMessage(msg);
    }

    Response.ErrorListener errorListener = new Response.ErrorListener() {

        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(context, "請檢查網絡", Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessage(RequestConstant.NO_NETWORK);
        }

    };
}
