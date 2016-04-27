package com.xbx.client.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.utils.Util;

/**
 * Created by EricYuan on 2016/4/6.
 */
public class TipsDialog extends Dialog {
    private TextView dialog_title_tv;
    private TextView dialog_msg_tv;
    private TextView cancel_tv;
    private TextView sure_tv;
    private ImageView reservat_reward_img;

    private String title = "";
    private String msg = "";
    private String cancel = "";
    private String submit = "";
    private boolean isShowReward = false;

    private DialogClickListener clickListener;

    public TipsDialog(Context context) {
        super(context, R.style.DialogStyleBottom);
    }

    public void setClickListener(DialogClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tips);
        setCanceledOnTouchOutside(false);
        initViews();
    }

    private void initViews() {
        dialog_title_tv = (TextView) findViewById(R.id.dialog_title_tv);
        dialog_msg_tv = (TextView) findViewById(R.id.dialog_msg_tv);
        cancel_tv = (TextView) findViewById(R.id.cancel_tv);
        sure_tv = (TextView) findViewById(R.id.sure_tv);
        reservat_reward_img = (ImageView) findViewById(R.id.reservat_reward_img);
        cancel_tv.setOnClickListener(listener);
        sure_tv.setOnClickListener(listener);
        if (!Util.isNull(title))
            dialog_title_tv.setText(title);
        if (!Util.isNull(msg))
            dialog_msg_tv.setText(msg);
        if (!Util.isNull(cancel))
            cancel_tv.setText(cancel);
        if (!Util.isNull(submit))
            sure_tv.setText(submit);
        if(isShowReward)
            reservat_reward_img.setVisibility(View.VISIBLE);
    }

    public void setInfo(String title, String msg) {
        this.title = title;
        this.msg = msg;
    }

    public void setBtnTxt(String cancel, String submit) {
        this.cancel = cancel;
        this.submit = submit;
    }

    public void isSHowRewardImg(boolean isShow){
        this.isShowReward = isShow;
    }

    private View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                switch (v.getId()) {
                    case R.id.cancel_tv:
                        clickListener.cancelDialog();
                        break;
                    case R.id.sure_tv:
                        clickListener.confirmDialog();
                        break;
                }
            }
        }
    };

    public interface DialogClickListener {
        public void cancelDialog();
        public void confirmDialog();
    }
}
