package com.dingqiqi.qijuproject.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.dingqiqi.qijuproject.util.CommonUtil;
import com.dingqiqi.qijuproject.util.DisplayUtil;
import com.dingqiqi.qijuproject.util.LogUtil;

/**
 * Created by 丁奇奇 on 2017/6/8.
 */
public class CircleProgress extends View {

    private Context mContext;
    //圆画笔
    private Paint mCirclePaint;
    //进度文字画笔
    private Paint mProgressPaint;
    //aqi文字画笔
    private Paint mAQIPaint;
    //标题画笔
    private Paint mTitlePaint;
    //圆半径
    private int mRadius;
    //圆半径
    private int mStrokeWidth = 5;
    //进度文字大小
    private int mProgressTextSize = 50;
    //aqi文字大小
    private int mAQITextSize = 20;
    //标题文字大小
    private int mTitleTextSize = 18;

    //圆背景颜色
    private int mCircleBgColor = Color.parseColor("#E8E8E8");
    //AQI文字颜色
    private int mAQITextColor = Color.parseColor("#BCBCBC");
    //title文字颜色
    private int mTitleTextColor = Color.parseColor("#000000");

    //全局的矩形(用于画圆)
    private RectF mRectF = new RectF();
    //用于计算文字大小
    private Rect mTextBounds = new Rect();
    //进度
    private String mProgress = "100";
    //AQI文字
    private String mAQIText = "AQI";
    //标题
    private String mTitleText = "空气质量指数";
    //总角度
    private int mAllAngle = 270;
    //进度条角度
    private int mProgressAngle;
    //动画
    private ValueAnimator mValueAnimator;
    //设置的进度值
    private int mSetProgress;

    public CircleProgress(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public CircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        mStrokeWidth = DisplayUtil.dp2px(mContext, mStrokeWidth);
        mAQITextSize = DisplayUtil.dp2px(mContext, mAQITextSize);
        mTitleTextSize = DisplayUtil.dp2px(mContext, mTitleTextSize);
        mProgressTextSize = DisplayUtil.dp2px(mContext, mProgressTextSize);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setColor(mCircleBgColor);
        mCirclePaint.setStrokeWidth(mStrokeWidth);

        mAQIPaint = new Paint(mCirclePaint);
        mAQIPaint.setStyle(Paint.Style.FILL);
        mAQIPaint.setTextSize(mAQITextSize);
        mAQIPaint.setColor(mAQITextColor);

        mTitlePaint = new Paint(mAQIPaint);
        mTitlePaint.setTextSize(mTitleTextSize);
        mTitlePaint.setColor(mTitleTextColor);

        mProgressPaint = new Paint(mAQIPaint);
        mProgressPaint.setTextSize(mProgressTextSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //获取进度圆的半径
        mRadius = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 2 - 10;
        mRectF.set(getMeasuredWidth() / 2 - mRadius, getMeasuredHeight() / 2 - mRadius, getMeasuredWidth() / 2 + mRadius, getMeasuredHeight() / 2 + mRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画进度背景-135 起始角度(右边角度为0开始计算)
        mCirclePaint.setColor(mCircleBgColor);
        canvas.drawArc(mRectF, 135, mAllAngle, false, mCirclePaint);

        //获取进度文字长宽
        mProgressPaint.getTextBounds(mProgress, 0, mProgress.length(), mTextBounds);
        //设置画笔颜色
        mProgressPaint.setColor(CommonUtil.getWeatherColor(Integer.parseInt(mProgress)));
        //画进度文字
        canvas.drawText(mProgress, getMeasuredWidth() / 2 - mTextBounds.width() / 2, getMeasuredHeight() / 2, mProgressPaint);

        //获取AQI文字长宽
        mAQIPaint.getTextBounds(mAQIText, 0, mAQIText.length(), mTextBounds);
        //画AQI文字
        canvas.drawText(mAQIText, getMeasuredWidth() / 2 - mTextBounds.width() / 2, getMeasuredHeight() / 2 + mTextBounds.height() + 10, mAQIPaint);

        //获取标题文字长宽
        mTitlePaint.getTextBounds(mTitleText, 0, mTitleText.length(), mTextBounds);
        //画标题文字
        canvas.drawText(mTitleText, getMeasuredWidth() / 2 - mTextBounds.width() / 2, getMeasuredHeight() / 2 + mRadius, mTitlePaint);

        //设置进度条颜色
        mCirclePaint.setColor(CommonUtil.getWeatherColor(Integer.parseInt(mProgress)));
        //画进度-135 起始角度(右边角度为0开始计算)
        canvas.drawArc(mRectF, 135, mProgressAngle, false, mCirclePaint);
    }

    /**
     * 设置进度条进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        LogUtil.d(" 设置的进度 " + progress);
        if (progress < 0) {
            return;
        }
        mSetProgress = progress;

        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }

        mValueAnimator = ValueAnimator.ofInt(progress);
        mValueAnimator.addUpdateListener(mUpdateListener);
        mValueAnimator.setDuration(1000);
        mValueAnimator.start();
    }

    /**
     * 动画返回值的监听
     */
    private ValueAnimator.AnimatorUpdateListener mUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            int value = (int) animation.getAnimatedValue();
            mProgress = String.valueOf(value).trim();
            mProgressAngle = (int) (value / 300f * mAllAngle);
            postInvalidate();
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //如果进度大于0,刷新下界面,防止出现动画播放到一半进度显示不正确
        if (mSetProgress > 0) {
            mProgress = String.valueOf(mSetProgress);
            invalidate();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //停止动画
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }

    }
}
