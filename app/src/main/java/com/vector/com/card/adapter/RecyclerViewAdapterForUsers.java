package com.vector.com.card.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vector.com.card.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/25.
 */
public class RecyclerViewAdapterForUsers extends RecyclerView.Adapter {

    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onNameClicked(String name);

        void onDeleteClicked(int position);
    }

    List<Map<String, String>> list;

    public RecyclerViewAdapterForUsers(List<Map<String, String>> list) {
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.self_single_user_item, null);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyHolder h = (MyHolder) holder;
        h.tv_name.setText(list.get(position).get("name"));
        h.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("info", "position:" + position);
                listener.onNameClicked(list.get(position).get("name"));
            }
        });
        h.iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteClicked(position);
//                list.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setListener(OnItemClickListener l) {
        this.listener = l;

    }

    private class MyHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public ImageView iv_delete;

        public MyHolder(View itemView) {
            super(itemView);

            tv_name = (TextView) itemView.findViewById(R.id.self_login_uer_select_name);
            iv_delete = (ImageView) itemView.findViewById(R.id.self_login_uer_select_delete);
        }
    }
}
