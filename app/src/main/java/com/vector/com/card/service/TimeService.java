package com.vector.com.card.service;

import android.app.IntentService;
import android.content.Intent;

import com.vector.com.card.view.HomeActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeService extends IntentService {
    private Calendar calendar;
    private SimpleDateFormat sdf;

    public TimeService() {
        super("TimeService");
        calendar = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        calendar = null;
        sdf = null;
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent timeUpdate = new Intent(HomeActivity.action);
        while (true) {
            getApplicationContext().sendBroadcast(timeUpdate);
            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
