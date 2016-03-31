package com.xbx.client.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.xbx.client.R;
import com.xbx.client.utils.Util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by EricYuan on 2016/3/31.
 * 搜索地址
 */
public class SearchAddressActivity extends BaseActivity implements
        OnGetPoiSearchResultListener, OnGetSuggestionResultListener ,OnGetGeoCoderResultListener {
    private ImageView search_back_img;
    private EditText search_input_et;
    private TextView search_cancel_tv;
    private RecyclerView locate_rv;

    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    private GeoCoder geoCoder = null;

    private Map<LatLng,String> resultMap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serach_adr);
        initLisener();
    }

    @Override
    protected void initDatas() {
        super.initDatas();
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);
        mSuggestionSearch = SuggestionSearch.newInstance();
        mSuggestionSearch.setOnGetSuggestionResultListener(this);
        geoCoder = GeoCoder.newInstance();
        geoCoder.setOnGetGeoCodeResultListener(this);
    }

    @Override
    protected void initViews() {
        super.initViews();
        search_back_img = (ImageView) findViewById(R.id.search_back_img);
        search_input_et = (EditText) findViewById(R.id.search_input_et);
        search_cancel_tv = (TextView) findViewById(R.id.search_cancel_tv);
        locate_rv = (RecyclerView) findViewById(R.id.locate_rv);
    }

    private void initLisener() {
        search_back_img.setOnClickListener(this);
        search_cancel_tv.setOnClickListener(this);
        search_input_et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    colseKey(search_input_et);
                    String keyword = search_input_et.getText().toString();
                    if (Util.isNull(keyword)) {
                        Util.showToast(SearchAddressActivity.this, getString(R.string.search_null));
                    }
                    return true;
                }
                return false;
            }
        });
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
                String city = "成都";
                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                mSuggestionSearch
                        .requestSuggestion((new SuggestionSearchOption())
                                .keyword(cs.toString()).city(city));
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.search_back_img:
                finish();
                break;
            case R.id.search_cancel_tv:

                break;
        }
    }

    @Override
    protected void onDestroy() {
        mPoiSearch.destroy();
        mSuggestionSearch.destroy();
        super.onDestroy();
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        Util.pLog("Result-key:" + poiResult.getSuggestCityList().get(0).city);
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        Util.pLog("onGetPoiDetailResult:" + poiDetailResult.getAddress()+"/name:"+poiDetailResult.getName());
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        resultMap = new HashMap<>();
        for(int i = 0;i<suggestionResult.getAllSuggestions().size();i++){
            Util.pLog("Result-key:" + suggestionResult.getAllSuggestions().get(i).key);
            /*resultMap.put(suggestionResult.getAllSuggestions().get(i).pt,"");
            geoCoder.reverseGeoCode(new ReverseGeoCodeOption()
                    .location(suggestionResult.getAllSuggestions().get(i).pt));*/
        }
    }


    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        Util.pLog("reverseGeoCodeResult="+reverseGeoCodeResult.getAddress());
        Iterator iter = resultMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            LatLng ll = (LatLng) entry.getKey();
            if(ll.equals(reverseGeoCodeResult.getLocation())){
                resultMap.put(ll,reverseGeoCodeResult.getAddress());
            }
        }
    }
}
