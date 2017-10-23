package com.vector.com.card.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.vector.com.card.R;
import com.vector.com.card.dao.ScoreDao;
import com.vector.com.card.dao.SignDao;
import com.vector.com.card.dao.UserDao;
import com.vector.com.card.domian.Score;
import com.vector.com.card.domian.Sign;
import com.vector.com.card.domian.User;
import com.vector.com.card.utils.SignCalendar;
import com.vector.com.card.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class SignActivity extends BaseActivity {

    long userid;
    private int year, month;
    private SignDao signDao;
    private TextView tv_date, tv_show_date, tv_show_count;
    private SignCalendar signCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addActivity(this);
        setContentView(R.layout.activity_sign);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        signDao = new SignDao(this);
        userid = Utils.getUserId(this);
        tv_date = (TextView) findViewById(R.id.sign_date);
        tv_show_count = (TextView) findViewById(R.id.sign_show_count);
        tv_show_date = (TextView) findViewById(R.id.sign_show_date);
        signCalendar = (SignCalendar) findViewById(R.id.sign_calendar);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 EEEE");
        Calendar calendar = Calendar.getInstance();

        tv_show_date.setText(sdf.format(calendar.getTime()));
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
        tv_date.setText(year + "年" + month + "月");

        signCalendar.setListener(new SignCalendar.Listener() {
            @Override
            public void onSelect(int selectedDay) {
                Utils.vibrate(SignActivity.this);
                SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
                Calendar c = Calendar.getInstance();
                c.set(Calendar.DAY_OF_MONTH, selectedDay);
                final String time = s.format(c.getTime()) + " 00:00:00";
                new AlertDialog.Builder(SignActivity.this)
                        .setTitle("提示")
                        .setMessage("是否补签" + time + "(补签消耗10个积分值)？")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.vibrate(SignActivity.this);
                                dialog.dismiss();
                                SignDao signDao = new SignDao(SignActivity.this);
                                UserDao userDao = new UserDao(SignActivity.this);
                                ScoreDao scoreDao = new ScoreDao(SignActivity.this);
                                String score_str = userDao.getSignedScore(userid);
                                int score = Integer.parseInt(score_str);
                                int loss = Integer.parseInt(getResources().getString(R.string.score_delay_sign));
                                if (score >= loss && signDao.insert(new Sign(String.valueOf(userid), time), true) > 0) {
                                    score -= loss;
                                    scoreDao.insert(new Score(String.valueOf(userid), "补签到", "-" + loss, "S"));
                                    userDao.updateScore(new User(userid, String.valueOf(score)));
                                    initCalendar(year, month);
                                } else {
                                    Utils.showMsg(getWindow().getDecorView(), "积分余额不足!");
                                }

                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Utils.vibrate(SignActivity.this);
                                dialog.dismiss();
                            }
                        }).show();
            }

            @Override
            public void onScroll(int location) {
                if (location > 0) {
                    showPreviousMonth();
                } else {
                    showNextMonth();
                }
            }
        });

        initCalendar(year, month);
    }

    @Override
    protected void onRestart() {
        initCalendar(year, month);
        super.onRestart();
    }

    public void previousMonth(View v) {
        showPreviousMonth();
    }

    public void nextMonth(View v) {
        showNextMonth();
    }

    public void initCalendar(int year, int month) {
        List<Integer> dates = signDao.getDates(String.valueOf(userid), year, month);
        tv_show_count.setText(dates.size() + "");
        signCalendar.addDate(dates);
    }

    public void showNextMonth() {
        String date = tv_date.getText().toString().trim();
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, date.length() - 1)) + 1;
        if (month > 12) {
            year++;
            month = 1;
        }
        signCalendar.setDate(year, month);
        tv_date.setText(year + "年" + month + "月");
        initCalendar(year, month);
    }

    public void showPreviousMonth() {
        String date = tv_date.getText().toString().trim();
        int year = Integer.parseInt(date.substring(0, 4));
        int month = Integer.parseInt(date.substring(5, date.length() - 1)) - 1;
        if (month < 1) {
            month = 12;
            year--;
        }
        signCalendar.setDate(year, month);
        tv_date.setText(year + "年" + month + "月");
        initCalendar(year, month);
    }
}
