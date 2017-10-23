package com.vector.com.card.view;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vector.com.card.R;
import com.vector.com.card.adapter.RecyclerViewAdapterForTask;
import com.vector.com.card.dao.DetailDao;
import com.vector.com.card.dao.NoticeDao;
import com.vector.com.card.dao.TaskDao;
import com.vector.com.card.domian.Detail;
import com.vector.com.card.domian.Notice;
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

        iv_empty = (ImageView) findViewById(R.id.home_empty);
        myRecyclerViewForTask = (MyRecyclerViewForTask) findViewById(R.id.task_recyclerView);
        myRecyclerViewForTask.setListener(new MyRecyclerViewForTask.onGetListener() {
            @Override
            public void onClickDelete(View view, long id) {
                Utils.vibrate(TaskActivity.this);
                TaskDao taskDao = new TaskDao(TaskActivity.this);
                if (taskDao.delete(id) > 0) {
                    Utils.showMsg(getWindow().getDecorView(), "删除成功");
                    init();
                }
            }

            @Override
            public void onClickAbort(View view, long id) {
                Utils.vibrate(TaskActivity.this);
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
            public void onClickLook(View view, long id, String startTime, String stopTime) {
                Utils.vibrate(TaskActivity.this);
                int finishCount = 0;
                View v = getLayoutInflater().inflate(R.layout.self_task_dialog, null);
                ImageView iv_close = (ImageView) v.findViewById(R.id.self_task_dialog_close);
                LinearLayout layoutContent = (LinearLayout) v.findViewById(R.id.self_task_dialog_content);
                TextView tv_time = (TextView) v.findViewById(R.id.self_task_dialog_period);
                TextView tv_finish = (TextView) v.findViewById(R.id.self_task_dialog_finish);
                tv_time.setText(startTime + "---->" + (stopTime != null ? stopTime : "--"));
                List<Detail> list = getDetails(id, startTime, stopTime);
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
                            finishCount++;
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
                            finishCount++;
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
                tv_finish.setText(finishCount + "次(共" + list.size() + "次)");
                final Dialog dialog = new AlertDialog.Builder(TaskActivity.this).setView(v).show();
                iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.vibrate(TaskActivity.this);
                        dialog.dismiss();
                    }
                });
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.vibrate(TaskActivity.this);
                LayoutInflater layoutInflater = getLayoutInflater();
                View v = layoutInflater.inflate(R.layout.self_task_create, null);
                final TextInputEditText et_content = (TextInputEditText) v.findViewById(R.id.self_task_create_content);
                final ImageView iv_close = (ImageView) v.findViewById(R.id.self_task_create_close);
                final Button btn_submit = (Button) v.findViewById(R.id.self_task_create_submit);
                final TextView tv_startTime = (TextView) v.findViewById(R.id.self_task_create_starttime);
                final Dialog dialog = new AlertDialog.Builder(TaskActivity.this).setView(v).show();
                iv_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                tv_startTime.setText(Utils.getCurrentDate());
                tv_startTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.vibrate(TaskActivity.this);
                        LayoutInflater layoutInflater = getLayoutInflater();
                        View v2 = layoutInflater.inflate(R.layout.self_date_picker, null);
                        final Dialog dialog2 = new AlertDialog.Builder(TaskActivity.this).setView(v2).show();
                        final ImageView iv_close = (ImageView) v2.findViewById(R.id.self_date_picker_close);
                        final Button btn_submit = (Button) v2.findViewById(R.id.self_date_picker_submit);
                        final CalendarView cv = (CalendarView) v2.findViewById(R.id.self_date_picker_picker);
                        iv_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Utils.vibrate(TaskActivity.this);
                                dialog2.dismiss();
                            }
                        });

                        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                                Utils.vibrate(TaskActivity.this);
                                tv_startTime.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                            }
                        });

                        btn_submit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Utils.vibrate(TaskActivity.this);
                                dialog2.dismiss();
                            }
                        });
                    }
                });

                btn_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.vibrate(TaskActivity.this);
                        String content = et_content.getText().toString().trim();
                        String startTime = tv_startTime.getText().toString().trim();
                        long id = Utils.getUserId(TaskActivity.this);
                        if (content.length() > 0) {
                            TaskDao taskDao = new TaskDao(TaskActivity.this);
                            long result = taskDao.insert(new Task(String.valueOf(id), content, startTime));
                            if (result > 0) {
                                new NoticeDao(TaskActivity.this).insert(new Notice(String.valueOf(id), "成功创建学习任务", "E"));
                                et_content.setText("");
                                recyclerViewAdapterForTask = new RecyclerViewAdapterForTask(getData());
                                myRecyclerViewForTask.setAdapter(recyclerViewAdapterForTask);
                                Utils.showMsg(v.getRootView(), "添加成功");
                            } else {
                                Utils.showMsg(v, "添加失败");
                            }
                        } else {
                            Utils.showMsg(v, "请正确填写任务内容");
                        }
                    }
                });
            }
        });

        init();
    }

    public void init() {
        list = getData();
        if (list.size() > 0) {
            iv_empty.setVisibility(View.GONE);
        } else {
            iv_empty.setVisibility(View.VISIBLE);
        }
        layoutManager = new LinearLayoutManager(this);
        myRecyclerViewForTask.setLayoutManager(layoutManager);
        recyclerViewAdapterForTask = new RecyclerViewAdapterForTask(list);
        myRecyclerViewForTask.setAdapter(recyclerViewAdapterForTask);
        myRecyclerViewForTask.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    public List<Task> getData() {
        TaskDao taskDao = new TaskDao(this);
        return taskDao.queryByUserId(Utils.getUserId(TaskActivity.this));
    }

    public List<Detail> getDetails(long id, String startTime, String stopTime) {
        DetailDao detailDao = new DetailDao(this);
        return detailDao.queryByTaskId(id, startTime, stopTime);
    }
}
