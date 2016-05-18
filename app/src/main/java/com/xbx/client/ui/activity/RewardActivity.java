package com.xbx.client.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.view.WheelView;

import java.util.Arrays;
import java.util.List;

/**
 * Created by EricYuan on 2016/4/12.
 * 打赏
 */
public class RewardActivity extends Activity implements View.OnClickListener{
    public static final int rewardResult = 1000;
    private TextView wheelview_title_tv;
    private WheelView peonum_wheelView;
    private TextView peo_cancel_txt;
    private TextView peo_sure_txt;

//    private String[] rewardMoney = null;
    private List<String> rewadMonList = null;
    private int selectIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choice_people);
        initViews();
    }
    private void initViews(){
        rewadMonList = getIntent().getStringArrayListExtra("rewardMonList");
        wheelview_title_tv = (TextView) findViewById(R.id.wheelview_title_tv);
        wheelview_title_tv.setText(getString(R.string.reward_money));
        peonum_wheelView = (WheelView) findViewById(R.id.peonum_wheelView);
        peonum_wheelView.setOffset(1);
        peonum_wheelView.setItems(rewadMonList);
        peo_cancel_txt = (TextView) findViewById(R.id.peo_cancel_txt);
        peo_sure_txt = (TextView) findViewById(R.id.peo_sure_txt);
        peo_cancel_txt.setOnClickListener(this);
        peo_sure_txt.setOnClickListener(this);
        peonum_wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                super.onSelected(selectedIndex, item);
                selectIndex = selectedIndex - 1;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.peo_cancel_txt:
                finish();
                break;
            case R.id.peo_sure_txt:
                if(rewadMonList != null){
                    Intent intent = new Intent();
                    intent.putExtra("RewardMoney",rewadMonList.get(selectIndex));
                    setResult(RESULT_OK,intent);
                }
                finish();
                break;
        }
    }
}
