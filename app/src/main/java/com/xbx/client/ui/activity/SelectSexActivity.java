package com.xbx.client.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xbx.client.R;

/**
 * Created by 鹏 on 2016/4/17.
 */
public class SelectSexActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_sex);
        initViews();
    }

    private void initViews() {
        findViewById(R.id.select_sex_male).setOnClickListener(this);
        findViewById(R.id.select_sex_female).setOnClickListener(this);
        findViewById(R.id.select_sex_cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.select_sex_male:
                intent.putExtra("result", getString(R.string.select_sex_male));
                intent.putExtra("resultCode", "0");
                setResult(RESULT_OK, intent);
                break;
            case R.id.select_sex_female:
                intent.putExtra("result", getString(R.string.select_sex_female));
                intent.putExtra("resultCode", "1");
                setResult(RESULT_OK, intent);
                break;
        }
        finish();
    }
}
