package com.xbx.client.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by EricYuan on 2016/4/13.
 * 选择导游
 */
public class ChoiceGuideAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<ServerListBean> sList = null;

    private ItemLisener itemLisener;
    private ImageLoader imageLoader;
    private ImageLoaderConfigFactory configFactory;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;


    public ChoiceGuideAdapter(Context context, List<ServerListBean> sList) {
        this.context = context;
        this.sList = sList;
        imageLoader = ImageLoader.getInstance();
        configFactory = ImageLoaderConfigFactory.getInstance();
    }

    public void setOnItemLisener(ItemLisener itemLisener) {
        this.itemLisener = itemLisener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_ITEM:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choice_guide, parent, false);
                return new MyViewHolder(view);
            case TYPE_FOOTER:
                View footView = LayoutInflater.from(context).inflate(R.layout.item_foot, parent,
                        false);
                return new FootViewHolder(footView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {
            if (position == sList.size())
                return;
            ServerListBean sBean = sList.get(position);
            ((MyViewHolder) holder).re_lookdetail_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemLisener != null)
                        itemLisener.clickDetail();
                }
            });
            ((MyViewHolder) holder).re_giveOrder_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemLisener != null)
                        itemLisener.clickGiveOrder();
                }
            });
            ((MyViewHolder) holder).re_guiName_tv.setText(sBean.getServerName());
            ((MyViewHolder) holder).re_guiPrice_tv.setText(sBean.getServerPrice() + context.getString(R.string.server_day));
            imageLoader.displayImage(sBean.getServerHead(), ((MyViewHolder) holder).guide_head_img, configFactory.getHeadImg(), new AnimateFirstDisplayListener());
            ((MyViewHolder) holder).re_guiCount_tv.setText(sBean.getServerTimes() + context.getString(R.string.server_times));
            if (!Util.isNull(sBean.getServerStar()))
                ((MyViewHolder) holder).re_guide_ratingbar.setRating(Float.valueOf(sBean.getServerStar()) / 2);
            ((MyViewHolder) holder).re_guiStar_tv.setText(sBean.getServerStar() + context.getString(R.string.scole));
        }
    }

    @Override
    public int getItemCount() {
        if (sList.size() == 0)
            return 0;
        return sList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
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

        public MyViewHolder(View itemView) {
            super(itemView);
            re_guiName_tv = (TextView) itemView.findViewById(R.id.re_guiName_tv);
            re_guiPrice_tv = (TextView) itemView.findViewById(R.id.re_guiPrice_tv);
            re_guiCount_tv = (TextView) itemView.findViewById(R.id.re_guiCount_tv);
            re_guiPriceTxt_tv = (TextView) itemView.findViewById(R.id.re_guiPriceTxt_tv);
            re_guiType_tv = (TextView) itemView.findViewById(R.id.re_guiType_tv);
            re_guiStar_tv = (TextView) itemView.findViewById(R.id.re_guiStar_tv);
            re_guide_ratingbar = (RatingBar) itemView.findViewById(R.id.re_guide_ratingbar);
            re_lookdetail_btn = (Button) itemView.findViewById(R.id.re_lookdetail_btn);
            re_giveOrder_btn = (Button) itemView.findViewById(R.id.re_giveOrder_btn);
            guide_head_img = (RoundedImageView) itemView.findViewById(R.id.guide_head_img);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }

    public interface ItemLisener {
        public void clickDetail();

        public void clickGiveOrder();
    }
}
