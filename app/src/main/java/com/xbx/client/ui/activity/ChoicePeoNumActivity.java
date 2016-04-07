package com.xbx.client.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.utils.Util;
import com.xbx.client.view.WheelView;

import java.util.Arrays;

/**
 * Created by EricYuan on 2016/4/5.
 */
public class ChoicePeoNumActivity extends Activity implements View.OnClickListener{

    private TextView peo_cancel_txt;
    private TextView peo_sure_txt;
    private WheelView peonum_wheelView;

    private String peopleNum[] = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choice_people);
        initViews();
        initLisener();
    }

    private void initViews(){
        peopleNum = getResources().getStringArray(R.array.peopleNum);
        peo_cancel_txt = (TextView) findViewById(R.id.peo_cancel_txt);
        peo_sure_txt = (TextView) findViewById(R.id.peo_sure_txt);
        peonum_wheelView = (WheelView) findViewById(R.id.peonum_wheelView);
        peonum_wheelView.setOffset(1);
        peonum_wheelView.setItems(Arrays.asList(peopleNum));
    }

    private void initLisener(){
        peo_cancel_txt.setOnClickListener(this);
        peo_sure_txt.setOnClickListener(this);
        peonum_wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
            @Override
            public void onSelected(int selectedIndex, String item) {
                super.onSelected(selectedIndex, item);
                Log.i("Tag", "selectedIndex: " + selectedIndex + ", item: " + item);
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.peo_cancel_txt:
                finish();
                break;
            case R.id.peo_sure_txt:
                setResult(RESULT_OK,intent);
                finish();
                break;
        }
    }
}
