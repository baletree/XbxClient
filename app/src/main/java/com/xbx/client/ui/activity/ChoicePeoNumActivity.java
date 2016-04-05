package com.xbx.client.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xbx.client.R;

/**
 * Created by EricYuan on 2016/4/5.
 */
public class ChoicePeoNumActivity extends Activity implements View.OnClickListener{

    private TextView guide_opeo_tv;
    private TextView guide_fpeo_tv;
    private TextView guide_speo_tv;
    private TextView guide_tpeo_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_choice_people);
        initViews();
        initLisener();
    }

    private void initViews(){
        guide_opeo_tv = (TextView) findViewById(R.id.guide_opeo_tv);
        guide_fpeo_tv = (TextView) findViewById(R.id.guide_fpeo_tv);
        guide_speo_tv = (TextView) findViewById(R.id.guide_speo_tv);
        guide_tpeo_tv = (TextView) findViewById(R.id.guide_tpeo_tv);
    }

    private void initLisener(){
        guide_opeo_tv.setOnClickListener(this);
        guide_fpeo_tv.setOnClickListener(this);
        guide_speo_tv.setOnClickListener(this);
        guide_tpeo_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.guide_opeo_tv:
                intent.putExtra("choiceType", 1);
                break;
            case R.id.guide_fpeo_tv:
                intent.putExtra("choiceType", 2);
                break;
            case R.id.guide_speo_tv:
                intent.putExtra("choiceType", 3);
                break;
            case R.id.guide_tpeo_tv:
                intent.putExtra("choiceType", 4);
                break;
        }
        setResult(RESULT_OK,intent);
        finish();
    }
}
