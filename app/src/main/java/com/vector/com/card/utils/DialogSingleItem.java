package com.vector.com.card.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vector.com.card.R;

/**
 * Created by Administrator on 2017/8/21.
 */
public class DialogSingleItem extends LinearLayout {
    private LinearLayout llTop, llBottom;
    private TextView tvTime, tvStatus, tvCenter;

    public DialogSingleItem(Context context) {
        this(context, null);
    }

    public DialogSingleItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DialogSingleItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.self_task_detail_single, this);
        llTop = (LinearLayout) findViewById(R.id.self_task_detail_left_top);
        llBottom = (LinearLayout) findViewById(R.id.self_task_detail_left_bottom);
        tvTime = (TextView) findViewById(R.id.self_task_detail_time);
        tvStatus = (TextView) findViewById(R.id.self_task_detail_status);
        tvCenter = (TextView) findViewById(R.id.self_task_detail_left_center);
    }

    public void setLlTopVisibility(int visibility) {
        llTop.setVisibility(visibility);
    }

    public void setLlTopColor(int color) {
        llTop.setBackgroundColor(color);
    }

    public void setLlBottomVisibility(int visibility) {
        llBottom.setVisibility(visibility);
    }

    public void setLlBottomColor(int color) {
        llBottom.setBackgroundColor(color);
    }

    public void setTvTime(String str) {
        tvTime.setText(str);
    }

    public void setTvTimeColor(int color) {
        tvTime.setTextColor(color);
    }

    public void setTvStatus(String str) {
        tvStatus.setText(str);
    }

    public void setTvStatusColor(int color) {
        tvStatus.setTextColor(color);
    }

    public void setTvCenter(Drawable drawable) {
        tvCenter.setBackground(drawable);
    }
}
