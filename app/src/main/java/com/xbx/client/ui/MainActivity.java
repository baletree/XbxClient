package com.xbx.client.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.xbx.client.R;
import com.xbx.client.api.HttpApi;
import com.xbx.client.api.RequestConstant;

public class MainActivity extends Activity {

    private HttpApi httpApi = null;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case RequestConstant.REQUEST_SUC:

                    break;
                case RequestConstant.REQUEST_ERROR:
                    
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        httpApi = new HttpApi(MainActivity.this,handler);
        httpApi.login("18602854129","1313113");
    }
}
