package com.vector.com.card.view;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.vector.com.card.R;
import com.vector.com.card.dao.MemoDao;
import com.vector.com.card.dao.NoticeDao;
import com.vector.com.card.dao.ScoreDao;
import com.vector.com.card.dao.SignDao;
import com.vector.com.card.dao.UserDao;
import com.vector.com.card.domian.Memo;
import com.vector.com.card.domian.Notice;
import com.vector.com.card.domian.Score;
import com.vector.com.card.domian.Sign;
import com.vector.com.card.domian.User;
import com.vector.com.card.service.TimeService;
import com.vector.com.card.utils.LineChart;
import com.vector.com.card.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class HomeActivity extends BaseActivity {
    public static final String action = "action.timeUpdate";
    private Intent intentService;
    private MyBroadcastReceiver broadcastReceiver;
    private IntentFilter filter;
    private LineChart lineChart;
    private TextView tv_broadcast;
    private TextView tv_qiandaozongshu, tv_score, tv_tip;
    private ImageView iv_setting;
    private ImageView iv_qiandao, iv_psersonal;
    private ImageView iv_task, iv_memo, tv_pop, iv_more;
    private ImageView iv_manage;
    private LinearLayout ll_notice;
    private ImageView iv_notice_icon_1, iv_notice_icon_2;
    private TextView tv_notice_content_1, tv_notice_content_2;
    private TextView tv_notice_time_1, tv_notice_time_2;
    private long userid;
    private boolean isSigned = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_home);

        intentService = new Intent(this, TimeService.class);
        iv_psersonal = (ImageView) findViewById(R.id.home_personal);
        iv_setting = (ImageView) findViewById(R.id.home_setting);
        iv_more = (ImageView) findViewById(R.id.home_chart_title_more);
        tv_pop = (ImageView) findViewById(R.id.home_pop);
        lineChart = (LineChart) findViewById(R.id.home_lineChart);
        tv_score = (TextView) findViewById(R.id.home_score);
        iv_qiandao = (ImageView) findViewById(R.id.home_sign);
        iv_manage = (ImageView) findViewById(R.id.home_manage);
        iv_task = (ImageView) findViewById(R.id.home_task);
        iv_memo = (ImageView) findViewById(R.id.home_memo);
        tv_broadcast = (TextView) findViewById(R.id.home_broadcast);
        tv_qiandaozongshu = (TextView) findViewById(R.id.home_qiandaozongshu);
        tv_score = (TextView) findViewById(R.id.home_score);
        tv_tip = (TextView) findViewById(R.id.home_tip);

        ll_notice = (LinearLayout) findViewById(R.id.home_notice);
        iv_notice_icon_1 = (ImageView) findViewById(R.id.home_notice_icon_1);
        iv_notice_icon_2 = (ImageView) findViewById(R.id.home_notice_icon_2);
        tv_notice_content_1 = (TextView) findViewById(R.id.home_notice_content_1);
        tv_notice_content_2 = (TextView) findViewById(R.id.home_notice_content_2);
        tv_notice_time_1 = (TextView) findViewById(R.id.home_notice_time_1);
        tv_notice_time_2 = (TextView) findViewById(R.id.home_notice_time_2);

        userid = Utils.getUserId(this);

        init();

        ll_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(NoticeActivity.class);
            }
        });

        iv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(ManageActivity.class);
            }
        });

        iv_psersonal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(PersonalActivity.class);
            }
        });

        iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.vibrate(HomeActivity.this);
//                View popup = getLayoutInflater().inflate(R.layout.self_popup_window_chart, null);
//                PopupWindow popupWindow = new PopupWindow(popup, Utils.dp2pix(HomeActivity.this, 150), Utils.dp2pix(HomeActivity.this, 60));
//                popupWindow.setOutsideTouchable(true);
//                popupWindow.setFocusable(true);
//                popupWindow.showAsDropDown(v, -200, -Utils.dp2pix(HomeActivity.this, 70));
            }
        });

        tv_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(PlayActivity.class);
            }
        });

        iv_manage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(TaskActivity.class);
            }
        });

        iv_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(DailyActivity.class);
            }
        });

        iv_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNewActivity(MemoActivity.class);
            }
        });

        iv_qiandao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSigned) {
                    Utils.vibrate(HomeActivity.this);
                    isSigned = true;
                    showSignedInfo(false);
                    setContinueSignedDays();
                } else {
                    startNewActivity(SignActivity.class);
                }
            }
        });

        tv_pop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.vibrate(HomeActivity.this);
                int[] images = {R.drawable.popup01, R.drawable.popup02, R.drawable.popup03};

                View popup = getLayoutInflater().inflate(R.layout.self_popup_window, null);
                ImageView imageView = (ImageView) popup.findViewById(R.id.self_popup_window_image);
                imageView.setImageResource(images[(int) (new Random().nextInt(images.length))]);
                Animation animation = AnimationUtils.loadAnimation(HomeActivity.this, R.anim.popup_window);
                popup.setAnimation(animation);
                //背景图片宽600px(200dp),高900px(300dp),圆角17px
                PopupWindow popupWindow = new PopupWindow(popup, Utils.dp2pix(HomeActivity.this, 200), Utils.dp2pix(HomeActivity.this, 300));
                popupWindow.setOutsideTouchable(true);
                popupWindow.update();
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                popup.startAnimation(animation);

            }
        });
    }

    @Override
    public void onBackPressed() {
        removeAll();
    }

    @Override
    protected void onRestart() {
        init();
        super.onRestart();
    }

    public void init() {
        setSignedStatus();
        showSignedInfo(true);
        setContinueSignedDays();
        initTopMarque();
        initNotice();
        initDailyChart();
        registBroadcast();
        startService();
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
            if (signDao.insert(new Sign(String.valueOf(userid)), false) > 0) {
                score += 1;
                scoreDao.insert(new Score(String.valueOf(userid), "签到", getResources().getString(R.string.score_sign), "S"));
                userDao.updateScore(new User(userid, String.valueOf(score)));
                setContinueSignedDays();
                initDailyChart();
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

    public float[] getDailyData() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        ScoreDao scoreDao = new ScoreDao(this);
        Calendar calendar = Calendar.getInstance();
        int today = calendar.get(Calendar.DAY_OF_MONTH);
        String prefix = simpleDateFormat.format(calendar.getTime());
        float[] datas = new float[today];
        for (int i = 1; i < today + 1; i++) {
            datas[i - 1] = scoreDao.queryByUserId(userid, prefix + "-" + ((i < 10) ? ("0" + i) : i));
        }
        return datas;
    }

    public void initDailyChart() {
        float[] data = getDailyData();
        float[] data2 = data.clone();
        lineChart.setData(data);
        lineChart.setyMaxValue(Utils.getMaxValue(data2) + 3);
        lineChart.setyLines((int) (Utils.getMaxValue(data2) + 3));
        lineChart.setDataDotColor(Color.RED);
        lineChart.setLineColor(Color.LTGRAY);
        lineChart.setCircleWidth(7);
        lineChart.setxLines(Utils.getMaxdaysOfThisMonth());
        lineChart.setxMaxValue(Utils.getMaxdaysOfThisMonth());
        lineChart.setAxisLabelSize(30);
        lineChart.setxLabel("日");
        lineChart.setyLabel("积分值");
        lineChart.setxWidth(2);
        lineChart.setyWidth(2);
        lineChart.invalidate();
    }

    public void initNotice() {
        List<Notice> list = new NoticeDao(this).queryAllByUserId(userid);
        Notice notice;
        if (list.size() == 1) {
            notice = list.get(0);
            if (notice.getStatus().equals("0")) {
                iv_notice_icon_1.setVisibility(View.VISIBLE);
            } else {
                iv_notice_icon_1.setVisibility(View.GONE);
            }
            tv_notice_content_1.setText(notice.getContent());
            tv_notice_time_1.setText(Utils.getFormatedTime(notice.getTime()));
        } else if (list.size() >= 2) {
            notice = list.get(0);
            if (notice.getStatus().equals("0")) {
                iv_notice_icon_1.setVisibility(View.VISIBLE);
            } else {
                iv_notice_icon_1.setVisibility(View.GONE);
            }
            tv_notice_content_1.setText(notice.getContent());
            tv_notice_time_1.setText(Utils.getFormatedTime(notice.getTime()));

            notice = list.get(1);
            if (notice.getStatus().equals("0")) {
                iv_notice_icon_2.setVisibility(View.VISIBLE);
            } else {
                iv_notice_icon_2.setVisibility(View.GONE);
            }
            tv_notice_content_2.setText(notice.getContent());
            tv_notice_time_2.setText(Utils.getFormatedTime(notice.getTime()));
        } else {
            iv_notice_icon_1.setVisibility(View.GONE);
            iv_notice_icon_2.setVisibility(View.GONE);
            tv_notice_content_1.setText("无消息");
            tv_notice_content_2.setText("无消息");
        }
    }

    public void startService() {
        stopService();
        startService(intentService);
    }

    public void stopService() {
        stopService(intentService);
    }

    public void registBroadcast() {
        if (broadcastReceiver == null) {
            broadcastReceiver = new MyBroadcastReceiver();
        }
        filter = new IntentFilter(action);
        this.registerReceiver(broadcastReceiver, filter);
    }

    public void unRegistBroadcast() {
        this.unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        stopService();
        unRegistBroadcast();
        super.onDestroy();
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            sendBroadcast(new Intent("android.appwidget.action.APPWIDGET_UPDATE"));
            initNotice();
        }
    }

}


