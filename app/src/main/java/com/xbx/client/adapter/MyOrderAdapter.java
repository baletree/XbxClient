package com.xbx.client.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.beans.OrderBean;

import java.util.List;

/**
 * Created by EricYuan on 2016/4/6.
 */
public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> {

    private OnRecyItemClickListener mOnItemClickListener;
    private List<OrderBean> orderList;

    public MyOrderAdapter(List<OrderBean> orderList){
        this.orderList = orderList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    public void setOnItemClickListener(OnRecyItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        OrderBean orderBean = orderList.get(position);
        holder.order_time_tv.setText(orderBean.getOrderTime());
        holder.order_guidetype_tv.setText(orderBean.getGuideType());
        holder.order_state_tv.setText(orderBean.getOrderState());
        holder.order_address_tv.setText(orderBean.getOrderAddress());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView order_time_tv;
        private TextView order_guidetype_tv;
        private TextView order_state_tv;
        private TextView order_address_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            order_time_tv = (TextView) itemView.findViewById(R.id.order_time_tv);
            order_guidetype_tv = (TextView) itemView.findViewById(R.id.order_guidetype_tv);
            order_state_tv = (TextView) itemView.findViewById(R.id.order_state_tv);
            order_address_tv = (TextView) itemView.findViewById(R.id.order_address_tv);
        }
    }

    public interface OnRecyItemClickListener {
        void onItemClick(View v, int position);
    }
}
