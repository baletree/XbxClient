package com.xbx.client.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.utils.Util;

public class LoadingFragment extends DialogFragment{
	private TextView vLoading_text;
	private String mMsg = "";

	@SuppressLint("InflateParams")
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.fragment_loading, null);
		vLoading_text = (TextView) view.findViewById(R.id.loading_text);
		if(Util.isNull(mMsg)){
			vLoading_text.setVisibility(View.GONE);
		}else {
			vLoading_text.setText(mMsg);
		}
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
	public void onStart() {
		super.onStart();
	}
	public void setMsg(String msg) {
		if (!Util.isNull(msg)) {
			this.mMsg = msg;
		}
	}
}
