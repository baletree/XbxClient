package com.xbx.client.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.bartoszlipinski.recyclerviewheader.RecyclerViewHeader;
import com.xbx.client.R;
import com.xbx.client.adapter.SearchResultAdapter;
import com.xbx.client.beans.PoiResultBean;
import com.xbx.client.utils.SharePrefer;
import com.xbx.client.utils.Util;
import com.xbx.client.view.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/3/31.
 * 搜索地址
 */
public class SearchAddressActivity extends BaseActivity implements
        OnGetPoiSearchResultListener, OnGetSuggestionResultListener, OnGetGeoCoderResultListener, SearchResultAdapter.OnRecyItemClickListener {
    private RelativeLayout search_back_layout;
    private EditText search_input_et;
    private TextView search_cancel_tv;
    private RecyclerView locate_rv;

    private PoiSearch mPoiSearch = null;
    private SuggestionSearch mSuggestionSearch = null;
    private GeoCoder geoCoder = null;
    private LinearLayoutManager layoutManager = null;
    private int searchCode = 0;
    private int resultCount = 0;
    private int count = 0;

    private List<PoiResultBean> poiReultList = null;
    private SearchResultAdapter searchResultAdapter = null;

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
        layoutManager = new LinearLayoutManager(this);
        searchCode = getIntent().getIntExtra("guide_code", 0);
        poiReultList = new ArrayList<>();
    }

    @Override
    protected void initViews() {
        super.initViews();
        search_back_layout = (RelativeLayout) findViewById(R.id.search_back_layout);
        search_input_et = (EditText) findViewById(R.id.search_input_et);
        search_cancel_tv = (TextView) findViewById(R.id.search_cancel_tv);
        locate_rv = (RecyclerView) findViewById(R.id.locate_rv);
        locate_rv.setLayoutManager(layoutManager);
        locate_rv.addItemDecoration(new RecycleViewDivider(this, R.drawable.spitline_bg));
        RecyclerViewHeader recyclerHeader = (RecyclerViewHeader) findViewById(R.id.hear_layout);
        recyclerHeader.attachTo(locate_rv, true);
        locate_rv.setItemAnimator(new DefaultItemAnimator());
    }

    private void initLisener() {
        search_back_layout.setOnClickListener(this);
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
                String city = SharePrefer.getLocate(SearchAddressActivity.this).getCity();
                if (Util.isNull(city)) {
                    return;
                }
                /**
                 * 使用建议搜索服务获取建议列表，结果在onSuggestionResult()中更新
                 */
                count = 0;
                poiReultList = new ArrayList<PoiResultBean>();
                locate_rv.setAdapter(null);
                SuggestionSearchOption suggestOption = new SuggestionSearchOption();
                suggestOption.keyword(cs.toString());
                suggestOption.city(city);
                mSuggestionSearch.requestSuggestion(suggestOption);
            }
        });
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.search_back_layout:
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

    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        count++;
        Util.pLog("poiResult:" + "poiResult===地址：" + poiDetailResult.getAddress() + "  名字：" + poiDetailResult.getName() + "  count="+count+"  resultCount="+resultCount);
        PoiResultBean poiResult = new PoiResultBean();
        poiReultList.add(poiResult);
        if (count == resultCount) {
            if (poiReultList != null && poiReultList.size() > 0) {
                searchResultAdapter = new SearchResultAdapter(SearchAddressActivity.this, poiReultList);
                locate_rv.setAdapter(searchResultAdapter);
            }
        }
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if (suggestionResult == null)
            return;
        if (suggestionResult.getAllSuggestions() != null)
            resultCount = suggestionResult.getAllSuggestions().size();
        PoiDetailSearchOption detailOption = new PoiDetailSearchOption();
        for (SuggestionResult.SuggestionInfo info : suggestionResult.getAllSuggestions()) {
            if (info.uid != null) {
                Util.pLog("SuggestResult:" + info.city + "  key:" + info.key + "  uid:" + info.uid + "  disrect" + info.district);
                detailOption.poiUid(info.uid);
                mPoiSearch.searchPoiDetail(detailOption);
            }
        }
    }


    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {

    }

    @Override
    public void onItemClick(View v, int position) {
        /*if(poiList != null){
            Intent intent = new Intent();
            switch (searchCode){
                case Constant.outsetFlag:
                    intent.putExtra("outsetResult",poiList.get(position));
                    break;
                case Constant.destFlag:
                    intent.putExtra("destResult",poiList.get(position));
                    break;
            }
        }*/
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }
}
