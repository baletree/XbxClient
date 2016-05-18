package com.xbx.client.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.utils.Util;

/**
 * Created by EricYuan on 2016/5/5.
 */
public class LoadDialog extends Dialog implements DialogInterface.OnKeyListener{
    private Context context;
    private Handler pHandler;

    private TextView vLoading_text;
    private int unCount = 50;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 2:
                    if (unCount == 0) {
                        pHandler.sendEmptyMessage(30);
                        handler.removeMessages(2);
                    } else {
                        vLoading_text.setText(unCount + context.getString(R.string.login_minute));
                        unCount--;
                    }
                    handler.sendEmptyMessageDelayed(2, 1000);
                    break;
            }
        }
    };

    public LoadDialog(Context context){
        super(context, R.style.LoadDialogStyle);
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_guide_loading);
        initViews();
    }

    private void initViews() {
        vLoading_text = (TextView) findViewById(R.id.vLoading_text);
        setOnKeyListener(this);
    }

    public void setCount(Handler pHandler) {
        this.pHandler = pHandler;
        unCount = 50;
        handler.sendEmptyMessage(2);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        unCount = 50;
        handler.removeMessages(2);
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        }
        return false;
    }
}
