package com.vector.com.card.view;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.vector.com.card.R;
import com.vector.com.card.adapter.RecyclerViewAdapterForScore;
import com.vector.com.card.dao.ScoreDao;
import com.vector.com.card.domian.Score;
import com.vector.com.card.utils.Utils;

import java.util.List;

public class ScoreActivity extends BaseActivity {

    private long userid;
    private ScoreDao scoreDao;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterForScore recyclerViewAdapterForScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_score);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userid = Utils.getUserId(this);
        scoreDao = new ScoreDao(this);
        iv_empty = (ImageView) findViewById(R.id.score_empty);
        recyclerView = (RecyclerView) findViewById(R.id.score_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        init();
    }

    public void init() {
        recyclerViewAdapterForScore = new RecyclerViewAdapterForScore(getData());
        recyclerView.setAdapter(recyclerViewAdapterForScore);
    }

    public List<Score> getData() {
        List<Score> list = scoreDao.queryBuUserId(userid);
        if (list.size() > 0) {
            iv_empty.setVisibility(View.GONE);
        } else {
            iv_empty.setVisibility(View.VISIBLE);
        }
        return list;
    }
}
