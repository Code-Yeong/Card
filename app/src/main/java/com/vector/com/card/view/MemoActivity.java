package com.vector.com.card.view;

import android.app.Dialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.vector.com.card.R;
import com.vector.com.card.adapter.RecyclerViewAdapterForMemo;
import com.vector.com.card.dao.MemoDao;
import com.vector.com.card.dao.NoticeDao;
import com.vector.com.card.domian.Memo;
import com.vector.com.card.domian.Notice;
import com.vector.com.card.utils.MyRecyclerViewForMemo;
import com.vector.com.card.utils.SpaceItemDecoration;
import com.vector.com.card.utils.Utils;

import java.util.List;

public class MemoActivity extends BaseActivity {

    private MyRecyclerViewForMemo recyclerView;
    private long userid;
    private boolean isStar = false;
    private MemoDao memoDao;
    private ImageView iv_switch;
    private RecyclerViewAdapterForMemo recyclerViewAdapterForMemo;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_memo);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userid = Utils.getUserId(this);
        memoDao = new MemoDao(this);
        iv_empty = (ImageView) findViewById(R.id.memo_empty);
        recyclerView = (MyRecyclerViewForMemo) findViewById(R.id.memo_recycler);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.memo_add);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration(10, 10, 10, 20));
        init();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.vibrate(MemoActivity.this);
                View view = getLayoutInflater().inflate(R.layout.self_memo_create_item_dialog, null);
                final Dialog dialog = new AlertDialog.Builder(MemoActivity.this).setView(view).show();
                final ImageView iv_close = (ImageView) view.findViewById(R.id.self_memo_create_item_dialog_close);
                iv_switch = (ImageView) view.findViewById(R.id.self_memo_create_item_dialog_switch);
                final Button btn_submit = (Button) view.findViewById(R.id.self_memo_create_item_dialog_submit);
                final TextInputEditText et_content = (TextInputEditText) view.findViewById(R.id.self_memo_create_item_dialog_content);

                if (isStar) {
                    iv_switch.setImageResource(R.drawable.icon_switch_right);
                } else {
                    iv_switch.setImageResource(R.drawable.icon_switch_left);
                }

                iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.vibrate(MemoActivity.this);
                        dialog.dismiss();
                    }
                });

                iv_switch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.vibrate(MemoActivity.this);
                        if (isStar) {
                            iv_switch.setImageResource(R.drawable.icon_switch_left);
                            isStar = false;
                        } else {
                            iv_switch.setImageResource(R.drawable.icon_switch_right);
                            isStar = true;
                        }
                    }
                });

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.vibrate(MemoActivity.this);
                        String content = et_content.getText().toString();
                        if (!TextUtils.isEmpty(content)) {
                            Memo memo = new Memo(String.valueOf(userid), content, isStar ? "1" : "0");
                            if (memoDao.insert(memo) > 0) {
                                new NoticeDao(MemoActivity.this).insert(new Notice(String.valueOf(userid), "备忘事件创建成功", "C"));
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

    public List<Memo> getData() {
        List<Memo> list = memoDao.queryBuUserId(userid);
        if (list.size() > 0) {
            iv_empty.setVisibility(View.GONE);
        } else {
            iv_empty.setVisibility(View.VISIBLE);
        }
        return list;
    }

    public void init() {
        recyclerViewAdapterForMemo = new RecyclerViewAdapterForMemo(this, getData());
        recyclerView.setAdapter(recyclerViewAdapterForMemo);
    }
}
