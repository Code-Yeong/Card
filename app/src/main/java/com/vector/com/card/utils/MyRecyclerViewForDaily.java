package com.vector.com.card.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.vector.com.card.R;
import com.vector.com.card.adapter.RecyclerViewAdapterForTask;

/**
 * Created by Administrator on 2017/8/21.
 */
public class MyRecyclerViewForDaily extends RecyclerView {

    private OnCheckBoxClickedListener listener;
    int xDown, yDown, pos;
    CheckBox checkBox;
    View itemView;
    ImageView imageView;
    Rect mTouchFrame = null;

    public interface OnCheckBoxClickedListener {
        void onCheckLicked(View checkBoxView, View imageView, int position);
    }

    public MyRecyclerViewForDaily(Context context) {
        this(context, null);
    }

    public MyRecyclerViewForDaily(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecyclerViewForDaily(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnClickListener(OnCheckBoxClickedListener l) {
        this.listener = l;
    }
}
