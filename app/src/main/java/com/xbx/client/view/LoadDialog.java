package com.xbx.client.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.utils.Util;

/**
 * Created by EricYuan on 2016/5/5.
 */
public class LoadDialog extends Dialog {
    private Context context;
    private String msg = "";

    private TextView vLoading_text;
    private ImageView dialog_loading_icon;

    public LoadDialog(Context context){
        super(context, R.style.DialogStyleBottom);
        this.context = context;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_guide_loading);
        initViews();
    }

    private void initViews() {
        vLoading_text = (TextView) findViewById(R.id.find_loading_text);
        dialog_loading_icon = (ImageView) findViewById(R.id.dialog_loading_icon);
        if (!Util.isNull(msg))
            vLoading_text.setText(msg);
    }

    public void setMessage(String msg) {
        this.msg = msg;
    }
}
