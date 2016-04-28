package com.xbx.client.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Gallery;

import com.xbx.client.R;
import com.xbx.client.adapter.TogetherAdapter;
import com.xbx.client.beans.GuideBean;
import com.xbx.client.beans.TogetherBean;
import com.xbx.client.utils.Util;

import java.util.List;

/**
 * Created by EricYuan on 2016/4/18.
 */
public class TogetherActivity extends Activity implements TogetherAdapter.CallListener {
    private Gallery together_gallery;
    private TogetherAdapter adapter;
    private List<GuideBean> list;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_together);
        initData();
        initViews();
    }

    private void initData() {
        Intent intent = getIntent();
        list = (List<GuideBean>) intent.getSerializableExtra("TogetherList");
        position = intent.getIntExtra("TegetherSelect", 0);
    }

    private void initViews() {
        together_gallery = (Gallery) findViewById(R.id.together_gallery);

        together_gallery.setSelection(position);

        together_gallery.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectTab(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (list == null)
            return;
        adapter = new TogetherAdapter(list);
        together_gallery.setAdapter(adapter);
        adapter.setSelectTab(position);
        adapter.setCallLisener(this);
    }

    @Override
    public void callClick(int position) {
        Intent intent = new Intent();
        intent.putExtra("TogetherId",list.get(position).getGuideId());
        setResult(RESULT_OK,intent);
        finish();
    }
}
