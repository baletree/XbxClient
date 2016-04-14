package com.xbx.client.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.beans.DateItemBean;
import com.xbx.client.utils.Util;

import java.util.List;

/**
 * Created by EricYuan on 2016/4/12.
 */
public class DateShowAdapter extends RecyclerView.Adapter<DateShowAdapter.MyViewHolder> {
    private List<DateItemBean> dateList;
    private Context context;
    private int nowWeek;
    private OnRecyItemClickListener mOnItemClickListener;

    public DateShowAdapter(Context context, List<DateItemBean> dateList, int nowWeek) {
        this.context = context;
        this.dateList = dateList;
        this.nowWeek = nowWeek;
    }

    public void setOnItemClickListener(OnRecyItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_num, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (position >= nowWeek && position < dateList.size() + nowWeek) {
//            Util.pLog("position:"+position+" nowWeek:"+nowWeek);
            DateItemBean dateBean = dateList.get(position - nowWeek);
            holder.item_date_tv.setText(dateBean.getDateNum());
            if (dateBean.isChoice())
                holder.item_date_tv.setTextColor(Color.rgb(68, 38, 128));
            else
                holder.item_date_tv.setTextColor(Color.rgb(180, 180, 180));
            holder.date_item_rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mOnItemClickListener == null)
                        return;
                    mOnItemClickListener.onItemClick(v,position);
                }
            });
        } else {
            holder.item_date_tv.setText("");
            holder.date_item_rl.setOnClickListener(null);
        }
    }

    @Override
    public int getItemCount() {
        return dateList.size() + nowWeek;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView item_date_tv;
        private RelativeLayout date_item_rl;

        public MyViewHolder(View itemView) {
            super(itemView);
            item_date_tv = (TextView) itemView.findViewById(R.id.item_date_tv);
            date_item_rl = (RelativeLayout) itemView.findViewById(R.id.date_item_rl);
        }
    }

    public interface OnRecyItemClickListener {
        void onItemClick(View v, int position);
    }
}
