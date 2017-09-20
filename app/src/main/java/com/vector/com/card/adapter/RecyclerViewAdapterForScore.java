package com.vector.com.card.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vector.com.card.R;
import com.vector.com.card.domian.Score;

import java.util.List;

/**
 * Created by Administrator on 2017/9/19.
 */
public class RecyclerViewAdapterForScore extends RecyclerView.Adapter {

    List<Score> list;

    public RecyclerViewAdapterForScore(List<Score> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.self_score_item, null);
        MyViewHolder holder = new MyViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder h = (MyViewHolder) holder;
        h.tv_content.setText(list.get(position).getContent());
        h.tv_id.setText(String.valueOf(position + 1));
        h.tv_time.setText(list.get(position).getTime());
        h.tv_score.setText("+" + list.get(position).getScore());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_id, tv_content, tv_time, tv_score;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_content = (TextView) itemView.findViewById(R.id.self_score_item_content);
            tv_id = (TextView) itemView.findViewById(R.id.self_score_item_id);
            tv_score = (TextView) itemView.findViewById(R.id.self_score_item_score);
            tv_time = (TextView) itemView.findViewById(R.id.self_score_item_time);
        }
    }
}