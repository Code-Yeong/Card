package com.vector.com.card.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.vector.com.card.R;
import com.vector.com.card.adapter.RecyclerViewAdapterForDaily;
import com.vector.com.card.database.BaseDao;
import com.vector.com.card.database.DetailDao;
import com.vector.com.card.domian.Detail;
import com.vector.com.card.utils.MyRecyclerViewForDaily;
import com.vector.com.card.utils.Utils;

import java.util.List;

public class DailyActivity extends BaseActivity {

    private MyRecyclerViewForDaily recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_daily);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (MyRecyclerViewForDaily) findViewById(R.id.daily_recycler);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            removeActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    public void init() {
        RecyclerViewAdapterForDaily adapterForDaily = new RecyclerViewAdapterForDaily(getData());
        recyclerView.setAdapter(adapterForDaily);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    public List<Detail> getData() {
        DetailDao detailDao = new DetailDao(this);
        return detailDao.queryAllByUserId(getSharedPreferences("userInfo", Activity.MODE_PRIVATE).getLong("id", 0));
    }
}
