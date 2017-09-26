package com.vector.com.card.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.vector.com.card.R;
import com.vector.com.card.database.SignDao;
import com.vector.com.card.utils.SignCalendar;
import com.vector.com.card.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class SignActivity extends BaseActivity {

    long userid;
    private int year,month;
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
            public void onSelect(View v) {

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
