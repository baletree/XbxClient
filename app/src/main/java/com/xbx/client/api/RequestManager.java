package com.xbx.client.api;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestManager {

	private static RequestManager instance;
	
	private RequestQueue requestQueue;
	
	private RequestManager(Context context) {
		requestQueue = Volley.newRequestQueue(context.getApplicationContext());
	}
	
	public static RequestManager getInstance(Context context) {
		if(instance == null) {
			synchronized (RequestManager.class) {
				if(instance == null) {
					instance = new RequestManager(context);
				}
			}
		}
		return instance;
	}
	
	public <T> void addToRequestQueue(Request<T> req) {
        requestQueue.add(req);  
    }  
	
	public RequestQueue getRequestQueue() {
		return requestQueue;
	}
}
