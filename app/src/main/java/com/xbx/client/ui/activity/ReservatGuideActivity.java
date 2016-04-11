package com.xbx.client.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xbx.client.R;

/**
 * Created by EricYuan on 2016/4/11.
 * 预约导游
 */
public class ReservatGuideActivity extends BaseActivity {
    private TextView title_txt_tv;
    private ImageView title_left_img;
    private TextView title_rtxt_tv;
    private TextView user_name_tv;
    private TextView user_setoff_tv;
    private TextView user_destination_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservat_guide);
    }

    @Override
    protected void initViews() {
        super.initViews();
        title_txt_tv = (TextView) findViewById(R.id.title_txt_tv);
        title_left_img = (ImageView) findViewById(R.id.title_left_img);
        title_rtxt_tv = (TextView) findViewById(R.id.title_rtxt_tv);
        title_left_img.setOnClickListener(this);
        title_txt_tv.setText(getString(R.string.reservat_info));
        title_rtxt_tv.setText(getString(R.string.txt_sure));
        title_rtxt_tv.setOnClickListener(this);
        title_rtxt_tv.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.title_left_img:
                finish();
                break;
            case R.id.title_rtxt_tv:
                startActivity(new Intent(ReservatGuideActivity.this,ChoiceGuideActivity.class));
                break;
        }
    }
}
