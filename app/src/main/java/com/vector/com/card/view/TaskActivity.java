package com.vector.com.card.view;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vector.com.card.R;
import com.vector.com.card.adapter.RecyclerViewAdapterForTask;
import com.vector.com.card.database.DetailDao;
import com.vector.com.card.database.TaskDao;
import com.vector.com.card.domian.Detail;
import com.vector.com.card.domian.Task;
import com.vector.com.card.utils.DialogSingleItem;
import com.vector.com.card.utils.MyRecyclerViewForTask;
import com.vector.com.card.utils.Utils;

import java.util.List;

public class TaskActivity extends BaseActivity {

    private RecyclerViewAdapterForTask recyclerViewAdapterForTask;
    private MyRecyclerViewForTask myRecyclerViewForTask;
    private RecyclerView.LayoutManager layoutManager;
    private List<Task> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_task);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myRecyclerViewForTask = (MyRecyclerViewForTask) findViewById(R.id.task_recyclerView);
        myRecyclerViewForTask.setListener(new MyRecyclerViewForTask.onGetListener() {
            @Override
            public void onClickDelete(View view, long id) {
                TaskDao taskDao = new TaskDao(TaskActivity.this);
                if (taskDao.delete(id) > 0) {
                    Utils.showMsg(getWindow().getDecorView(), "删除成功");
                    init();
                }
            }

            @Override
            public void onClickAbort(View view, long id) {
                TaskDao taskDao = new TaskDao(TaskActivity.this);
                String text = ((TextView) view).getText().toString();
                if (text.equals("终止")) {
                    ((TextView) view).setText("已终止");
                    if (taskDao.updateStatus(new Task(id, "1")) > 0) {
                        Utils.showMsg(getWindow().getDecorView(), "操作成功");
                        init();
                    }
                } else {
                    Utils.showMsg(getWindow().getDecorView(), "任务已终止");
                }
            }

            @Override
            public void onClickLook(View view, long id) {
                View v = getLayoutInflater().inflate(R.layout.self_task_dialog, null);
                ImageView iv_close = (ImageView) v.findViewById(R.id.self_task_dialog_close);
                LinearLayout layoutContent = (LinearLayout) v.findViewById(R.id.self_task_dialog_content);
                List<Detail> list = getDetails(id);
                for (int i = 0; i < list.size(); i++) {
                    DialogSingleItem dialogSingleItem = new DialogSingleItem(TaskActivity.this);
                    if (i == 0) {
                        dialogSingleItem.setLlTopVisibility(View.GONE);
                    }
                    if (list.get(i).getTime().equals(Utils.getCurrentDate())) {
                        dialogSingleItem.setTvCenter(getResources().getDrawable(R.drawable.shape_ring));
                        dialogSingleItem.setTvTime("今天");
                        if (list.get(i).isCompleted()) {
                            dialogSingleItem.setTvStatus("已完成");
                            dialogSingleItem.setTvStatusColor(Color.BLACK);
                            dialogSingleItem.setTvTimeColor(Color.BLACK);
                        } else {
                            dialogSingleItem.setTvStatus("未完成");
                            dialogSingleItem.setTvTimeColor(Color.RED);
                            dialogSingleItem.setTvStatusColor(Color.RED);
                        }
                    } else {
                        dialogSingleItem.setTvTime(Utils.getFormatDate(list.get(i).getTime()));
                        if (list.get(i).isCompleted()) {
                            dialogSingleItem.setTvStatus("已完成");
                            dialogSingleItem.setTvStatusColor(Color.BLACK);
                            dialogSingleItem.setTvTimeColor(Color.BLACK);
                        } else {
                            dialogSingleItem.setTvStatus("未完成");
                            dialogSingleItem.setTvStatusColor(Color.RED);
                            dialogSingleItem.setTvTimeColor(Color.RED);
                        }
                    }

                    if (i == (list.size() - 2)) {
                        dialogSingleItem.setLlBottomColor(Color.LTGRAY);
                    }

                    if (i == (list.size() - 1)) {
                        dialogSingleItem.setLlTopColor(Color.LTGRAY);
                        dialogSingleItem.setLlBottomVisibility(View.GONE);
                        dialogSingleItem.setTvCenter(getResources().getDrawable(R.drawable.shape_circle_gray));
                        dialogSingleItem.setTvTime(Utils.getFormatDate(list.get(i).getTime()));
                        dialogSingleItem.setTvStatusColor(Color.LTGRAY);
                        dialogSingleItem.setTvTimeColor(Color.LTGRAY);
                    }
                    layoutContent.addView(dialogSingleItem);
                }
                final Dialog dialog = new AlertDialog.Builder(TaskActivity.this).setView(v).show();
                iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater = getLayoutInflater();
                View v = layoutInflater.inflate(R.layout.self_task_create, null);
                final TextInputEditText et_content = (TextInputEditText) v.findViewById(R.id.self_task_create_content);
                final ImageView iv_close = (ImageView) v.findViewById(R.id.self_task_create_close);
                final Button btn_submit = (Button) v.findViewById(R.id.self_task_create_submit);
                final TextView tv_tip = (TextView) v.findViewById(R.id.self_task_create_tip);
                final Dialog dialog = new AlertDialog.Builder(TaskActivity.this).setView(v).show();
                iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String content = et_content.getText().toString().trim();
                        long id = getSharedPreferences("userInfo", Activity.MODE_PRIVATE).getLong("id", 0);
                        if (content.length() > 0) {
                            TaskDao taskDao = new TaskDao(TaskActivity.this);
                            long result = taskDao.insert(new Task(String.valueOf(id), content));
                            if (result > 0) {
                                et_content.setText("");
                                recyclerViewAdapterForTask = new RecyclerViewAdapterForTask(getData());
                                myRecyclerViewForTask.setAdapter(recyclerViewAdapterForTask);
                                tv_tip.setText("*添加成功[#" + result + "]");
                                tv_tip.setTextColor(Color.GREEN);
                                tv_tip.setVisibility(View.VISIBLE);
                            } else {
                                tv_tip.setText("*添加失败，请重试");
                                tv_tip.setVisibility(View.VISIBLE);
                            }
                        } else {
                            tv_tip.setText("*请正确填写任务内容");
                            tv_tip.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });

        init();
    }

    public void init() {
        list = getData();
        layoutManager = new LinearLayoutManager(this);
        myRecyclerViewForTask.setLayoutManager(layoutManager);
        recyclerViewAdapterForTask = new RecyclerViewAdapterForTask(list);
        myRecyclerViewForTask.setAdapter(recyclerViewAdapterForTask);
        myRecyclerViewForTask.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            removeActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    public List<Task> getData() {
        TaskDao taskDao = new TaskDao(this);
        return taskDao.queryByUserId(getSharedPreferences("userInfo", Activity.MODE_PRIVATE).getLong("id", 0));
    }

    public List<Detail> getDetails(long id) {
        DetailDao detailDao = new DetailDao(this);
        return detailDao.queryByTaskId(id);
    }
}
