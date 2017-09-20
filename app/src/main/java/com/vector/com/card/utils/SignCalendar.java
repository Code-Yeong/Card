package com.vector.com.card.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2017/9/19.
 */
public class SignCalendar extends View {

    public interface Listener {
        void onSelect(View v);
    }

    private List<Integer> dates;

    int year, month;
    private Context context;
    private Listener listener;

    public void setListener(Listener l) {
        this.listener = l;
    }

    public SignCalendar(Context context) {
        this(context, null);
    }

    public SignCalendar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SignCalendar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        dates = new ArrayList<>();
        this.context = context;
        setToCurrentMonth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        int rowWidth = width / 7;
        int columnHeight = height / 6;

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayPos = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        Paint paint = new Paint();
        Paint paint2 = new Paint();
        paint.setTextSize(60);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.BLACK);

        int x = 0, y = 0, value = 0;
        for (int i = firstDayPos; i < max + firstDayPos; i++) {
            value = i - firstDayPos + 1;
            y = i / 7 + 1;
            x = Math.abs(i - 7 * (y - 1));
            canvas.drawText(String.valueOf(value), (float) (x + 0.5) * rowWidth, (float) (y - 0.25) * columnHeight, paint);
            if (dates.contains(value)) {
                dates.remove((Integer) value);
                int ringWidth = Math.min(rowWidth, columnHeight);
                float centerX = (float) x * rowWidth;
                float centerY = (float) (y - 1 + 0.1) * columnHeight;

                RectF rectF = new RectF(centerX, centerY, centerX + ringWidth, centerY + ringWidth);
                paint2.setColor(Color.RED);
                paint2.setAntiAlias(true);
                canvas.drawArc(rectF, 0, 360, true, paint2);
                paint2.setColor(Color.WHITE);
                paint2.setTextSize(60);
                canvas.drawText(String.valueOf(value), (float) (x + 0.25) * rowWidth, (float) (y - 0.25) * columnHeight, paint2);
                paint2.setTextSize(20);
                canvas.drawText("已签到", centerX + rowWidth / 4, centerY + columnHeight - 20, paint2);
            }
        }
        super.onDraw(canvas);
    }

    public void setDate(int y, int m) {
        year = y;
        month = m;
        invalidate();
    }

    public void addDate(List<Integer> l) {
        dates.clear();
        dates = l;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
        }
        return super.onTouchEvent(event);
    }

    public void setToCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH) + 1;
    }

}
