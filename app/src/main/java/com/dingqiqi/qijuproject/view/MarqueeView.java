package com.dingqiqi.qijuproject.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * 跑马灯效果
 * Created by dingqiqi on 2016/6/16.
 */
public class MarqueeView extends TextView {

    private Paint mPaint;

    private Context mContext;

    private int mWidth;
    private int mHeight;

    private int mCurrentWidth;

    private Handler mHandler;
    //渐变颜色
    private String[] mColors = new String[]{"#600030", "#820041", "#9F0050", "#BF0060", "#D9006C", "#F00078", "#FF0080", "#FF359A", "#FF60AF", "#FF79BC", "#FF95CA", "#ffaad5"};
    //颜色下标
    private int mCount = 0;
    /**
     * 广告标题
     */
    private String[] mTitleArray;
    /**
     * 当前文字下标
     */
    private int mClickIndex;
    /**
     * 标题个数
     */
    private int mAllTitleLength;
    /**
     * 文字信息
     */
    private Rect mTextBounds;
    /**
     * 标题
     */
    private String mText;

    private onTitleClickListener mClickListener;

    //字体跑动延时时间ms
    private int mDelayTime = 30;

    public MarqueeView(Context context) {
        super(context, null);

    }

    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        mPaint = new Paint();
        mHandler = new Handler();
    }

    //设置移动文字标题
    public void setTitleArray(String[] mTitleArray) {
        this.mTitleArray = mTitleArray;
        mAllTitleLength = mTitleArray.length;

        if (mAllTitleLength > 0) {
            mText = mTitleArray[0];
        }
    }

    public void setClickListener(onTitleClickListener mClickListener) {
        this.mClickListener = mClickListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                stopRun();
                break;
            case MotionEvent.ACTION_UP:
                startRun();
                if (mClickListener != null) {
                    mClickListener.onClick(mClickIndex, mText);
                }
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);

        if (mAllTitleLength != 0) {
            mPaint.setTextSize(getTextSize());
            mPaint.setColor(Color.parseColor(mColors[mCount]));
            mPaint.setAntiAlias(true);
//        mPaint.setUnderlineText(true);

            if (mTextBounds == null) {
                mTextBounds = new Rect();
            }
            mPaint.getTextBounds(mText, 0, mText.length(), mTextBounds);

            int height = mHeight * 2 / 3;
            canvas.drawText(mText, mCurrentWidth, height, mPaint);

            mCurrentWidth += 3;
            mCount++;

            if (mCount == mColors.length - 1) {
                mCount = 0;
            }

            if (mCurrentWidth >= mWidth) {
                mCurrentWidth = 0;
                setNextTitle();
            }

            mHandler.postDelayed(mRunnable, mDelayTime);
        }

    }

    private void setNextTitle() {
        mClickIndex++;
        if (mClickIndex == mAllTitleLength) {
            mClickIndex = 0;
        }

        mText = mTitleArray[mClickIndex];

        mPaint.getTextBounds(mText, 0, mText.length(), mTextBounds);
        mCurrentWidth = -mTextBounds.width();
    }


    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            postInvalidate();
        }
    };

    //是否在跑动中
    boolean mIsRun = false;

    /**
     * 开始启动
     */
    public void startRun() {
        if (!mIsRun) {
            mHandler.postDelayed(mRunnable, mDelayTime);
            mIsRun = true;
        }
    }

    /**
     * 停止运行
     */
    public void stopRun() {
        try {
            mHandler.removeCallbacks(mRunnable);
            mIsRun = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    public interface onTitleClickListener {
        void onClick(int index, String title);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacks(mRunnable);
    }
}
