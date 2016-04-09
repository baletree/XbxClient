package com.xbx.client.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xbx.client.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by EricYuan on 2016/4/8.
 */
public class GuideTagAdapter extends RecyclerView.Adapter<GuideTagAdapter.MyViewHolder>{
    private List<String> tagList;
    private List<Integer> weightList;
    public GuideTagAdapter(List<String> tagList){
        this.tagList = tagList;
//        getRandomHeight(tagList);
    }

    private void getRandomHeight(List<String> lists){//得到随机item的高度
        weightList = new ArrayList<>();
        for (int i = 0; i < lists.size(); i++) {
            weightList.add((int)(200+Math.random()*400));
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_guide_tag, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        /*ViewGroup.LayoutParams params =  holder.itemView.getLayoutParams();//得到item的
        params.width = weightList.get(position);//把随机的高度赋予item布局
        holder.itemView.setLayoutParams(params);//把params设置给item布局*/
        holder.guide_tag_tv.setText(tagList.get(position));
    }

    @Override
    public int getItemCount() {
        return tagList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView guide_tag_tv;
        public MyViewHolder(View itemView) {
            super(itemView);
            guide_tag_tv = (TextView) itemView.findViewById(R.id.guide_tag_tv);
        }
    }
}
