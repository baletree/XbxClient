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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xbx.client.R;
import com.xbx.client.beans.ServerListBean;
import com.xbx.client.linsener.AnimateFirstDisplayListener;
import com.xbx.client.linsener.ImageLoaderConfigFactory;
import com.xbx.client.utils.Util;

import java.util.List;

/**
 * Created by EricPeng on 2016/4/21.
 */
public class ChoicedGuideAdapter extends BaseAdapter {
    private Context context;
    private List<ServerListBean> sList = null;
    private ClickLisener onClick;

    private ImageLoader imageLoader;
    private ImageLoaderConfigFactory configFactory;

    public ChoicedGuideAdapter(Context context, List<ServerListBean> sList) {
        this.context = context;
        this.sList = sList;
        imageLoader = ImageLoader.getInstance();
        configFactory = ImageLoaderConfigFactory.getInstance();
    }

    public void setOnCicks(ClickLisener onClick) {
        this.onClick = onClick;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        holder.re_giveOrder_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClick == null)
                    return;
                onClick.downOrder(position);
            }
        });
        holder.re_lookdetail_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClick == null)
                    return;
                onClick.lookGuide(position);
            }
        });
        ServerListBean sBean = sList.get(position);
        holder.re_guiName_tv.setText(sBean.getServerName());
        holder.re_guiPrice_tv.setText(sBean.getServerPrice() + context.getString(R.string.server_day));
        imageLoader.displayImage(sBean.getServerHead(), holder.guide_head_img, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
        holder.re_guiCount_tv.setText(sBean.getServerTimes() + context.getString(R.string.server_times));
        if (!Util.isNull(sBean.getServerStar()))
            holder.re_guide_ratingbar.setRating(Float.valueOf(sBean.getServerStar()) / 2);
        holder.re_guiStar_tv.setText(sBean.getServerStar() + context.getString(R.string.scole));
        return convertView;
    }

    public interface ClickLisener {
        public void downOrder(int position);

        public void lookGuide(int position);
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
