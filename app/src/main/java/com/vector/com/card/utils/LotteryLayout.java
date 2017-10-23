package com.vector.com.card.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2017/9/27.
 */
public class LotteryLayout extends LinearLayout {

    private int width, height;
    private float centerX, centerY;
    private float outerRadius = 0, innerRadius = 0;

    public LotteryLayout(Context context) {
        this(context, null);
    }

    public LotteryLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LotteryLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.RED);
        canvas.drawCircle(width / 2, height / 2, outerRadius, p);
        p.setColor(Color.YELLOW);
        for (int i = 0; i < 24; i++) {
            canvas.drawCircle((float) (centerX + innerRadius * Math.cos((i * 15)/360.0*2*Math.PI)), (float) (centerY - innerRadius * Math.sin((i * 15)/360.0*2*Math.PI)), 10, p);
        }
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        centerX = width / 2;
        centerY = height / 2;
        outerRadius = Math.min(width, height)/2;
        innerRadius = outerRadius - 20;
    }
}
