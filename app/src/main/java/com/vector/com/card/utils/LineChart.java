package com.vector.com.card.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/20.
 */
public class LineChart extends View {

    private int width, height;
    private int xLines = 12;//x轴上默认线条数
    private int yLines = 10;//y轴上默认线条数
    private int xColor = Color.BLACK;//x轴上线条颜色
    private int yColor = Color.BLACK;//x轴上线条颜色
    private int lineColor = Color.BLACK;
    private int dataDotColor = Color.BLACK;
    private int xUnit = 0, yUnit = 0;//x、y轴上单位长度
    private int xWidth = 5, yWidth = 5;//x、y轴线的宽度
    private float xMaxValue = 12, yMaxValue = 10;
    private float circleWidth = 15;
    private int margin = 80;
    private int textSize = 50;
    private float[] data;
    private List<Map<String, Float>> coordinates;
    private float[] xDots, yDots;
    private String yLabel = "Y";
    private String xLabel = "X";
    private float axisLabelSize = 20;

    public LineChart(Context context) {
        this(context, null);
    }

    public LineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChart(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        coordinates = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        xUnit = (width - 2 * margin) / xLines;
        yUnit = (height - 2 * margin) / yLines;
        xDots = new float[xLines + 1];
        yDots = new float[yLines + 1];
        xDots[0] = margin;
        yDots[0] = height - margin;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setStrokeWidth(xWidth);
        p.setColor(xColor);
        canvas.drawLine(margin, height - margin, width - margin, height - margin, p);
        p.setColor(yColor);
        p.setStrokeWidth(yWidth);
        canvas.drawLine(margin, height - margin, margin, margin, p);
        p.setTextSize(textSize);
        canvas.drawText("0", margin - 40, height - margin + 40, p);

        //画X、Y轴上的标签
        p.setTextSize(axisLabelSize);
        canvas.drawText(yLabel, margin - 20, margin - 10, p);
        canvas.drawText(xLabel, width - margin + 10, height - margin , p);

        //画x轴上的点
        for (int i = 0; i < xLines; i++) {
            canvas.drawLine((i + 1) * xUnit + margin, height - margin, (i + 1) * xUnit + margin, height - margin - 10, p);
            xDots[i + 1] = (i + 1) * xUnit + margin;
            if (i % 2 == 1 && i > 0) {
                canvas.drawText(String.valueOf((int) ((i + 1) * xMaxValue / xLines)), (i + 1) * xUnit + margin * 3 / 4, height - margin / 3, p);
            }
        }

        //画y轴上的点
        for (int j = 0; j < yLines; j++) {
            yDots[j + 1] = height - (j + 1) * yUnit - margin;
            canvas.drawLine(margin, height - (j + 1) * yUnit - margin, margin + 10, height - (j + 1) * yUnit - margin, p);
            if ((j + 1) == yLines / 2 || (j + 1) == yLines) {
                canvas.drawText(String.valueOf((int) (yMaxValue * (j + 1) / yLines)), margin / 3, height - (j + 1) * yUnit - margin * 3 / 4, p);
            }
        }
        initCoordinates();
        int m = coordinates.size();
        int temp = 0;
        Log.i("info", "m:" + m);
        for (int n = 0; n < coordinates.size(); n++) {
            if (m > 1) {
                temp = n + 1;
                if (temp < m) {
                    Map firstNode = coordinates.get(n);
                    Map secondNode = coordinates.get(temp);
                    p.setColor(lineColor);
                    canvas.drawLine((float) firstNode.get("x"), (float) firstNode.get("y"), (float) secondNode.get("x"), (float) secondNode.get("y"), p);
                }
            }
            //画数据点
            p.setColor(dataDotColor);
            canvas.drawCircle(coordinates.get(n).get("x"), coordinates.get(n).get("y"), circleWidth, p);
        }

        super.onDraw(canvas);
    }

    //设置数据
    public void setData(float[] data) {
        this.data = data;
    }

    private void initCoordinates() {
        Map<String, Float> map;
        coordinates.clear();
        for (int k = 0; k < data.length; k++) {
            map = new HashMap<>();
            map.put("x", xDots[(k + 1)]);
            map.put("y", (float) (height - margin - (height - 2 * margin) * ((data[k] * 100.0 / yMaxValue) / 100.0)));
            coordinates.add(map);
        }
    }

    //设置X轴上线条数
    public void setxLines(int num) {
        this.xLines = num;
    }

    //设置Y轴上线条数
    public void setyLines(int num) {
        this.yLines = num;
    }

    //设置X轴颜色
    public void setxColor(int color) {
        this.xColor = color;
    }

    //设置Y轴颜色
    public void setyColor(int color) {
        this.yColor = color;
    }

    //设置数据点颜色
    public void setDataDotColor(int color) {
        this.dataDotColor = color;
    }

    //设置线条颜色
    public void setLineColor(int color) {
        this.lineColor = color;
    }

    //设置X轴线条宽度
    public void setxWidth(int width) {
        this.xWidth = width;
    }

    //设置Y轴线条宽度
    public void setyWidth(int width) {
        this.yWidth = width;
    }

    //设置Y轴最大值
    public void setyMaxValue(float value) {
        this.yMaxValue = value;
    }

    //设置数据点的半径
    public void setCircleWidth(float width) {
        this.circleWidth = width;
    }

    //X轴最大值
    public void setxMaxValue(float value) {
        this.xMaxValue = value;
    }

    //设置X轴标签
    public void setxLabel(String text) {
        this.xLabel = text;
    }

    //设置Y轴标签
    public void setyLabel(String text) {
        this.yLabel = text;
    }

    public void setAxisLabelSize(int size) {
        this.axisLabelSize = size;
    }

}
