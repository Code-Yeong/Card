package com.vector.com.card.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Administrator on 2017/9/27.
 */
public class LotteryPlate extends View {
    private float width, height;
    private int blocks = 8;
    private float centerX, centerY;
    private int rotateBlockCount = 0;
    private float radius;
    private float offset = 1;

    public LotteryPlate(Context context) {
        this(context, null);
    }

    public LotteryPlate(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LotteryPlate(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        centerX = width / 2;
        centerY = height / 2;
        radius = Math.min(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.BLACK);
        RectF rectF = new RectF(0, 0, width, height);
        float angle = 360f / blocks;
        for (int i = 0; i < blocks; i++) {
            if (i == 4) {
                p.setStyle(Paint.Style.FILL);
            } else {
                p.setStyle(Paint.Style.STROKE);
            }
            canvas.drawArc(rectF, (i + 0.5f + offset) * angle, angle, true, p);
        }

        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.FILL);
        canvas.drawCircle(centerX, centerY, 60, p);

        Path path = new Path();
        path.moveTo(centerX - 40, centerY + 40);
        path.lineTo(centerX, centerY + 90);
        path.lineTo(centerX + 40, centerY + 40);
        path.close();
        canvas.drawPath(path, p);

    }

    public void startRotate() {
        Log.i("info", "begin" + getRotateBlockCount());
        for (int i = 1; i <= rotateBlockCount; i++) {
            offset = i;
            for (int k = 0; k < 1000000000; k++) ;
            Log.i("info", "i:" + i);
            invalidate();
        }
    }

    public int getRotateBlockCount() {
        return rotateBlockCount;
    }

    public void setRotateBlockCount(int rotateBlockCount) {
        this.rotateBlockCount = rotateBlockCount;
    }

    public float getOffset() {
        return offset;
    }

    public void setOffset(float offset) {
        this.offset = offset;
        invalidate();
    }

    @Override
    public void setRotation(float rotation) {
        super.setRotation(rotation);
    }

}
