package com.xbx.client.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.xbx.client.R;

import java.util.List;

/**
 * Created by EricYuan on 2016/4/1.
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.MyViewHolder>{
    private Context context;
    private List<PoiInfo> poiList;

    public SearchResultAdapter(Context context,List<PoiInfo> poiList){
        this.context = context;
        this.poiList = poiList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_res_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PoiInfo poiInfo = poiList.get(position);
        holder.search_name.setText(poiInfo.name);
        holder.search_address.setText(poiInfo.address);
    }

    @Override
    public int getItemCount() {
        return poiList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView search_name;
        private TextView search_address;
        public MyViewHolder(View itemView) {
            super(itemView);
            search_name = (TextView) itemView.findViewById(R.id.search_name);
            search_address = (TextView) itemView.findViewById(R.id.search_address);
        }
    }
}
