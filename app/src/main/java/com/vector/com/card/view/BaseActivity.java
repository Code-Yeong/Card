package com.vector.com.card.view;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.vector.com.card.domian.Notice;
import com.vector.com.card.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/3.
 */
public class BaseActivity extends AppCompatActivity {

    public ImageView iv_empty;
    public Notice notice;

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
        list.remove(a);
        a.finish();
    }

    /**
     * 返回到第一个界面，重新登录
     */
    protected void backToFirst() {
        for (int i = list.size() - 1; i > 0; i--) {
            Activity a = list.get(i);
            list.remove(i);
            a.finish();
        }
    }

    /**
     * 关闭所有界面，退出登录
     */
    protected void removeAll() {
        for (int i = (list.size() - 1); i > -1; i--) {
            Activity a = list.get(i);
            list.remove(a);
            a.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Utils.vibrate(this);
        if (item.getItemId() == android.R.id.home) {
            removeActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        removeActivity();
    }
}
