package com.dingqiqi.qijuproject.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * 动态改变图形圆角
 * Created by Administrator on 2017/7/27.
 */
public class TestRadiusView extends View {

    private Paint mPaint;

    private RectF mRectF;

    private float mRadius;

    private int mAllRadius;

    public TestRadiusView(Context context) {
        super(context);
        initView(context);
    }

    public TestRadiusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.RED);

        mRectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mRectF.set(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + getMeasuredWidth(), getPaddingTop() + getMeasuredHeight());
        mAllRadius = (getMeasuredWidth() - getPaddingLeft() - getPaddingRight()) / 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        canvas.drawRoundRect(mRectF, mRadius, mRadius, mPaint);
    }

    public void setRadius(int i) {
        mRadius = i * mAllRadius * 1.0f / 100f;
        postInvalidate();
    }

}
