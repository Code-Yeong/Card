package com.vector.com.card.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/9/12.
 */
public class TriangleView extends TextView {
    private int width, height;

    public TriangleView(Context context) {
        this(context, null);
    }

    public TriangleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TriangleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        width = getWidth();
        height = getHeight();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        float[] pts1 = new float[]{width / 2, 0, 0, height};
        float[] pts2 = new float[]{0, height, width, height};
        canvas.drawLines(pts1, paint);
        canvas.drawLines(pts2, paint);
        super.onDraw(canvas);
    }
}
