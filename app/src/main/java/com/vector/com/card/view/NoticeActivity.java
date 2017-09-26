package com.vector.com.card.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.vector.com.card.R;
import com.vector.com.card.adapter.RecyclerViewAdapterForNotice;
import com.vector.com.card.database.NoticeDao;
import com.vector.com.card.domian.Notice;
import com.vector.com.card.utils.SpaceItemDecoration;
import com.vector.com.card.utils.Utils;

import java.util.List;

public class NoticeActivity extends BaseActivity {

    private long userid;
    private NoticeDao noticeDao;
    private RecyclerView recyclerView;
    private RecyclerViewAdapterForNotice recyclerViewAdapterForNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_notice);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userid = Utils.getUserId(this);
        noticeDao = new NoticeDao(this);
        recyclerView = (RecyclerView) findViewById(R.id.notice_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpaceItemDecoration(0, 0, 0, 10));

        initPage();
    }

    private void initPage() {
        recyclerViewAdapterForNotice = new RecyclerViewAdapterForNotice(getData());
        recyclerView.setAdapter(recyclerViewAdapterForNotice);
    }

    private List<Notice> getData() {
        List<Notice> list = noticeDao.queryAllByUserId(userid);
        return list;
    }
}
