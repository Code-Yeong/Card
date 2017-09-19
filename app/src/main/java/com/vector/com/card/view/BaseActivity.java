package com.vector.com.card.view;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/3.
 */
public class BaseActivity extends AppCompatActivity {
    public static List<Activity> list = new ArrayList<>();

    /**
     * 添加一个新界面
     */
    protected void addActivity(Activity activity) {
        list.add(activity);
    }

    /**
     * 关闭一个界面
     */
    protected void removeActivity() {
        Activity a = list.get(list.size() - 1);
        a.finish();
        list.remove(a);
    }

    /**
     * 返回到第一个界面，重新登录
     */
    protected void backToFirst() {
        for (int i = list.size(); i > 1; i--) {
            Activity a = list.get(i - 1);
            a.finish();
        }
    }

    /**
     * 关闭所有界面，退出登录
     */
    protected void removeAll() {
        for (int i = list.size(); i > 0; i--) {
            list.get(i - 1).finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
