package com.xbx.client.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.xbx.client.R;
import com.xbx.client.adapter.SearchResultAdapter;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.Util;
import com.xbx.client.view.RecycleViewDivider;

import java.util.List;

/**
 * Created by EricPeng on 2016/4/17.
 * 预约搜索地点
 */
public class SerachInCityActvity extends BaseActivity implements
        OnGetPoiSearchResultListener, OnGetGeoCoderResultListener, SearchResultAdapter.OnRecyItemClickListener {
    private RelativeLayout sIncity_back_layout;
    private LinearLayout inCity_city_ll;
    private TextView inCity_city_tv;
    private RecyclerView inCity_locate_rv;
    private EditText search_input_et;

    private PoiSearch mPoiSearch = null;
    private GeoCoder geoCoder = null;
    private LinearLayoutManager layoutManager = null;
    private SearchResultAdapter searchResultAdapter = null;
    private List<PoiInfo> poiList = null;

    private String choiceCity = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_incity);
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        choiceCity = getString(R.string.default_city);
        layoutManager = new LinearLayoutManager(this);
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);
        String locateCity = SharePrefer.getLocate(SerachInCityActvity.this).getCity();
        Util.pLog("city:"+locateCity);
        if (!Util.isNull(locateCity))
            choiceCity = locateCity;
    }

    @Override
    protected void initViews() {
        super.initViews();
        sIncity_back_layout = (RelativeLayout) findViewById(R.id.sIncity_back_layout);
        sIncity_back_layout.setOnClickListener(this);
        inCity_city_ll = (LinearLayout) findViewById(R.id.inCity_city_ll);
        inCity_city_ll.setOnClickListener(this);
        inCity_city_tv = (TextView) findViewById(R.id.inCity_city_tv);
        inCity_city_tv.setText(choiceCity);
        search_input_et = (EditText) findViewById(R.id.search_input_et);
        inCity_locate_rv = (RecyclerView) findViewById(R.id.inCity_locate_rv);
        inCity_locate_rv.setLayoutManager(layoutManager);
        inCity_locate_rv.addItemDecoration(new RecycleViewDivider(this, R.drawable.spitline_bg));
        inCity_locate_rv.setItemAnimator(new DefaultItemAnimator());
        search_input_et.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
            }

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                if (cs.length() <= 0) {
                    return;
                }
                if (Util.isNull(choiceCity)) {
                    Util.showToast(SerachInCityActvity.this, getString(R.string.set_city));
                    return;
                }
                PoiCitySearchOption poiCityOption = new PoiCitySearchOption();
                poiCityOption.city(choiceCity);
                poiCityOption.keyword(cs.toString());
                poiCityOption.pageCapacity(50);
                mPoiSearch.searchInCity(poiCityOption);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null)
            return;
        if (resultCode != RESULT_OK)
            return;
        switch (requestCode) {
            case 1000:
                choiceCity = data.getStringExtra("choiceCity_name");
                inCity_city_tv.setText(choiceCity);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.sIncity_back_layout:
                finish();
                break;
            case R.id.inCity_city_ll:
                startActivityForResult(new Intent(SerachInCityActvity.this, ChocieCityActivity.class), 1000);
                break;
        }
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        poiList = poiResult.getAllPoi();
        if (poiList == null)
            return;
        searchResultAdapter = new SearchResultAdapter(this, poiList);
        inCity_locate_rv.setAdapter(searchResultAdapter);
        searchResultAdapter.setOnItemClickListener(this);
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

    }

    @Override
    public void onItemClick(View v, int position) {
        if (poiList == null)
            return;
        Intent intent = new Intent();
        intent.putExtra("choiceCityName",inCity_city_tv.getText().toString());
        intent.putExtra("destResult", poiList.get(position));
        setResult(RESULT_OK, intent);
        finish();
    }
}
