package com.vector.com.card.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vector.com.card.R;
import com.vector.com.card.database.MemoDao;
import com.vector.com.card.database.NoticeDao;
import com.vector.com.card.domian.Memo;
import com.vector.com.card.domian.Notice;
import com.vector.com.card.utils.Utils;

import java.util.List;

/**
 * Created by Administrator on 2017/8/26.
 */
public class RecyclerViewAdapterForMemo extends RecyclerView.Adapter {

    private List<Memo> list;
    private Context context;
    private MemoDao memoDao;

    public RecyclerViewAdapterForMemo(Context context, List<Memo> list) {
        this.context = context;
        this.list = list;
        memoDao = new MemoDao(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.self_memo_item, null);
        MyHolder holder = new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyHolder myHolder = (MyHolder) holder;
        final Drawable star_unactived = context.getResources().getDrawable(R.drawable.icon_star_unactived);
        final Drawable star_actived = context.getResources().getDrawable(R.drawable.icon_star_actived);
        myHolder.tv_content.setText(list.get(position).getContent());
        myHolder.tv_time.setText(Utils.getFormatDate(list.get(position).getTime()));
        final String flag = list.get(position).getFlag();
        final long id = list.get(position).getId();
        if (flag.equals("0")) {
            myHolder.iv_star.setImageDrawable(star_unactived);
        } else {
            myHolder.iv_star.setImageDrawable(star_actived);
        }

        ((MyHolder) holder).iv_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag.equals("0")) {
                    //flag置为1
                    if (setFlag(id, 1)) {
                        list.get(position).setFlag("1");
                        myHolder.iv_star.setImageDrawable(star_actived);
                        notifyDataSetChanged();
                    }
                } else {
                    //falg置为0
                    if (setFlag(id, 0)) {
                        list.get(position).setFlag("0");
                        myHolder.iv_star.setImageDrawable(star_unactived);
                        notifyDataSetChanged();
                    }
                }
            }
        });

        ((MyHolder) holder).iv_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delete(id)) {
                    new NoticeDao(context).insert(new Notice(String.valueOf(Utils.getUserId(context)), "备忘事件删除成功", "C"));
                    list.remove(position);
                    notifyDataSetChanged();
                }
            }
        });

        ((MyHolder) holder).iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //修改该条目
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public ImageView iv_delete, iv_edit, iv_star;
        public TextView tv_content, tv_time;

        public MyHolder(View itemView) {
            super(itemView);
            iv_delete = (ImageView) itemView.findViewById(R.id.self_memo_item_delete);
            iv_edit = (ImageView) itemView.findViewById(R.id.self_memo_item_edit);
            iv_star = (ImageView) itemView.findViewById(R.id.self_memo_item_star);
            tv_content = (TextView) itemView.findViewById(R.id.self_memo_item_content);
            tv_time = (TextView) itemView.findViewById(R.id.self_memo_item_time);
        }
    }

    public boolean setFlag(long id, int value) {
        Memo memo = new Memo(id, String.valueOf(value));
        return memoDao.setFlag(memo) > 0;
    }

    public boolean delete(long id) {
        return memoDao.delete(id) > 0;
    }
}
