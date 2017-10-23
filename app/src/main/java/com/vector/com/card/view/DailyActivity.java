package com.vector.com.card.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.vector.com.card.R;
import com.vector.com.card.adapter.RecyclerViewAdapterForDaily;
import com.vector.com.card.dao.DetailDao;
import com.vector.com.card.domian.Detail;
import com.vector.com.card.utils.MyRecyclerViewForDaily;
import com.vector.com.card.utils.SpaceItemDecoration;
import com.vector.com.card.utils.Utils;

import java.util.List;

public class DailyActivity extends BaseActivity {

    private long userid;
    private MyRecyclerViewForDaily recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_daily);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        iv_empty = (ImageView) findViewById(R.id.daily_empty);
        recyclerView = (MyRecyclerViewForDaily) findViewById(R.id.daily_recycler);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        userid = Utils.getUserId(this);
        init();
    }

    public void init() {
        RecyclerViewAdapterForDaily adapterForDaily = new RecyclerViewAdapterForDaily(DailyActivity.this, getData());
        recyclerView.setAdapter(adapterForDaily);
        recyclerView.addItemDecoration(new SpaceItemDecoration(0, 0, 0, 10));
    }

    public List<Detail> getData() {
        DetailDao detailDao = new DetailDao(this);
        List<Detail> list = detailDao.queryAllByUserId(userid);
        if (list.size() > 0) {
            iv_empty.setVisibility(View.GONE);
        } else {
            iv_empty.setVisibility(View.VISIBLE);
        }
        return list;
    }
}
