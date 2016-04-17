package com.xbx.client.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.utils.Constant;

/**
 * Created by 鹏 on 2016/4/17.
 */
public class UserInfoActivity extends BaseActivity {

    private TextView title_txt_tv;
    private TextView title_rtxt_tv;
    private TextView user_info_nickname_text;
    private TextView user_info_sex_text;
    private TextView user_info_birthday_text;
    private TextView user_info_name_text;
    private TextView user_info_phone_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
    }

    @Override
    protected void initViews() {
        title_txt_tv = (TextView) findViewById(R.id.title_txt_tv);
        title_rtxt_tv = (TextView) findViewById(R.id.title_rtxt_tv);
        user_info_nickname_text = (TextView) findViewById(R.id.user_info_nickname_text);
        user_info_sex_text = (TextView) findViewById(R.id.user_info_sex_text);
        user_info_birthday_text = (TextView) findViewById(R.id.user_info_birthday_text);
        user_info_name_text = (TextView) findViewById(R.id.user_info_name_text);
        user_info_phone_text = (TextView) findViewById(R.id.user_info_phone_text);

        title_txt_tv.setText(getString(R.string.user_info_title));
        title_rtxt_tv.setText(getString(R.string.user_info_title_right_text));
        title_rtxt_tv.setOnClickListener(this);
        findViewById(R.id.title_left_img).setOnClickListener(this);
        findViewById(R.id.user_info_head_layout).setOnClickListener(this);
        findViewById(R.id.user_info_nickname_layout).setOnClickListener(this);
        findViewById(R.id.user_info_sex_layout).setOnClickListener(this);
        findViewById(R.id.user_info_birthday_layout).setOnClickListener(this);
        findViewById(R.id.user_info_name_layout).setOnClickListener(this);
        findViewById(R.id.user_info_phone_layout).setOnClickListener(this);
    }

    @Override
    protected void initDatas() {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.title_left_img :
                finish();
                break;
            case R.id.title_rtxt_tv :
                break;
            case R.id.user_info_head_layout :
                intent.setClass(this, SelectAlbumActivity.class);
                intent.putExtra("type", 1);
                startActivityForResult(intent, 100);
                break;
            case R.id.user_info_nickname_layout :
                break;
            case R.id.user_info_sex_layout :
                intent.setClass(this, SelectSexActivity.class);
                startActivityForResult(intent, 102);
                break;
            case R.id.user_info_birthday_layout :
                break;
            case R.id.user_info_name_layout :
                break;
            case R.id.user_info_phone_layout :
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && data != null) {
            switch (requestCode) {
                case 100:
                    String picPath = data.getStringExtra(Constant.KEY_PHOTO_PATH);

                    break;
                case 102:
                    String sex = data.getStringExtra("result");
                    user_info_sex_text.setText(sex);
                    break;
            }
        }
    }

}
