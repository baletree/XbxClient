package com.xbx.client.utils.updateversion;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.beans.Version;

/**
 * Created by Eric
 * Created Date 2015/9/24 11:14
 * Describe:版本更新提示对话框
 */
public class UpdateVersionDialog extends Dialog {
    /**
     * 对话框的内容
     */
    private Version version;
    private CustomClickListener clickListener;
    private TextView new_versioncode_tv;
    private TextView new_versionmsg_tv;
    private TextView update_cancel_tv;
    private TextView update_submit_tv;

    public UpdateVersionDialog(Context context, Version version) {
        super(context, R.style.DialogStyleBottom);
        this.version = version;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.version_dialog);
        new_versioncode_tv = (TextView) findViewById(R.id.new_versioncode_tv);
        new_versionmsg_tv = (TextView) findViewById(R.id.new_versionmsg_tv);
        update_cancel_tv = (TextView) findViewById(R.id.update_cancel_tv);
        update_submit_tv = (TextView) findViewById(R.id.update_submit_tv);
        update_cancel_tv.setOnClickListener(listener);
        update_submit_tv.setOnClickListener(listener);
        new_versioncode_tv.setText(version.getVersionName());
        new_versionmsg_tv.setText(Html.fromHtml(version.getVersionMsg()));
    }

    public void setButtonClickListener(CustomClickListener clickListener) {
        this.clickListener = clickListener;
        setCanceledOnTouchOutside(false);
    }

    private View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                switch (v.getId()) {
                    case R.id.update_cancel_tv:
                        clickListener.cancel();
                        break;
                    case R.id.update_submit_tv:
                        clickListener.confirm();
                        break;
                }
            }
        }
    };

    public interface CustomClickListener {
        public void cancel();
        public void confirm();
    }
}
