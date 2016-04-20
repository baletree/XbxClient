package com.xbx.client.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.utils.Util;

/**
 * Created by EricYuan on 2016/4/20.
 */
public class LoadingDialog extends Dialog implements DialogInterface.OnKeyListener {
    private TextView vLoading_text;
    private ProgressBar find_loading_bar;
    private ProgressBar find_loading_bar2;

    private String msg = "";

    public LoadingDialog(Context context) {
        super(context, R.style.DialogStyleBottom);
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
        if (Integer.valueOf(android.os.Build.VERSION.SDK_INT) >= 23) {
            find_loading_bar2.setVisibility(View.VISIBLE);
            find_loading_bar.setVisibility(View.GONE);
        } else {
            find_loading_bar.setVisibility(View.GONE);
            find_loading_bar2.setVisibility(View.VISIBLE);
        }
        if (!Util.isNull(msg))
            vLoading_text.setText(msg);
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
}
