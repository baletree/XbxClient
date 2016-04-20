package com.xbx.client.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.adapter.CityListAdapter;
import com.xbx.client.adapter.ResultListAdapter;
import com.xbx.client.beans.City;
import com.xbx.client.db.DBManager;
import com.xbx.client.utils.Util;
import com.xbx.client.view.SideLetterBar;

import java.util.List;

/**
 * Created by EricPeng on 2016/4/17.
 */
public class ChocieCityActivity extends BaseActivity implements CityListAdapter.OnCityClickListener {

    private ImageView imgBack;
    private TextView txtTitle;
    private ListView mListView;
    private ListView mResultListView;
    private SideLetterBar mLetterBar;
    private EditText searchBox;
    private TextView clearBtn;
    private ViewGroup emptyView;

    private CityListAdapter mCityAdapter;
    private ResultListAdapter mResultAdapter;
    private List<City> mAllCities;
    private DBManager dbManager;

    private String KEY_PICKED_CITY = "choiceCity_Name";
    private String KEY_PICKED_CITYID = "choiceCity_Id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_city);
    }

    @Override
    protected void initViews() {
        super.initViews();
        Util.pLog("mCityAdapter == null " + (mCityAdapter == null));
        imgBack = (ImageView) findViewById(R.id.title_left_img);
        txtTitle = (TextView) findViewById(R.id.title_txt_tv);
        txtTitle.setText(getString(R.string.choice_city));
        if(mCityAdapter == null)
            return;
        mListView = (ListView) findViewById(R.id.listview_all_city);
        mListView.setAdapter(mCityAdapter);
        mCityAdapter.setOnCityClickListener(this);
        TextView overlay = (TextView) findViewById(R.id.tv_letter_overlay);
        mLetterBar = (SideLetterBar) findViewById(R.id.side_letter_bar);
        mLetterBar.setOverlay(overlay);
        mLetterBar.setOnLetterChangedListener(new SideLetterBar.OnLetterChangedListener() {
            @Override
            public void onLetterChanged(String letter) {
                int position = mCityAdapter.getLetterPosition(letter);
                mListView.setSelection(position);
            }
        });

        searchBox = (EditText) findViewById(R.id.et_search);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String keyword = s.toString();
                if (TextUtils.isEmpty(keyword)) {
//                    clearBtn.setVisibility(View.GONE);
                    emptyView.setVisibility(View.GONE);
                    mResultListView.setVisibility(View.GONE);
                } else {
//                    clearBtn.setVisibility(View.VISIBLE);
                    mResultListView.setVisibility(View.VISIBLE);
                    List<City> result = dbManager.searchCity(keyword);
                    if (result == null || result.size() == 0) {
                        emptyView.setVisibility(View.VISIBLE);
                    } else {
                        emptyView.setVisibility(View.GONE);
                        mResultAdapter.changeData(result);
                    }
                }
            }
        });

        emptyView = (ViewGroup) findViewById(R.id.empty_view);
        mResultListView = (ListView) findViewById(R.id.listview_search_result);
        mResultListView.setAdapter(mResultAdapter);
        mResultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                back(mResultAdapter.getItem(position).getName(),mResultAdapter.getItem(position).getId());
            }
        });
        imgBack.setOnClickListener(this);
    }

    private void back(String cityName,String cityId){
        Util.showToast(this, "搜索点击的城市：" + cityName);
        Intent data = new Intent();
        data.putExtra(KEY_PICKED_CITY, cityName);
        data.putExtra(KEY_PICKED_CITYID, cityId);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        dbManager = new DBManager(this);
        dbManager.copyDBFile();
        mAllCities = dbManager.getAllCities();
        Log.i("Tag", "mAllCities:"+mAllCities.size());
        mCityAdapter = new CityListAdapter(this, mAllCities);
        mResultAdapter = new ResultListAdapter(this, null);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.title_left_img:
                finish();
                break;
        }
    }

    @Override
    public void onCityClick(String cityName,String cityId) {
        Util.showToast(this, "点击的城市：" + cityName);
        Intent data = new Intent();
        data.putExtra(KEY_PICKED_CITY, cityName);
        data.putExtra(KEY_PICKED_CITYID, cityId);
        setResult(RESULT_OK, data);
        finish();
    }

}
