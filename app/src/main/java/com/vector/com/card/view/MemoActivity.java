package com.vector.com.card.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.vector.com.card.R;
import com.vector.com.card.adapter.RecyclerViewAdapterForMemo;
import com.vector.com.card.database.MemoDao;
import com.vector.com.card.domian.Memo;
import com.vector.com.card.utils.MyRecyclerViewForMemo;
import com.vector.com.card.utils.Utils;

import java.util.List;

public class MemoActivity extends BaseActivity {

    private MyRecyclerViewForMemo recyclerView;
    private long userid;
    private MemoDao memoDao;
    private RecyclerViewAdapterForMemo recyclerViewAdapterForMemo;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_memo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userid = getSharedPreferences("userInfo", Activity.MODE_PRIVATE).getLong("id", 0);
        memoDao = new MemoDao(this);
        recyclerView = (MyRecyclerViewForMemo) findViewById(R.id.memo_recycler);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.memo_add);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        init();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            boolean isStar = false;

            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.self_memo_create_item_dialog, null);
                final Dialog dialog = new AlertDialog.Builder(MemoActivity.this).setView(view).show();
                final Drawable left = v.getContext().getResources().getDrawable(R.drawable.icon_switch_left);
                final Drawable right = v.getContext().getResources().getDrawable(R.drawable.icon_switch_right);
                final ImageView iv_close = (ImageView) view.findViewById(R.id.self_memo_create_item_dialog_close);
                final ImageView iv_switch = (ImageView) view.findViewById(R.id.self_memo_create_item_dialog_switch);
                final Button btn_submit = (Button) view.findViewById(R.id.self_memo_create_item_dialog_submit);
                final TextInputEditText et_content = (TextInputEditText) view.findViewById(R.id.self_memo_create_item_dialog_content);

                iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                iv_switch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isStar) {
                            iv_switch.setImageDrawable(right);
                            isStar = false;
                        } else {
                            iv_switch.setImageDrawable(left);
                            isStar = true;
                        }
                    }
                });

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String content = et_content.getText().toString();
                        if (!TextUtils.isEmpty(content)) {
                            Memo memo = new Memo(String.valueOf(userid), content, isStar ? "0" : "1");
                            if (memoDao.insert(memo) > 0) {
                                Utils.showMsg(v, "添加成功");
                                et_content.setText("");
                                init();
                            } else {
                                Utils.showMsg(v, "添加失败");
                            }
                        }
                    }
                });

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            removeActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    public List<Memo> getData() {
        List<Memo> list = memoDao.queryBuUserId(userid);
        return list;
    }

    public void init() {
        recyclerViewAdapterForMemo = new RecyclerViewAdapterForMemo(this, getData());
        recyclerView.setAdapter(recyclerViewAdapterForMemo);
    }
}
