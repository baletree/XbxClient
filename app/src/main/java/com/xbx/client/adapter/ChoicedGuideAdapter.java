package com.xbx.client.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.xbx.client.R;
import com.xbx.client.beans.ServerListBean;

import java.util.List;

/**
 * Created by EricPeng on 2016/4/21.
 */
public class ChoicedGuideAdapter extends BaseAdapter {
    private Context context;
    private List<ServerListBean> sList = null;

    public ChoicedGuideAdapter(Context context, List<ServerListBean> sList) {
        this.context = context;
        this.sList = sList;
    }

    @Override
    public int getCount() {
        return sList.size();
    }

    @Override
    public Object getItem(int position) {
        return sList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choice_guide, parent, false);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.re_guiName_tv = (TextView) convertView.findViewById(R.id.re_guiName_tv);
        holder.re_guiPrice_tv = (TextView) convertView.findViewById(R.id.re_guiPrice_tv);
        holder.re_guiCount_tv = (TextView) convertView.findViewById(R.id.re_guiCount_tv);
        holder.re_guiPriceTxt_tv = (TextView) convertView.findViewById(R.id.re_guiPriceTxt_tv);
        holder.re_guiType_tv = (TextView) convertView.findViewById(R.id.re_guiType_tv);
        holder.re_guiStar_tv = (TextView) convertView.findViewById(R.id.re_guiStar_tv);
        holder.re_guide_ratingbar = (RatingBar) convertView.findViewById(R.id.re_guide_ratingbar);
        holder.re_lookdetail_btn = (Button) convertView.findViewById(R.id.re_lookdetail_btn);
        holder.re_giveOrder_btn = (Button) convertView.findViewById(R.id.re_giveOrder_btn);
        holder.guide_head_img = (RoundedImageView) convertView.findViewById(R.id.guide_head_img);
        return convertView;
    }

    class ViewHolder {
        private TextView re_guiName_tv;
        private TextView re_guiPrice_tv;
        private TextView re_guiCount_tv;
        private TextView re_guiPriceTxt_tv;
        private TextView re_guiType_tv;
        private RatingBar re_guide_ratingbar;
        private TextView re_guiStar_tv;
        private Button re_lookdetail_btn;
        private Button re_giveOrder_btn;
        private RoundedImageView guide_head_img;
    }
}
