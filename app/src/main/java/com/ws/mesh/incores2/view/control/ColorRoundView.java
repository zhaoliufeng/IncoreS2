package com.ws.mesh.incores2.view.control;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by zhaol on 2018/5/3.
 */

public class ColorRoundView extends android.support.v7.widget.AppCompatImageView {

    private Paint mPaint;
    private int mBgColor;

    public ColorRoundView(Context context) {
        super(context);
        mBgColor = Color.parseColor("#7FFF0000");

        initView();
    }

    public ColorRoundView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Drawable bg = getBackground();
        if (bg instanceof ColorDrawable) {
            mBgColor = ((ColorDrawable) bg).getColor();
        }

        initView();
        setBackground(null);
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mPaint.setColor(mBgColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF();
        rectF.top = 0;
        rectF.bottom = getHeight();
        rectF.left = 0;
        rectF.right = getWidth();
        canvas.drawRoundRect(rectF, 10, 10, mPaint);
    }

    @Override
    public void setBackgroundColor(int color) {
        mPaint.setColor(color);
        postInvalidate();
    }
}
