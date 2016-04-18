package com.xbx.client.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xbx.client.R;
import com.xbx.client.beans.TogetherBean;
import com.xbx.client.utils.AnimateFirstDisplayListener;
import com.xbx.client.utils.ImageLoaderConfigFactory;
import com.xbx.client.utils.Util;

import java.util.List;

/**
 * Created by EricYuan on 2016/4/18.
 */
public class TogetherAdapter extends BaseAdapter {

    private List<TogetherBean> list;
    private int mSelectTab;
    public ImageLoader imageLoader;
    public ImageLoaderConfigFactory configFactory;

    public TogetherAdapter(List<TogetherBean> list) {
        this.list = list;
        imageLoader = ImageLoader.getInstance();
        configFactory = ImageLoaderConfigFactory.getInstance();
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.togther_item, null);
            holder.bg = (ImageView) convertView.findViewById(R.id.together_item_bg);
            holder.head = (RoundedImageView) convertView.findViewById(R.id.together_item_head_img);
            holder.name = (TextView) convertView.findViewById(R.id.together_item_name);
            holder.ratingbar = (RatingBar) convertView.findViewById(R.id.together_item_ratingbar);
            holder.score = (TextView) convertView.findViewById(R.id.together_item_score);
            holder.price = (TextView) convertView.findViewById(R.id.together_item_price);
            holder.num = (TextView) convertView.findViewById(R.id.together_item_num);
            holder.call = (TextView) convertView.findViewById(R.id.together_item_call);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(position == mSelectTab) {
            holder.bg.setVisibility(View.VISIBLE);
            Util.pLog("ItemSelect显示");
        } else {
            holder.bg.setVisibility(View.GONE);
        }
        final TogetherBean bean = list.get(position);
        /*imageLoader.displayImage(bean.getHeadImg(), holder.head, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
        holder.name.setText(bean.getName());
        holder.score.setText(bean.getScore()+"分");
        holder.price.setText(bean.getPrice());
        holder.num.setText(bean.getNum());*/
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }

    private class ViewHolder {

        private ImageView bg;
        private RoundedImageView head;
        private TextView name;
        private RatingBar ratingbar;
        private TextView score;
        private TextView price;
        private TextView num;
        private TextView call;

    }

    public void setSelectTab(int tab) {
        if(tab != mSelectTab) {
            mSelectTab = tab;
            notifyDataSetChanged();
        }
    }
}
