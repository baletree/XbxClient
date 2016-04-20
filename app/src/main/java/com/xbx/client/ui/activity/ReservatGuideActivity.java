package com.xbx.client.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.xbx.client.R;
import com.xbx.client.adapter.DateShowAdapter;
import com.xbx.client.beans.DateItemBean;
import com.xbx.client.http.Api;
import com.xbx.client.utils.TaskFlag;
import com.xbx.client.utils.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by EricYuan on 2016/4/11.
 * 预约导游
 */
public class ReservatGuideActivity extends BaseActivity implements DateShowAdapter.OnRecyItemClickListener ,RadioGroup.OnCheckedChangeListener{
    private TextView title_txt_tv;
    private ImageView title_left_img;
    private TextView title_rtxt_tv;
    private TextView user_name_tv;
    private TextView user_destination_tv;
    private RecyclerView date_show_rv;
    private RadioGroup sex_rg;
    private RadioGroup language_rg;

    private List<DateItemBean> dateList = null;
    private DateShowAdapter dateAdapter = null;
    private PoiInfo poiInfoRe = null;
    private Api api = null;

    private int nowWeek;
    private int maxDay = 0;
    private String choiceCityId = "";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case TaskFlag.REQUESTSUCCESS:

                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservat_guide);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        api = new Api(this,handler);
    }

    @Override
    protected void initViews() {
        super.initViews();
        title_txt_tv = (TextView) findViewById(R.id.title_txt_tv);
        title_left_img = (ImageView) findViewById(R.id.title_left_img);
        title_rtxt_tv = (TextView) findViewById(R.id.title_rtxt_tv);
        user_destination_tv = (TextView) findViewById(R.id.user_destination_tv);
        title_left_img.setOnClickListener(this);
        user_destination_tv.setOnClickListener(this);
        title_txt_tv.setText(getString(R.string.reservat_info));
        title_rtxt_tv.setText(getString(R.string.txt_sure));
        date_show_rv = (RecyclerView) findViewById(R.id.date_show_rv);
        title_rtxt_tv.setOnClickListener(this);
        title_rtxt_tv.setVisibility(View.VISIBLE);
        date_show_rv.setLayoutManager(new GridLayoutManager(this, 7));
        sex_rg = (RadioGroup) findViewById(R.id.sex_rg);
        language_rg = (RadioGroup) findViewById(R.id.language_rg);
        language_rg.setOnCheckedChangeListener(this);
        sex_rg.setOnCheckedChangeListener(this);
        setDateInRv();
    }

    private void setDateInRv() {
        dateList = new ArrayList<>();
        Calendar rightNow = Calendar.getInstance();
        Date date = new Date();
        nowWeek = rightNow.get(Calendar.DAY_OF_WEEK) - 1;
        maxDay = 8;
        for (int i = 1; i < maxDay; i++) {
            rightNow.setTime(date);
            rightNow.set(Calendar.DATE, rightNow.get(Calendar.DATE) + i);
            DateItemBean dateBean = new DateItemBean();
            dateBean.setDateNum(rightNow.get(Calendar.DAY_OF_MONTH) + "");
            dateBean.setDateReal(rightNow.get(Calendar.YEAR) + "-" + (rightNow.get(Calendar.MONTH) + 1) + "-" + rightNow.get(Calendar.DAY_OF_MONTH));
            dateList.add(dateBean);
        }
        if (dateList == null)
            return;
        dateAdapter = new DateShowAdapter(ReservatGuideActivity.this, dateList, nowWeek);
        date_show_rv.setAdapter(dateAdapter);
        dateAdapter.setOnItemClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK)
            return;
        if (data == null)
            return;
        switch (requestCode) {
            case 1000://目的地选择
                String destCity = data.getStringExtra("choiceCityName");
                choiceCityId = data.getStringExtra("choiceCityId");
                poiInfoRe = data.getParcelableExtra("destResult");
                if (poiInfoRe == null)
                    return;
                user_destination_tv.setText(destCity + "·" + poiInfoRe.name);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_img:
                finish();
                break;
            case R.id.title_rtxt_tv:
//                startActivity(new Intent(ReservatGuideActivity.this, ChoiceGuideActivity.class));

                break;
            case R.id.user_destination_tv:
                startActivityForResult(new Intent(ReservatGuideActivity.this, SerachInCityActvity.class), 1000);
                break;
        }
    }

    @Override
    public void onItemClick(View v, int position) {
        int theFirstChoice = -1;
        int theLastChoice = -1;
        for (int i = 0; i < dateList.size(); i++) {
            DateItemBean dBean = dateList.get(i);
            if (dBean.isChoice()) {
                theFirstChoice = i;
                break;
            }
        }
        for (int i = 0; i < dateList.size(); i++) {
            DateItemBean dBean = dateList.get(i);
            if (dBean.isChoice()) {
                theLastChoice = i;
            }
        }
        if (theFirstChoice != -1) {
            if (theFirstChoice == (position - nowWeek)) {
                if (dateList.get(theFirstChoice).isChoice())
                    dateList.get(theFirstChoice).setChoice(false);
                else
                    dateList.get(theFirstChoice).setChoice(true);
            } else {
                if (theFirstChoice < (position - nowWeek)) {
                    for (int i = theFirstChoice; i <= (position - nowWeek); i++) {
                        dateList.get(i).setChoice(true);
                    }
                } else {
                    for (int i = (position - nowWeek); i <= theFirstChoice; i++) {
                        dateList.get(i).setChoice(true);
                    }
                }
                if (theLastChoice == (position - nowWeek))
                    if (dateList.get(theLastChoice).isChoice())
                        dateList.get(theLastChoice).setChoice(false);
                    else
                        dateList.get(theLastChoice).setChoice(true);
            }
        } else {
            dateList.get(position - nowWeek).setChoice(true);
        }
        dateAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.mail_rb:
                Util.showToast(ReservatGuideActivity.this,"选择男性");
                break;
            case R.id.femail_rb:

                break;
            case R.id.not_limitsex_rb:

                break;
            case R.id.chinese_rb:

                break;
            case R.id.english_rb:

                break;
            case R.id.not_limitlangu_rb:

                break;
        }
    }
}
