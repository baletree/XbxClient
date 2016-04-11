package com.xbx.client.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.xbx.client.R;
import com.xbx.client.beans.PoiResultBean;

import java.util.List;

/**
 * Created by EricYuan on 2016/4/1.
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MyViewHolder>{
    private Context context;
    private List<PoiResultBean> poiResultList;

    private OnRecyItemClickListener mOnItemClickListener;

    public SearchResultAdapter(Context context,List<PoiResultBean> poiResultList){
        this.context = context;
        this.poiResultList = poiResultList;
    }

    public void setOnItemClickListener(OnRecyItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_res_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final PoiResultBean poiInfo = poiResultList.get(position);
        holder.search_name.setText(poiInfo.getPoiKey());
        holder.search_address.setText(poiInfo.getPoiAddress());
        holder.search_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return poiResultList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout search_item_layout;
        private TextView search_name;
        private TextView search_address;
        public MyViewHolder(View itemView) {
            super(itemView);
            search_name = (TextView) itemView.findViewById(R.id.search_name);
            search_address = (TextView) itemView.findViewById(R.id.search_address);
            search_item_layout = (RelativeLayout) itemView.findViewById(R.id.search_item_layout);
        }
    }

    public interface OnRecyItemClickListener {
        void onItemClick(View v, int position);
    }
}
