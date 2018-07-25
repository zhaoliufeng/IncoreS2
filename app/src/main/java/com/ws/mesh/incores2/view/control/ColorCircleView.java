package com.ws.mesh.incores2.view.control;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

/**
 * Created by zhaol on 2018/5/3.
 */

public class ColorCircleView extends android.support.v7.widget.AppCompatImageView {

    private Paint mPaint;
    private Paint mStrokePaint;
    private int mBgColor;

    public ColorCircleView(Context context) {
        super(context);
        mBgColor = Color.parseColor("#FFFFFFFF");

        initView();
    }

    public ColorCircleView(Context context, @Nullable AttributeSet attrs) {
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

        mStrokePaint = new Paint();
        mStrokePaint.setAntiAlias(true);

        mStrokePaint.setStrokeWidth(2);
        mStrokePaint.setColor(Color.GRAY);
        mStrokePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, mPaint);
        int color = mPaint.getColor();
        if ((color & 0xFFFFFF) == 0xFFFFFF){
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - 1, mStrokePaint);
        }
    }

    @Override
    public void setBackgroundColor(int color) {
        mPaint.setColor(Color.parseColor(String.format("#%06x", color & 0xFFFFFF)));
        Log.i("ColorBG", String.format("%06x", color & 0xFFFFFF));
        invalidate();
    }
}
