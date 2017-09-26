package com.vector.com.card.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.vector.com.card.R;
import com.vector.com.card.database.DetailDao;
import com.vector.com.card.database.NoticeDao;
import com.vector.com.card.database.ScoreDao;
import com.vector.com.card.database.UserDao;
import com.vector.com.card.domian.Detail;
import com.vector.com.card.domian.Notice;
import com.vector.com.card.domian.Score;
import com.vector.com.card.domian.Sign;
import com.vector.com.card.domian.Task;
import com.vector.com.card.domian.User;
import com.vector.com.card.utils.Utils;
import com.vector.com.card.view.DailyActivity;
import com.vector.com.card.view.HomeActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/8/21.
 */
public class RecyclerViewAdapterForDaily extends RecyclerView.Adapter {
    private List<Detail> list;
    private Context context;

    public RecyclerViewAdapterForDaily(Context context, List<Detail> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.self_daily_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.tv_content.setText(list.get(position).getContent());
        String time = list.get(position).getTime();
        boolean isFinished = time.equals("FINISHED") ? true : false;
        if (isFinished) {
            myViewHolder.chk_unfinished.setVisibility(View.INVISIBLE);
            myViewHolder.iv_finished.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.chk_unfinished.setVisibility(View.VISIBLE);
            myViewHolder.iv_finished.setVisibility(View.INVISIBLE);
        }
        myViewHolder.chk_unfinished.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DetailDao detailDao = new DetailDao(buttonView.getContext());
                    long result = detailDao.insert(new Detail(String.valueOf(list.get(position).getTask())));
                    if (result > 0) {
                        Utils.vibrate(context);
                        myViewHolder.chk_unfinished.setVisibility(View.INVISIBLE);
                        myViewHolder.iv_finished.setVisibility(View.VISIBLE);

                        long userid = Utils.getUserId(context);
                        UserDao userDao = new UserDao(buttonView.getContext());
                        ScoreDao scoreDao = new ScoreDao(buttonView.getContext());
                        String score_str = userDao.getSignedScore(userid);
                        int score = Integer.parseInt(score_str);
                        score += 2;
                        (new NoticeDao(context)).insert(new Notice(String.valueOf(userid), "恭喜您完成了学习任务", "B"));
                        scoreDao.insert(new Score(String.valueOf(userid), "完成日常任务", context.getResources().getString(R.string.score_daily), "D"));
                        userDao.updateScore(new User(userid, String.valueOf(score)));
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_content;
        public ImageView iv_finished;
        public CheckBox chk_unfinished;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_content = (TextView) itemView.findViewById(R.id.self_daily_content);
            iv_finished = (ImageView) itemView.findViewById(R.id.self_daily_finished);
            chk_unfinished = (CheckBox) itemView.findViewById(R.id.self_daily_unfinished);
        }
    }
}
