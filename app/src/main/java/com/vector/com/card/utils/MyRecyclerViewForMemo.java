package com.vector.com.card.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017/8/26.
 */
public class MyRecyclerViewForMemo extends RecyclerView {
    public MyRecyclerViewForMemo(Context context) {
        this(context, null);
    }

    public MyRecyclerViewForMemo(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyRecyclerViewForMemo(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
