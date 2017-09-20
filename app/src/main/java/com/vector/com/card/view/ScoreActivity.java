package com.vector.com.card.view;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;

import com.vector.com.card.R;
import com.vector.com.card.adapter.RecyclerViewAdapterForScore;
import com.vector.com.card.database.ScoreDao;
import com.vector.com.card.domian.Memo;
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

        userid = getSharedPreferences("userInfo", Activity.MODE_PRIVATE).getLong("id", 0);
        scoreDao = new ScoreDao(this);
        recyclerView = (RecyclerView) findViewById(R.id.score_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Utils.vibrate(ScoreActivity.this);
        if (item.getItemId() == android.R.id.home) {
            removeActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    public void init() {
        recyclerViewAdapterForScore = new RecyclerViewAdapterForScore(getData());
        recyclerView.setAdapter(recyclerViewAdapterForScore);
    }

    public List<Score> getData() {
        List<Score> list = scoreDao.queryBuUserId(userid);
        return list;
    }
}
