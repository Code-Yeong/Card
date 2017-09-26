package com.vector.com.card.utils;

import android.app.Application;

/**
 * Created by Administrator on 2017/9/24.
 */
public class UserApplication extends Application {
    private String userId;
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Application getInstance() {
        return instance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
