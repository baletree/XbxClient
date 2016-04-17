package com.xbx.client.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.ui.activity.ChocieCityActivity;

import java.util.List;

/**
 * Created by EricYuan on 2016/4/13.
 * 选择导游
 */
public class ChoiceGuideAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<String> infoList = null;

    private ItemLisener itemLisener;

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;

    public ChoiceGuideAdapter(Context context,List<String> infoList){
        this.context = context;
        this.infoList = infoList;
    }

    public void setOnItemLisener(ItemLisener itemLisener){
        this.itemLisener = itemLisener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case TYPE_ITEM:
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choice_guide,parent,false);
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
            ((MyViewHolder) holder).re_lookdetail_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemLisener != null)
                        itemLisener.clickDetail();
                }
            });
            ((MyViewHolder) holder).re_giveOrder_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itemLisener != null)
                        itemLisener.clickGiveOrder();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return infoList.size() == 0 ? 0 : infoList.size() + 1;
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
        public MyViewHolder(View itemView) {
            super(itemView);
            re_guiName_tv = (TextView) itemView.findViewById(R.id.re_guiName_tv);
            re_guiPrice_tv = (TextView) itemView.findViewById(R.id.re_guiPrice_tv);
            re_guiCount_tv = (TextView) itemView.findViewById(R.id.re_guiCount_tv);
            re_guiPriceTxt_tv = (TextView) itemView.findViewById(R.id.re_guiPriceTxt_tv);
            re_guiType_tv = (TextView) itemView.findViewById(R.id.re_guiType_tv);
            re_guide_ratingbar = (RatingBar) itemView.findViewById(R.id.re_guide_ratingbar);
            re_lookdetail_btn = (Button) itemView.findViewById(R.id.re_lookdetail_btn);
            re_giveOrder_btn = (Button) itemView.findViewById(R.id.re_giveOrder_btn);
        }
    }
    static class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }

    public interface ItemLisener{
        public void clickDetail();
        public void clickGiveOrder();
    }
}
