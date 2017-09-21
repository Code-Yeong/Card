package com.vector.com.card.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vector.com.card.R;
import com.vector.com.card.database.MemoDao;
import com.vector.com.card.database.ScoreDao;
import com.vector.com.card.database.SignDao;
import com.vector.com.card.database.UserDao;
import com.vector.com.card.domian.Memo;
import com.vector.com.card.domian.Score;
import com.vector.com.card.domian.Sign;
import com.vector.com.card.domian.User;
import com.vector.com.card.utils.LineChart;
import com.vector.com.card.utils.MyImageView;
import com.vector.com.card.utils.SignCalendar;
import com.vector.com.card.utils.Utils;

import java.util.List;

public class HomeActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private SharedPreferences sharedPreferences;
    private LineChart lineChart;
    private TextView tv_userName, tv_userMark, tv_broadcast;
    private TextView tv_qiandaozongshu, tv_score, tv_scoreAnimation, tv_tip;
    private ImageView iv_editMark, iv_qiandao;
    private MyImageView iv_task, iv_memo;
    private MyImageView iv_manage;
    private RelativeLayout relativeLayout;
    private long userid;
    private boolean isSigned = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_home);

        toolbar = (Toolbar) findViewById(R.id.home_toolbar);

        lineChart = (LineChart) findViewById(R.id.home_lineChart);
        relativeLayout = (RelativeLayout) findViewById(R.id.home_score_ll);
        iv_qiandao = (ImageView) findViewById(R.id.home_pic);
        iv_manage = (MyImageView) findViewById(R.id.home_manage);
        iv_task = (MyImageView) findViewById(R.id.home_task);
        iv_memo = (MyImageView) findViewById(R.id.home_memo);
        tv_broadcast = (TextView) findViewById(R.id.home_broadcast);
        tv_qiandaozongshu = (TextView) findViewById(R.id.home_qiandaozongshu);
        tv_score = (TextView) findViewById(R.id.home_score);
        tv_tip = (TextView) findViewById(R.id.home_tip);

        tv_scoreAnimation = (TextView) findViewById(R.id.home_score_animation);
        sharedPreferences = getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        userid = sharedPreferences.getLong("id", 0);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        tv_userName = (TextView) headerView.findViewById(R.id.nav_header_home_name);
        tv_userMark = (TextView) headerView.findViewById(R.id.nav_header_home_mark);
        iv_editMark = (ImageView) headerView.findViewById(R.id.nav_header_home_edit);
        iv_editMark.setOnClickListener(new EditMarkListener(this));

        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        init();

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.vibrate(HomeActivity.this);
                startActivity(new Intent(HomeActivity.this, ScoreActivity.class));
            }
        });

        iv_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.vibrate(HomeActivity.this);
                startActivity(new Intent(HomeActivity.this, TaskActivity.class));
            }
        });

        iv_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.vibrate(HomeActivity.this);
                startActivity(new Intent(HomeActivity.this, DailyActivity.class));
            }
        });

        iv_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.vibrate(HomeActivity.this);
                startActivity(new Intent(HomeActivity.this, MemoActivity.class));
            }
        });

        iv_qiandao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.vibrate(HomeActivity.this);
                if (!isSigned) {
                    isSigned = true;
                    tv_scoreAnimation.setVisibility(View.VISIBLE);
                    Animation translate = AnimationUtils.loadAnimation(v.getContext(), R.anim.from_bottom_to_top);
                    translate.setFillAfter(true);
                    tv_scoreAnimation.startAnimation(translate);
                    tv_scoreAnimation.setVisibility(View.INVISIBLE);

                    showSignedInfo(false);
                    setContinueSignedDays();
                } else {
                    startActivity(new Intent(HomeActivity.this, SignActivity.class));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Utils.vibrate(HomeActivity.this);
        if (id == R.id.nav_taskManage) {
            startActivity(new Intent(this, TaskActivity.class));
        } else if (id == R.id.nav_daily) {
            startActivity(new Intent(this, DailyActivity.class));
        } else if (id == R.id.nav_note) {
            startActivity(new Intent(this, MemoActivity.class));
        } else if (id == R.id.nav_reLogin) {
            backToFirst();
        } else if (id == R.id.nav_exit) {
            removeAll();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onRestart() {
        init();
        super.onRestart();
    }

    public void init() {
        tv_userName.setText(sharedPreferences.getString("name", null));
        tv_userMark.setText(sharedPreferences.getString("mark", "..."));
        setSignedStatus();
        showSignedInfo(true);
        setContinueSignedDays();
        initTopMarque();

        lineChart.setyMaxValue(10);
        lineChart.setData(new float[]{1, 4, 3, 4, 6, 3, 1, 10, 9, 0});
        lineChart.setDataDotColor(Color.RED);
        lineChart.setLineColor(Color.GREEN);
        lineChart.setCircleWidth(10);
        lineChart.setxLines(15);
        lineChart.setyLines(20);
        lineChart.setxMaxValue(12);
        lineChart.setxLabel("月份");
        lineChart.setyLabel("分数");
        lineChart.invalidate();
    }

    private class EditMarkListener implements View.OnClickListener {

        Context context;

        public EditMarkListener(Context cxt) {
            this.context = cxt;
        }

        @Override
        public void onClick(View view) {
            Utils.vibrate(HomeActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View v = inflater.inflate(R.layout.self_home_edit_mark, null);
            ImageView iv_close = (ImageView) v.findViewById(R.id.self_home_edit_mark_close);
            final TextInputEditText et_content = (TextInputEditText) v.findViewById(R.id.self_home_edit_mark_content);
            Button btn_submit = (Button) v.findViewById(R.id.self_home_edit_mark_submit);
            final TextView tv_tip = (TextView) v.findViewById(R.id.self_home_edit_mark_tip);
            final Dialog dialog = new AlertDialog.Builder(context).setView(v).show();

            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.vibrate(HomeActivity.this);
                    dialog.dismiss();
                }
            });

            btn_submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Utils.vibrate(HomeActivity.this);
                    String content = et_content.getText().toString().trim();
                    if (content.length() == 0) {
                        tv_tip.setText("你还未输入任何内容");
                        tv_tip.setVisibility(View.VISIBLE);
                    } else {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("mark");
                        editor.putString("mark", content);
                        editor.commit();
                        tv_userMark.setText(content);
                        UserDao userDao = new UserDao(context);
                        User user = (new User(userid, sharedPreferences.getString("name", null)
                                , sharedPreferences.getString("time", null), sharedPreferences.getString("password", null)
                                , sharedPreferences.getString("mark", null), sharedPreferences.getString("keepLogin", "0")));
                        Utils.writeToFile(context.getFilesDir().getPath().toString() + "userInfo.txt", user);
                        int result = userDao.update(user);
                        if (result <= 0) {
                            tv_tip.setText("修改失败，请重试");
                            tv_tip.setVisibility(View.VISIBLE);
                        } else {
                            dialog.dismiss();
                        }
                    }
                }
            });
        }
    }

    public void setSignedStatus() {
        isSigned = new SignDao(this).getSignedStatus(userid);
    }

    public void showSignedInfo(boolean isIniting) {
        UserDao userDao = new UserDao(HomeActivity.this);
        SignDao signDao = new SignDao(HomeActivity.this);
        ScoreDao scoreDao = new ScoreDao(HomeActivity.this);
        String score_str = userDao.getSignedScore(userid);
        int score = Integer.parseInt(score_str);

        if (isSigned && !isIniting) {
            if (signDao.insert(new Sign(String.valueOf(userid))) > 0) {
                score += 1;
                scoreDao.insert(new Score(String.valueOf(userid), "签到", "1"));
                userDao.updateScore(new User(userid, String.valueOf(score)));
                setContinueSignedDays();
            }
        }

        if (isSigned) {
            iv_qiandao.setImageDrawable(getResources().getDrawable(R.drawable.icon_yiqiandao));
            tv_tip.setText("已签到");
        }

        tv_score.setText(userDao.getSignedScore(userid));
    }

    public void setContinueSignedDays() {
        SignDao signDao = new SignDao(this);
        int count = signDao.getContinuesSignedCounts(userid);
        tv_qiandaozongshu.setText(count < 2 ? "已签到" + count + "天" : "已连续签到" + count + "天");
    }

    public void initTopMarque() {
        String str = "";
        MemoDao memoDao = new MemoDao(this);
        List<Memo> list = memoDao.queryStarMemoByUserId(userid);
        for (int i = 0; i < list.size(); i++) {
            str += "[" + (i + 1) + "/" + list.size() + "]" + list.get(i).getContent() + "\t ";
        }
        if (str.equals("")) {
            tv_broadcast.setText("没有内容可展示^_^!!");
        } else {
            tv_broadcast.setText(str);
        }
    }
}
