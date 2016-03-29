package com.xbx.client.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xbx.client.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/3/29.
 */
public class MyViewPagerAdapter extends FragmentPagerAdapter {

    //添加的Fragment的集合
    private final List<Fragment> mFragments = new ArrayList<>();
    //每个Fragment对应的title的集合
    private final List<String> mFragmentsTitles = new ArrayList<>();

    private Context context;


    public MyViewPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    /**
     * @param fragment      添加Fragment
     * @param fragmentTitle Fragment的标题，即TabLayout中对应Tab的标题
     */
    public void addFragment(Fragment fragment, String fragmentTitle) {
        mFragments.add(fragment);
        mFragmentsTitles.add(fragmentTitle);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentsTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //得到对应position的Fragment的title
        return mFragmentsTitles.get(position);
    }

    /**
     * 自定义TabLayout title
     *
     * @param position
     * @return
     */
    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_sxp_tab_title, null);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(mFragmentsTitles.get(position));
        ImageView img = (ImageView) view.findViewById(R.id.imageView);
        img.setImageResource(R.drawable.tab_bg);
        return view;
    }

}
