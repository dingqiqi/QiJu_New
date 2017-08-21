package com.dingqiqi.qijuproject.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dingqiqi on 2016/9/2.
 */
public class CircleProgressBall360 extends View {
    /**
     * 圆半径。默认是控件宽度一半
     */
    private int mRadius;
    /**
     * 画线画笔
     */
    private Paint mPaintLine;
    private Paint mPaintLine1;
    //消除背景色重叠改变波浪颜色
    private Paint mPaintBgCircle;
    /**
     * 画圆画笔
     */
    private Paint mPaintCircle;
    /**
     * 画外圈画笔
     */
    private Paint mPaintRightCircle;
    /**
     * 文字画笔
     */
    private Paint mPaintText;
    /**
     * 画笔颜色
     */
    private int mLineColor = Color.parseColor("#88702e8c");
    private int mCircleColor = Color.BLUE;
    /**
     * 存储初始的sin值
     */
    private float[] mList;
    /**
     * 移动后产生的两个sin
     */
    private float[] mList1;
    private float[] mList2;
    /**
     * 线移动的速度
     */
    private int mOffSet1;
    private int mOffSet2;
    /**
     * 波纹高度
     */
    private int mBWHeight = 8;
    /**
     * 进度
     */
    private int mProgress = 0;
    /**
     * 进度高度
     */
    private int mProgressHeight = 0;
    /**
     * 是否实时刷新,显示波浪效果
     */
    private boolean mIsProgress = true;
    /**
     * 文字大小
     */
    private int mTextSize = 40;
    /**
     * 圆圈
     */
    private Bitmap mCircleBitmap;
    /**
     * 计算字体大小
     */
    private Rect mTextBounds = new Rect();
    /**
     * 文字
     */
    private String mText = "";

    private int mWidth, mHeight;

    private Handler mHandler = new Handler();

    private onProgressListener mProgressListener;
    //平移动画
    private ValueAnimator mValueAnimatorMove;
    //放大动画
    private ValueAnimator mValueAnimatorScale;
    //平移距离
    private int mTranslateValue;
    //落下来的圆的半径
    private int mTranslateRadius;

    //是否平移
    private int mIsTranslate = 0;
    //播放动画
    private boolean mIsFirst = true;
    //圆的上下左右间距
    private int mRadiusPadding = 5;
    //曲线水平移动速度
    private int mMoveValue1 = 8;
    private int mMoveValue2 = 14;
    /**
     * 背景颜色
     */
    private int mBgColor = Color.parseColor("#FF6D05");

    public CircleProgressBall360(Context context) {
        super(context, null);
    }

    public CircleProgressBall360(Context context, AttributeSet attrs) {
        super(context, attrs);
        mBWHeight = (int) (context.getResources().getDisplayMetrics().density * mBWHeight + 0.5);
        mTextSize = (int) (context.getResources().getDisplayMetrics().density * mTextSize + 0.5);
        initView();
    }

    public void setProgressListener(onProgressListener mProgressListener) {
        this.mProgressListener = mProgressListener;
    }

    private void initView() {
        //禁止硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        mPaintLine = new Paint();
        mPaintLine.setAntiAlias(true);
        mPaintLine.setStyle(Paint.Style.FILL);
        mPaintLine.setColor(mLineColor);

        mPaintLine1 = new Paint(mPaintLine);
        mPaintLine1.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));

        mPaintBgCircle = new Paint(mPaintLine1);
        mPaintBgCircle.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

        mPaintCircle = new Paint(mPaintLine);
        mPaintCircle.setStyle(Paint.Style.FILL);
        mPaintCircle.setStrokeWidth(8);
        mPaintCircle.setColor(mCircleColor);

        mPaintRightCircle = new Paint(mPaintCircle);
        mPaintRightCircle.setStyle(Paint.Style.STROKE);
        mPaintRightCircle.setStrokeWidth(2);
        mPaintRightCircle.setColor(Color.BLACK);

        mPaintText = new Paint(mPaintRightCircle);
        mPaintText.setTextSize(mTextSize);
        mPaintText.setStyle(Paint.Style.FILL);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // setMeasuredDimension(getMeasureSpec(widthMeasureSpec, 1), getMeasureSpec(heightMeasureSpec, 2));
        //设置控件宽高一样，为宽度与高度间的最小值
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
//        int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
//        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //设置半径为控件一半宽度
        mRadius = w < h ? w / 2 : h / 2;
        initData();
    }

    private int getMeasureSpec(int spec, int flag) {
        int value;
        if (flag == 1) {
            value = getMeasuredWidth() + getPaddingLeft() + getPaddingRight();
        } else {
            value = getMeasuredHeight() + getPaddingTop() + getPaddingBottom();
        }

        int mode = MeasureSpec.getMode(spec);
        int size = MeasureSpec.getSize(spec);

        switch (mode) {
            case MeasureSpec.EXACTLY:
                value = size;
                break;
        }

        return value;
    }

    /**
     * 初始化sin值
     */
    public void initData() {
        mList = new float[getMeasuredWidth()];
        mList1 = new float[getMeasuredWidth()];
        mList2 = new float[getMeasuredWidth()];
        //存储sin值
        for (int i = 0; i < getMeasuredWidth(); i++) {
            //*2是为了间距放大，让线更圆滑
            mList[i] = mBWHeight * (float) Math.sin(i * 2 * Math.PI / getMeasuredWidth());
        }

        mTranslateRadius = mRadius / 14;

        //移动到屏幕中间动画
        mValueAnimatorMove = ValueAnimator.ofInt(-2 * mTranslateRadius, getMeasuredHeight() / 2);
        mValueAnimatorMove.setDuration(1000);
        mValueAnimatorMove.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //圆从上到下的移动距离
                mTranslateValue = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        mValueAnimatorMove.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //启动放大动画
                mValueAnimatorScale.start();
            }
        });

        //圆移动到屏幕中间放大到最大的动画
        mValueAnimatorScale = ValueAnimator.ofInt(mTranslateRadius, mRadius - mRadiusPadding);
        mValueAnimatorScale.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTranslateRadius = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        mValueAnimatorScale.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mIsTranslate = 1;
                postInvalidate();
                //动画初始化完成
                if (mProgressListener != null) {
                    mProgressListener.initAnimation();
                }
            }
        });
        mValueAnimatorScale.setDuration(1000);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        //画背景
        //canvas.drawColor(mBgColor);

        //平移
        if (mIsTranslate == 0) {
            mPaintRightCircle.setStyle(Paint.Style.FILL);
            canvas.drawCircle(getMeasuredWidth() / 2, mTranslateValue, mTranslateRadius, mPaintRightCircle);
            //启动移动动画
            if (mIsFirst) {
                mValueAnimatorMove.start();
                mIsFirst = false;
            }
        } else {
            //画圆，是为了圆形的水纹波浪
            if (mCircleBitmap == null) {
                mCircleBitmap = getCircleBitmap();
            }
            int centerX = (mWidth - getMeasuredWidth()) / 2;
            int centerY = 0;

//            //画线
//            moveData();
//            Bitmap bitmap = getLineBitmap();
//            //外层投影到背景，相交处图形
//            canvas.drawBitmap(bitmap, centerX, centerY, null);
//
//            canvas.drawBitmap(mCircleBitmap, centerX, centerY, mPaintLine1);

//            canvas.drawBitmap(getBWCircle(), centerX, centerY, mPaintBgCircle);
            canvas.drawBitmap(getBWCircle(), centerX, centerY, null);


        /*----------------------------提示---------------------------*/
        /*  要使setXfermode有效果，要用两个bitmap相交，不然没效果  */

            //画外圈
            mPaintRightCircle.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(centerX + getMeasuredWidth() / 2, centerY + getMeasuredHeight() / 2, mRadius - mRadiusPadding, mPaintRightCircle);
            //画进度文字
            canvas.drawText(mText, centerX + getMeasuredWidth() / 2 - mTextBounds.width() / 2, centerY + getMeasuredHeight() / 2 + mTextBounds.height() / 2, mPaintText);

            //移动速度
            mOffSet1 = mOffSet1 + mMoveValue1;
            mOffSet2 = mOffSet2 + mMoveValue2;

            //大于宽度重新开始
            if (mOffSet1 >= getMeasuredWidth()) {
                mOffSet1 = 0;
            }

            if (mOffSet2 >= getMeasuredWidth()) {
                mOffSet2 = 0;
            }

            //是否启动波浪效果
            if (mIsProgress) {
                mHandler.postDelayed(mRunnable, 15);
            }
        }
    }

    private Bitmap getBWCircle() {
        Bitmap allBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(allBitmap);
        //画线
        moveData();
        Bitmap bitmap = getLineBitmap();

        int centerX = (mWidth - getMeasuredWidth()) / 2;
        int centerY = 0;
        //画水波纹
        canvas.drawBitmap(bitmap, centerX, centerY, null);
        //背景投影到外层，相交处圆形
        canvas.drawBitmap(mCircleBitmap, centerX, centerY, mPaintLine1);

        return allBitmap;
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            postInvalidate();
        }
    };

    /**
     * 获取圆的bitmap
     *
     * @return
     */
    public Bitmap getCircleBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, mRadius - mRadiusPadding, mPaintCircle);

        return bitmap;
    }

    /**
     * 获取线的bitmap
     *
     * @return
     */
    public Bitmap getLineBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        for (int i = 0; i < mList.length; i++) {
            if (mIsProgress) {
                canvas.drawLine(i, getMeasuredHeight() / 2 + mRadius - mList1[i] - mProgressHeight, i, getMeasuredHeight() / 2 + mRadius, mPaintLine);
                canvas.drawLine(i, getMeasuredHeight() / 2 + mRadius - mList2[i] - mProgressHeight, i, getMeasuredHeight() / 2 + mRadius, mPaintLine);
            } else {
                mPaintLine.setColor(Color.parseColor("#AA702e8c"));
                canvas.drawLine(i, 0, i, getMeasuredHeight(), mPaintLine);
            }
        }

        return bitmap;
    }

    /**
     * 移动数据，产生波浪效果
     */
    public void moveData() {
        int offsetLength = mList.length - mOffSet1;
        System.arraycopy(mList, mOffSet1, mList1, 0, offsetLength);
        System.arraycopy(mList, 0, mList1, offsetLength, mOffSet1);

        int offsetLength1 = mList.length - mOffSet2;
        System.arraycopy(mList, mOffSet2, mList2, 0, offsetLength1);
        System.arraycopy(mList, 0, mList2, offsetLength1, mOffSet2);
    }

    /**
     * 设置进度
     *
     * @param mProgress
     */
    public void setProgress(int mProgress) {
        mText = String.valueOf(mProgress + "%");
        mPaintText.getTextBounds(mText, 0, mText.length(), mTextBounds);

        this.mProgress = mProgress;
        this.mProgressHeight = mProgress * (mRadius - mRadiusPadding) / 50;
        if (mProgress >= 100) {
            mIsProgress = false;
            invalidate();
        } else {
            if (!mIsProgress) {
                mIsProgress = true;
                invalidate();
            }
        }

        if (mProgressListener != null) {
            mProgressListener.onProgress(mProgress >= 100 ? 100 : mProgress);
        }
    }

    public interface onProgressListener {
        //进度
        void onProgress(int progress);

        //初始动画加载完成
        void initAnimation();
    }

}
