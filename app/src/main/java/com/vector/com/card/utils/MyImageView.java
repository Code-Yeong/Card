package com.vector.com.card.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Administrator on 2017/9/3.
 */
public class MyImageView extends ImageView {
    public MyImageView(Context context) {
        this(context, null);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Drawable mDrawable = getDrawable();
        if (mDrawable == null) {
            return;
        }
        Bitmap b = ((BitmapDrawable) mDrawable).getBitmap();
        Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
        int width = getWidth();
        Bitmap roundBitmap = getCircleBitmap(bitmap, width);
        canvas.drawBitmap(roundBitmap, 0, 0, null);
    }

    private Bitmap getCircleBitmap(Bitmap bmp, int radius) {
        Bitmap p;
        if (bmp.getWidth() != radius || bmp.getHeight() != radius) {
            p = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        } else {
            p = bmp;
        }
        Bitmap bitmap = Bitmap.createBitmap(p.getWidth(), p.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, p.getWidth(), p.getHeight());
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(p.getWidth() / 2 + 0.7f, p.getHeight() / 2 + 0.7f, p.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(p, rect, rect, paint);
        return bitmap;
    }
}
