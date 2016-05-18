package com.xbx.client.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xbx.client.R;
import com.xbx.client.beans.OrderBean;
import com.xbx.client.utils.StringUtil;
import com.xbx.client.utils.Util;

import java.util.List;

/**
 * Created by EricYuan on 2016/4/6.
 */
public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.MyViewHolder> {

    private OnRecyItemClickListener mOnItemClickListener;
    private List<OrderBean> orderList;
    private Context context;

    public MyOrderAdapter(Context context, List<OrderBean> orderList) {
        this.context = context;
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
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        OrderBean orderBean = orderList.get(position);
        holder.order_time_tv.setText(orderBean.getOrderTime());
        holder.order_state_tv.setText(StringUtil.getOrderState(context, orderBean.getOrderType(), orderBean.getOrderState()));
        holder.order_pay_tv.setText(orderBean.getOrderPay());
        getServerType(holder.order_type_tv,holder.serType_tv,context,orderBean.getOrderType());
        getGuideType(holder.order_guidetype_tv,holder.guiType_tv,context,orderBean.getGuideType());
        if (!Util.isNull(orderBean.getOrderAddress()) && orderBean.getOrderAddress().contains(context.getString(R.string.city_Name)))
            holder.order_address_tv.setText(orderBean.getOrderAddress().split(context.getString(R.string.city_Name))[1]);
        else
            holder.order_address_tv.setText(orderBean.getOrderAddress());
        holder.order_item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout order_item_layout;
        private TextView order_state_tv;
        private TextView order_pay_tv;
        private TextView order_time_tv;
        private TextView order_address_tv;
        private TextView order_type_tv;
        private TextView order_guidetype_tv;
        private TextView guiType_tv;
        private TextView serType_tv;

        public MyViewHolder(View itemView) {
            super(itemView);
            order_item_layout = (LinearLayout) itemView.findViewById(R.id.order_item_layout);
            order_time_tv = (TextView) itemView.findViewById(R.id.order_time_tv);
            order_guidetype_tv = (TextView) itemView.findViewById(R.id.order_guidetype_tv);
            order_state_tv = (TextView) itemView.findViewById(R.id.order_state_tv);
            order_address_tv = (TextView) itemView.findViewById(R.id.order_address_tv);
            order_type_tv = (TextView) itemView.findViewById(R.id.order_type_tv);
            order_pay_tv = (TextView) itemView.findViewById(R.id.order_pay_tv);
            guiType_tv = (TextView) itemView.findViewById(R.id.guiType_tv);
            serType_tv = (TextView) itemView.findViewById(R.id.serType_tv);
        }
    }

    private void getServerType(TextView textView, TextView textView1,Context context, int orderType){
        switch (orderType) {
            case 0:
                textView.setText(context.getString(R.string.order_immedia));
                textView1.setText(context.getString(R.string.immediaServer));
                textView.setBackgroundResource(R.drawable.immedia_circle);
                break;
            case 1:
                textView.setText(context.getString(R.string.order_reservat));
                textView1.setText(context.getString(R.string.reserveServer));
                textView.setBackgroundResource(R.drawable.reservat_circle);
                break;
        }
    }

    private void getGuideType(TextView textView,TextView textView1, Context context, int guideType) {
        switch (guideType) {
            case 1:
                textView.setText(context.getString(R.string.order_guide));
                textView1.setText(context.getString(R.string.main_guide));
                textView.setBackgroundResource(R.drawable.guide_circle);
                break;
            case 3:
                textView.setText(context.getString(R.string.order_native));
                textView1.setText(context.getString(R.string.main_native));
                textView.setBackgroundResource(R.drawable.native_circle);
                break;
        }
    }

    private void getSeverStr(TextView textView, Context context, int guideType){

    }

    public interface OnRecyItemClickListener {
        void onItemClick(View v, int position);
    }
}
