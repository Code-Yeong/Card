package com.vector.com.card.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        viewHolder.tv_id.setText("No." + (position + 1));
        String status = list.get(position).getStatus();
        if (status.equals("0")) {
            viewHolder.tv_time.setText("进行中");
            viewHolder.tv_time.setTextColor(Color.GREEN);
        } else {
            viewHolder.tv_time.setText("已终止");
            viewHolder.tv_time.setTextColor(Color.LTGRAY);
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

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_id, tv_content, tv_time, tv_look, tv_delete;
        public View view;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            tv_content = (TextView) itemView.findViewById(R.id.self_task_item_content);
            tv_id = (TextView) itemView.findViewById(R.id.self_task_item_id);
            tv_time = (TextView) itemView.findViewById(R.id.self_task_item_time);
        }
    }
}
