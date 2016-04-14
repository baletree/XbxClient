package com.xbx.client.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.adapter.DateShowAdapter;
import com.xbx.client.beans.DateItemBean;
import com.xbx.client.utils.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by EricYuan on 2016/4/11.
 * 预约导游
 */
public class ReservatGuideActivity extends BaseActivity implements DateShowAdapter.OnRecyItemClickListener {
    private TextView title_txt_tv;
    private ImageView title_left_img;
    private TextView title_rtxt_tv;
    private TextView user_name_tv;
    private TextView user_setoff_tv;
    private TextView user_destination_tv;
    private RecyclerView date_show_rv;

    private int nowWeek;
    private int maxDay = 0;
    private List<DateItemBean> dateList = null;
    private DateShowAdapter dateAdapter = null;

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
        date_show_rv = (RecyclerView) findViewById(R.id.date_show_rv);
        title_rtxt_tv.setOnClickListener(this);
        title_rtxt_tv.setVisibility(View.VISIBLE);
        date_show_rv.setLayoutManager(new GridLayoutManager(this, 7));
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
//            Util.pLog(rightNow.get(Calendar.YEAR) + "-" + (rightNow.get(Calendar.MONTH) + 1) + "-" + rightNow.get(Calendar.DAY_OF_MONTH));
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
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.title_left_img:
                finish();
                break;
            case R.id.title_rtxt_tv:
                startActivity(new Intent(ReservatGuideActivity.this, ChoiceGuideActivity.class));
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
//     Util.pLog("AdClickPos:" + (position - nowWeek) + "  AdthsFirstChoi:" + theFirstChoice);
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
}
