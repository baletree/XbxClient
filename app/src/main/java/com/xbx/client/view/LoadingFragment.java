package com.xbx.client.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.utils.Util;

public class LoadingFragment extends DialogFragment {
    private TextView vLoading_text;
    private ProgressBar loading_bar;
    private ProgressBar loading_bar2;
    private ImageView dialog_loading_icon;

    private String mMsg = "";
    // 均匀旋转动画
    private RotateAnimation refreshingAnimation;

    @SuppressLint("InflateParams")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_loading, null);
        vLoading_text = (TextView) view.findViewById(R.id.loading_text);
        loading_bar = (ProgressBar) view.findViewById(R.id.loading_bar);
        loading_bar2 = (ProgressBar) view.findViewById(R.id.loading_bar2);
        dialog_loading_icon = (ImageView) view.findViewById(R.id.dialog_loading_icon);
        refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
                getActivity(), R.anim.rotating);
        LinearInterpolator lir = new LinearInterpolator();
        refreshingAnimation.setInterpolator(lir);
        dialog_loading_icon.startAnimation(refreshingAnimation);
        if (!Util.isNull(mMsg))
            vLoading_text.setText(mMsg);
        Dialog dialog = new Dialog(getActivity(), R.style.MyLoadDialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(view);
        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        dialog_loading_icon.clearAnimation();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void setMsg(String msg) {
        if (!Util.isNull(msg)) {
            this.mMsg = msg;
        }
    }
}
