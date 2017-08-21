package com.dingqiqi.qijuproject.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * 加载view
 * Created by dingqiqi on 2017/8/8.
 */

public class LoadingView extends View {
    /**
     * 初始圆路径
     */
    private Path mPath;
    //画笔
    private Paint mPaint;
    //计算路径
    private PathMeasure mPathMeasure;
    //动画
    private ValueAnimator mValueAnimator;
    //移动的路径
    private Path mMovePath;
    //第一次启动
    private boolean isFirst = true;

    public LoadingView(Context context) {
        super(context);
        initView(context);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mPath = new Path();
        mMovePath = new Path();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(10);
        mPaint.setColor(Color.BLUE);

        mPathMeasure = new PathMeasure();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawPath(mMovePath, mPaint);

        //第一次启动动画
        if (isFirst) {
            isFirst = false;
            startAnimation();
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        int mWidth = getMeasuredWidth();
        int mHeight = getMeasuredHeight();
        //减去画的路径宽度
        int mRadius = mWidth / 2 - 10;

        mPath.addCircle(mWidth / 2, mHeight / 2, mRadius, Path.Direction.CCW);
        mPathMeasure.setPath(mPath, false);
    }

    /**
     * 启动转圈动画
     */
    public void startAnimation() {
        if (mValueAnimator == null) {
            mValueAnimator = ValueAnimator.ofFloat(0, mPathMeasure.getLength());
            mValueAnimator.setDuration(1000);
            //设置动画循环
            mValueAnimator.setRepeatCount(Integer.MAX_VALUE);
            mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
            mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float value = (float) valueAnimator.getAnimatedValue();
                    float scale = value / mPathMeasure.getLength();
                    mMovePath.reset();
                    mPathMeasure.getSegment(scale * value, value, mMovePath, true);

                    postInvalidate();
                }
            });
        }
        mValueAnimator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mValueAnimator != null) {
            mValueAnimator.end();
        }
    }

}
