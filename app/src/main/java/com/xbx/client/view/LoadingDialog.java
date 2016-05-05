package com.xbx.client.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.ui.activity.LoginActivity;
import com.xbx.client.utils.Util;

/**
 * Created by EricYuan on 2016/4/20.
 */
public class LoadingDialog extends Dialog implements DialogInterface.OnKeyListener {
    private TextView vLoading_text;
    private ProgressBar find_loading_bar;
    private ProgressBar find_loading_bar2;
    private ImageView dialog_loading_icon;
    private TextView dialog_count_tv;

    private Context context;
    private Handler pHandler;

    private String msg = "";

    private int count = 0;
    private int unCount = 50;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    setLoadingImg();
                    if (count == 12)
                        count = 0;
                    else
                        count++;
                    handler.sendEmptyMessageDelayed(1, 50);
                    break;
                case 2:
                    if (unCount == 0) {
                        pHandler.sendEmptyMessage(30);
                        handler.removeMessages(2);
                    } else {
                        dialog_count_tv.setText(unCount + context.getString(R.string.login_minute));
                        unCount--;
                    }
                    handler.sendEmptyMessageDelayed(2, 1000);
                    break;
            }
        }
    };

    public LoadingDialog(Context context) {
        super(context, R.style.DialogStyleBottom);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_guide_loading);
        initViews();
        setOnKeyListener(this);
    }

    private void initViews() {
        vLoading_text = (TextView) findViewById(R.id.find_loading_text);
        find_loading_bar = (ProgressBar) findViewById(R.id.find_loading_bar);
        find_loading_bar2 = (ProgressBar) findViewById(R.id.find_loading_bar2);
        dialog_loading_icon = (ImageView) findViewById(R.id.dialog_loading_icon);
        dialog_count_tv = (TextView) findViewById(R.id.dialog_count_tv);
        if (!Util.isNull(msg))
            vLoading_text.setText(msg);
    }

    @Override
    public void show() {
        super.show();
        count = 0;
        dialog_count_tv.setText("");
        handler.sendEmptyMessage(1);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        count = 0;
        handler.removeMessages(1);
        handler.removeMessages(2);
    }

    public void setCount(Handler pHandler) {
        this.pHandler = pHandler;
        unCount = 50;
        handler.sendEmptyMessage(2);
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }

    @Override
    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        }
        return false;
    }

    private void setLoadingImg() {
        if (dialog_loading_icon == null)
            return;
        switch (count) {
            case 0:
                dialog_loading_icon.setImageResource(R.drawable.loading1);
                break;
            case 1:
                dialog_loading_icon.setImageResource(R.drawable.loading2);
                break;
            case 2:
                dialog_loading_icon.setImageResource(R.drawable.loading3);
                break;
            case 3:
                dialog_loading_icon.setImageResource(R.drawable.loading4);
                break;
            case 4:
                dialog_loading_icon.setImageResource(R.drawable.loading5);
                break;
            case 5:
                dialog_loading_icon.setImageResource(R.drawable.loading6);
                break;
            case 6:
                dialog_loading_icon.setImageResource(R.drawable.loading7);
                break;
            case 7:
                dialog_loading_icon.setImageResource(R.drawable.loading8);
                break;
            case 8:
                dialog_loading_icon.setImageResource(R.drawable.loading9);
                break;
            case 9:
                dialog_loading_icon.setImageResource(R.drawable.loading10);
                break;
            case 10:
                dialog_loading_icon.setImageResource(R.drawable.loading11);
                break;
            case 11:
                dialog_loading_icon.setImageResource(R.drawable.loading12);
                break;
        }
    }
}
