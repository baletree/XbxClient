package com.xbx.client.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.view.WheelView;

import java.util.Arrays;

/**
 * Created by EricYuan on 2016/4/12.
 * 打赏
 */
public class RewardActivity extends Activity implements View.OnClickListener{
    private TextView wheelview_title_tv;
    private WheelView peonum_wheelView;
    private TextView peo_cancel_txt;
    private TextView peo_sure_txt;

    private String[] rewardMoney = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choice_people);
        initViews();
    }
    private void initViews(){
        rewardMoney = getResources().getStringArray(R.array.rewardMoney);
        wheelview_title_tv = (TextView) findViewById(R.id.wheelview_title_tv);
        wheelview_title_tv.setText(getString(R.string.reward_money));
        peonum_wheelView = (WheelView) findViewById(R.id.peonum_wheelView);
        peonum_wheelView.setOffset(1);
        peonum_wheelView.setItems(Arrays.asList(rewardMoney));
        peo_cancel_txt = (TextView) findViewById(R.id.peo_cancel_txt);
        peo_sure_txt = (TextView) findViewById(R.id.peo_sure_txt);
        peo_cancel_txt.setOnClickListener(this);
        peo_sure_txt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.peo_cancel_txt:
                finish();
                break;
            case R.id.peo_sure_txt:
                finish();
                break;
        }
    }
}
