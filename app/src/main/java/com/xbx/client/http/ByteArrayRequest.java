package com.xbx.client.http;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.xbx.client.app.App;
import com.xbx.client.utils.AESCrypt;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.Util;

/**
 * 小袁
 * Created by Administrator on 2015/3/11.
 */
class ByteArrayRequest extends Request<byte[]> {

    private final Listener<byte[]> mListener;

    private Object mPostBody = null;

    private HttpEntity httpEntity =null;

    public ByteArrayRequest(int method, String url, Object postBody, Listener<byte[]> listener, ErrorListener errorListener) {
        super(method, url, errorListener);
        this.mPostBody = postBody;
        this.mListener = listener;

        if (this.mPostBody != null && this.mPostBody instanceof RequestParams) {// contains file
            this.httpEntity = ((RequestParams) this.mPostBody).getEntity();
        }
    }

    /**
     * mPostBody is null or Map<String, String>, then execute this method
     */
    @SuppressWarnings("unchecked")
    protected Map<String, String> getParams() throws AuthFailureError {
        if (this.httpEntity == null && this.mPostBody != null && this.mPostBody instanceof Map<?, ?>) {
            return ((Map<String, String>) this.mPostBody);//common Map<String, String>
        }
        return null;//process as json, xml or MultipartRequestParams
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = super.getHeaders();
        if (null == headers || headers.equals(Collections.emptyMap())) {
            headers = new HashMap<String, String>();
            String deviceId = SharePrefer.getPhoneId(App.getContext());
            String uid = SharePrefer.getUserInfo(App.getContext()).getUid();
            AESCrypt aesCrypt = new AESCrypt(deviceId);
            headers.put("deviceid", deviceId);
            headers.put("uuid", aesCrypt.encrypt(uid));
//            Util.pLog("uuid:"+aesCrypt.encrypt(uid));
        }
        return headers;
    }

    @Override
    public String getBodyContentType() {
        if (httpEntity != null) {
            return httpEntity.getContentType().getValue();
        }
        return null;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        if (this.mPostBody != null && this.mPostBody instanceof String) {//process as json or xml
            String postString = (String) mPostBody;
            if (postString.length() != 0) {
                try {
                    return postString.getBytes("UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else {
                return null;
            }
        }
        if (this.httpEntity != null) {//process as MultipartRequestParams
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                httpEntity.writeTo(baos);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return baos.toByteArray();
        }
        return super.getBody();// mPostBody is null or Map<String, String>
    }

    @Override
    protected Response<byte[]> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response.data, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(byte[] response) {
        this.mListener.onResponse(response);
    }
}