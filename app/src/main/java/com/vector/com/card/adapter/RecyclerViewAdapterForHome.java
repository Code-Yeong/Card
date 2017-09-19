package com.vector.com.card.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vector.com.card.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/8/3.
 */
public class RecyclerViewAdapterForHome extends RecyclerView.Adapter {
    public List<Map<String, String>> list = null;

    public RecyclerViewAdapterForHome(List<Map<String, String>> list) {
        this.list = list;
    }

    //创建新View，被LayoutManager所调用
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.self_home_item, viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    //将数据与界面进行绑定的操作
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        holder.number.setText("No."+(position + 1) + "");
        holder.content.setText(list.get(position).get("content"));
//            holder.content.setText(list.get(i).get("status"));

    }

    //获取数据的数量
    @Override
    public int getItemCount() {
        return list.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView number, content;
        public ImageView pic;

        public ViewHolder(View view) {
            super(view);
            number = (TextView) view.findViewById(R.id.home_item_number);
            content = (TextView) view.findViewById(R.id.home_item_content);
            pic = (ImageView) view.findViewById(R.id.home_item_pic);
        }
    }
}
