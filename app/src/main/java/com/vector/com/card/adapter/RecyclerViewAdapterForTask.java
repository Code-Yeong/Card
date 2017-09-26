package com.vector.com.card.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vector.com.card.R;
import com.vector.com.card.domian.Task;
import com.vector.com.card.utils.Utils;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Administrator on 2017/8/19.
 */
public class RecyclerViewAdapterForTask extends RecyclerView.Adapter {

    List<Task> list;
//    Context cxt;

    public RecyclerViewAdapterForTask(List<Task> list) {
        this.list = list;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.tv_content.setText(list.get(position).getContent());
        viewHolder.tv_time.setText(list.get(position).getStartTime());
        String status = list.get(position).getStatus();
        if (status.equals("0")) {
            viewHolder.iv_id.setImageResource(R.drawable.icon_task_on);
            viewHolder.tv_status.setText("进行中");
            viewHolder.tv_status.setTextColor(Color.GREEN);
        } else {
            viewHolder.iv_id.setImageResource(R.drawable.icon_task_stop);
            viewHolder.tv_status.setText("已终止");
            viewHolder.tv_status.setTextColor(Color.LTGRAY);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.self_task_item, null);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getId();
    }

    public String getItemStartTime(int position) {
        return list.get(position).getStartTime();
    }

    public String getItemStopTime(int position) {
        return list.get(position).getStopTime();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_content, tv_time, tv_status, tv_look, tv_delete;
        public View view;
        public ImageView iv_id;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            tv_content = (TextView) itemView.findViewById(R.id.self_task_item_content);
            iv_id = (ImageView) itemView.findViewById(R.id.self_task_item_id);
            tv_time = (TextView) itemView.findViewById(R.id.self_task_item_time);
            tv_status = (TextView) itemView.findViewById(R.id.self_task_item_status);
        }
    }
}
