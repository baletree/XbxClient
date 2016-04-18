package com.xbx.client.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.utils.Util;

public class FindGuideFragment extends DialogFragment implements DialogInterface.OnKeyListener,View.OnClickListener {
	private TextView vLoading_text;
	private ImageView vLoading_img;
	private LinearLayout cancle_find_ll;
	private ProgressBar find_loading_bar;
	private ProgressBar find_loading_bar2;

	private String mMsg = "";
	private OnCancelGuiFindLisener cancelFindLisener;

	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = initViews();
		return dialog;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	public void setOncancelFindLisen(OnCancelGuiFindLisener cancelFindLisener){
		this.cancelFindLisener = cancelFindLisener;
	}

	private Dialog initViews(){
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.find_guide_loading, null);
		vLoading_text = (TextView) view.findViewById(R.id.find_loading_text);
		vLoading_img = (ImageView) view.findViewById(R.id.cancel_find_img);
		cancle_find_ll = (LinearLayout) view.findViewById(R.id.cancle_find_ll);
		find_loading_bar = (ProgressBar) view.findViewById(R.id.find_loading_bar);
		find_loading_bar2 = (ProgressBar) view.findViewById(R.id.find_loading_bar2);
		if(Integer.valueOf(android.os.Build.VERSION.SDK_INT) >= 23){
			find_loading_bar2.setVisibility(View.VISIBLE);
			find_loading_bar.setVisibility(View.GONE);
		}else{
			find_loading_bar.setVisibility(View.GONE);
			find_loading_bar2.setVisibility(View.VISIBLE);
		}
		if(Util.isNull(mMsg)){
			vLoading_text.setVisibility(View.GONE);
		}else {
			vLoading_text.setText(mMsg);
		}
		cancle_find_ll.setOnClickListener(this);
		Dialog dialog = new Dialog(getActivity(), R.style.MyLoadDialog);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentView(view);
		dialog.setOnKeyListener(this);
		return dialog;
	}

	public void setMsg(String msg) {
		if (!Util.isNull(msg)) {
			this.mMsg = msg;
		}
	}

	@Override
	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
		Util.pLog("返回按键。。");
		if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0){
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.cancle_find_ll:
				if(cancelFindLisener == null)
					return;
				cancelFindLisener.cancelGuiFind();
				break;
		}
	}

	public interface OnCancelGuiFindLisener{
		public void cancelGuiFind();
	}
}
