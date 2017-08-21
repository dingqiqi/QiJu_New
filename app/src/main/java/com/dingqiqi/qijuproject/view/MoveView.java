package com.dingqiqi.qijuproject.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 移动的view
 * Created by 丁奇奇 on 2017/6/29.
 */
public class MoveView extends ImageView {
    /**
     * 可用宽高
     */
    private int mWidth, mHeight;
    /**
     * 上下左右padding
     */
    private int mLeft, mTop;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 分块数量
     */
    private int mNum = 3;
    /**
     * 背景图片
     */
    private Bitmap mBitmapBg;
    /**
     * 用于bitmap放大缩小
     */
    private Matrix mMatrix;
    /**
     * 画图区域
     */
    private RectF mRectF = new RectF();
    /**
     * 所有小图
     */
    private Bitmap[] mAllBitmap;

    //图片下标排列
    //private int[] mIndex = new int[]{0, 1, 2, 3, 4, 7, 5, 6, 8};
    private int[] mIndex = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
    /**
     * 正确的下标
     */
    private int[] mOkIndex;
    //小图片宽高
    //private int mImgWidth;
    //private int mImgHeight;
    //平均高宽
    private float mAvgWidth;
    private float mAvgHeight;
    /**
     * 可移动的下标
     */
    private int mPosition;
    /**
     * 是否成功
     */
    private boolean mIsSuccess = true;
    /**
     * 结果回调
     */
    private onCallBack mOnCallBack;
    /**
     * 时间
     */
    private int mSecond;
    /**
     * 点击次数
     */
    private int mCount;
    /**
     * 点击的下标
     */
    private int mDownIndex;
    /**
     * 用于计时
     */
    private Handler mHandler = new Handler();
    /**
     * 是否是第一次点击
     */
    private boolean mIsFirst = true;
    /**
     * 图片放大缩小
     */
    private float mScale = 1.0f;
    /**
     * 是否第一次启动
     */
    private boolean mIsStart = true;

    /**
     * 用于放大缩小
     */
    private ValueAnimator mValueAnimator = ValueAnimator.ofFloat(0.5f, 1.0f);
    /**
     * 初始化游戏时存储数据
     */
    private List<Integer> mList = new ArrayList<>();
    /**
     * 是否重画
     */
    private boolean mIsDraw = false;

    public MoveView(Context context) {
        super(context);
        initView(context);
    }

    public MoveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MoveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化
     *
     * @param context 上下文
     */
    private void initView(Context context) {
        Log.i("aaa", context.getPackageName());
        //线画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(3);

        //用于缩放bitmap的matrix
        mMatrix = new Matrix();

        //初始化空格下标位置
        mPosition = mIndex.length - 1;

        //拼图成功缩放动画
        mValueAnimator.setDuration(800);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mScale = (float) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        //缩放动画完成监听,启动放大动画
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (mIsStart) {
                    mIsStart = false;
                    mValueAnimator.start();
                }
            }
        });
    }

    /**
     * 设置游戏监听
     *
     * @param onCallBack 回调
     */
    public void setOnCallBack(onCallBack onCallBack) {
        this.mOnCallBack = onCallBack;
    }

    /**
     * 设置背景图片
     *
     * @param bitmapBg 背景图片
     */
    public void setBitmapBg(Bitmap bitmapBg) {
        mBitmapBg = bitmapBg;
        startGame(mNum);
    }

    /**
     * 获取背景图片
     *
     * @return
     */
    public Bitmap getBitmapBg() {
        return mBitmapBg;
    }

    /**
     * 初始化化游戏
     *
     * @param num 宽高格子数
     */
    public void startGame(int num) {
        mNum = num;
        int length = num * num;
        mOkIndex = new int[length];

        mIndex = new int[length];
        mOkIndex = new int[length];

        //给下标赋值
        for (int i = 0; i < length; i++) {
            mIndex[i] = i;
            mOkIndex[i] = i;
        }

        //变量还原
        mIsSuccess = false;
        mSecond = 0;
        mCount = 0;
        mIsStart = true;
        mIsFirst = true;
        mScale = 1.0f;

        mPosition = length - 1;

        //初始化游戏,移动格子
        initGame(length, mNum * 10);

        //是否重画
        mIsDraw = true;

        //重新计算大小并画图
        requestLayout();
        postInvalidate();
    }

    /**
     * 初始化游戏,移动格子
     *
     * @param length 数组长度
     * @param count  移动次数
     */
    private void initGame(int length, int count) {
        for (int i = 0; i < count; i++) {
            moveGame();
        }

        //判断拼图成功
        int index = -1;
        for (int i = 0; i < length; i++) {
            if (mIndex[i] != mOkIndex[i]) {
                index = i;
                break;
            }
        }

        //拼图成功,重新初始化游戏位置
        if (index == -1) {
            initGame(length, count);
        }
    }

    /**
     * 移动格子,初始化游戏
     */
    private void moveGame() {
        //计算上下左右能移动的下标(9个=0-8)
        int leftPosition = mPosition % mNum == 0 ? -1 : mPosition - 1;
        int rightPosition = mPosition % mNum == (mNum - 1) ? -1 : mPosition + 1;
        int topPosition = mPosition - mNum;
        int bottomPosition = mPosition + mNum;

        int length = mIndex.length;

        mList.clear();
        //能左移0-8
        if (leftPosition >= 0) {
            mList.add(leftPosition);
        }

        //能右移0-8
        if (rightPosition >= 0 && rightPosition < length) {
            mList.add(rightPosition);
        }

        //能上移0-8
        if (topPosition >= 0) {
            mList.add(topPosition);
        }

        //能下移0-8
        if (bottomPosition >= 0 && bottomPosition < length) {
            mList.add(bottomPosition);
        }

        //有能移动的格子
        if (mList.size() > 0) {
            //随机上下左右移动
            int index = (int) (Math.random() * mList.size());
            //获取移动的下标
            mDownIndex = mList.get(index);

            //更换下标图案位置
            int value = mIndex[mPosition];
            mIndex[mPosition] = mIndex[mDownIndex];
            mIndex[mDownIndex] = value;

            //记录最新的空白位置
            mPosition = mDownIndex;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mLeft = getPaddingLeft();
        int mRight = getPaddingRight();
        mTop = getPaddingTop();
        int mBottom = getPaddingBottom();

        //计算可用的宽高
        mWidth = getMeasuredWidth() - mLeft - mRight;
        mHeight = getMeasuredHeight() - mTop - mBottom;
        //计算方框平均宽高
        mAvgWidth = mWidth / (1.0f * mNum);
        mAvgHeight = mHeight / (1.0f * mNum);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //画背景,掩盖背景图片
        canvas.drawColor(Color.WHITE);
        //获取背景图片
        Drawable drawable = getBackground();
        if (drawable != null) {
            //bitmap只要计算一次
            if (mBitmapBg == null || mIsDraw) {
                mIsDraw = false;

                int imgWidth;
                int imgHeight;

                if (mBitmapBg != null) {
                    imgWidth = mBitmapBg.getWidth();
                    imgHeight = mBitmapBg.getHeight();
                } else {
                    //获取图片宽高
                    imgWidth = drawable.getIntrinsicWidth();
                    imgHeight = drawable.getIntrinsicHeight();

                    //获取图片bitmap
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    mBitmapBg = bitmapDrawable.getBitmap();
                }

                //获取图片缩放比例
                float scale = 1 / Math.min(imgWidth / (1.0f * mWidth), imgHeight / (1.0f * mHeight));
                mMatrix.reset();
                mMatrix.setScale(scale, scale);
                //缩放图片
                mBitmapBg = Bitmap.createBitmap(mBitmapBg, 0, 0, imgWidth, imgHeight, mMatrix, false);

                //获取小图片的平均宽高
                float mImgWidth = mBitmapBg.getWidth() / (1.0f * mNum);
                float mImgHeight = mBitmapBg.getHeight() / (1.0f * mNum);

                //计算要画的方框个数
                int length = mNum * mNum;
                mAllBitmap = new Bitmap[length];
                mOkIndex = new int[length];

                for (int i = 0; i < length; i++) {
                    //存储对应方框的图像
                    mAllBitmap[i] = Bitmap.createBitmap(mBitmapBg, (int) mImgWidth * (i % mNum), (int) mImgHeight * (i / mNum), (int) mImgWidth, (int) mImgHeight);
                    //给正确下标赋值
                    mOkIndex[i] = i;
                }
            }

            //画每个小格的图片
            for (int i = 0; i < mIndex.length; i++) {
                //获取随机下标
                int index = mIndex[i];

                //最后一块图片不画(拼图的最后一个图不画,不是最后一个空格)
                if ((!mIsSuccess) && index == mIndex.length - 1) {
                    continue;
                }

                float leftWidth = mAvgWidth * (i % mNum) + mAvgWidth * (1 - mScale) / 2.0f;
                float rightWidth = mAvgWidth + mAvgWidth * (i % mNum) - mAvgWidth * (1 - mScale) / 2.0f;
                float topHeight = mAvgHeight * (i / mNum) + mAvgHeight * (1 - mScale) / 2.0f;
                float bottomHeight = mAvgHeight + mAvgHeight * (i / mNum) - mAvgHeight * (1 - mScale) / 2.0f;

                //设置图片区域
                mRectF.set(mLeft + leftWidth, mTop + topHeight, mLeft + rightWidth, mTop + bottomHeight);
//                Log.i("aaa", "上  " + (mTop + topHeight) + "下  " + (mTop + bottomHeight));
                //画图
                canvas.drawBitmap(mAllBitmap[index], null, mRectF, null);
            }

            //画间隔线
            drawLine(canvas);
        }
    }

    private void drawLine(Canvas canvas) {
        for (int i = 0; i <= mNum; i++) {
            //成功后不画中间线(4*4不知道为啥中间有空格,画线盖住空格)
            if (mIsSuccess && i != 0 && i != mNum) {
                continue;
            }
            //画水平线
            canvas.drawLine(mLeft, mTop + i * mAvgHeight, mLeft + mWidth, mTop + i * mAvgHeight, mPaint);

            //画垂直线
            canvas.drawLine(mLeft + i * mAvgWidth, mTop, mLeft + i * mAvgWidth, mTop + mHeight, mPaint);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //释放bitmap,
        mBitmapBg.recycle();
        mBitmapBg = null;

        //回收bitmap
        for (int i = 0; i < mAllBitmap.length; i++) {
            mAllBitmap[i].recycle();
            mAllBitmap[i] = null;
        }

        //移除时间倒计时回调
        mHandler.removeCallbacks(mTimeRunnable);
        mAllBitmap = null;
    }

    /**
     * 获取当前点击下标
     *
     * @return 点中下标(0到8)
     */
    private int getCurIndex(float downX, float downY) {
        int x = (int) ((downX - mLeft) / mAvgWidth);
        int y = (int) ((downY - mTop) / mAvgHeight);

        return y * mNum + x;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //计算点击下标
                mDownIndex = getCurIndex(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                //计算抬起下标
                int mUpIndex = getCurIndex(event.getX(), event.getY());
                //点击同一个图片 且未按到空白图片
                if (mDownIndex == mUpIndex && mDownIndex != mPosition) {
                    isImgMove();
                }
                break;
        }
        //拼图成功不让点击
        return !mIsSuccess;
    }

    /**
     * 是否能移动图片
     */
    private void isImgMove() {
        //计算上下左右能移动的下标
        int leftPosition = mPosition % mNum == 0 ? -1 : mPosition - 1;
        int rightPosition = mPosition % mNum == (mNum - 1) ? -1 : mPosition + 1;
        int topPosition = mPosition - mNum;
        int bottomPosition = mPosition + mNum;

        //只要点击的下标和下面的某一个下标一致,则代表能移动
        if (mDownIndex == leftPosition) {
            moveBitmap();
        } else if (mDownIndex == rightPosition) {
            moveBitmap();
        } else if (mDownIndex == topPosition) {
            moveBitmap();
        } else if (mDownIndex == bottomPosition) {
            moveBitmap();
        } else {
            Log.i("aaa", "不能移动");
            //Toast.makeText(mContext, "不能移动", Toast.LENGTH_SHORT).show();
        }
    }

    private void moveBitmap() {
        //第一次点击开始计时
        if (mIsFirst) {
            mIsFirst = false;
            mHandler.postDelayed(mTimeRunnable, 1000);
        }

        //点击计数+1
        mCount++;

        //更换点击的图案位置
        int value = mIndex[mPosition];
        mIndex[mPosition] = mIndex[mDownIndex];
        mIndex[mDownIndex] = value;

        //记录最新的空白位置
        mPosition = mDownIndex;

        //判断拼图成功
        int index = -1;
        for (int i = 0; i < mIndex.length; i++) {
            if (mIndex[i] != mOkIndex[i]) {
                index = i;
                break;
            }
        }

        //回调点击计数
        if (mOnCallBack != null) {
            mOnCallBack.onCount(mCount);
        }

        //拼图成功
        if (index == -1) {
            mIsSuccess = true;
            if (mOnCallBack != null) {
                mOnCallBack.onSuccess();
                mIsStart = true;
                mValueAnimator.reverse();
            }
        }

        postInvalidate();
    }

    /**
     * 用于计时
     */
    private Runnable mTimeRunnable = new Runnable() {
        @Override
        public void run() {
            //在玩游戏中
            if (!mIsSuccess) {
                mSecond++;
                //计时回调
                if (mOnCallBack != null) {
                    mOnCallBack.onTime(mSecond);
                }
                //延时
                mHandler.postDelayed(mTimeRunnable, 1000);
            }
        }
    };

    /**
     * 计数计时回调
     */
    public interface onCallBack {
        void onSuccess();

        void onTime(int time);

        void onCount(int count);
    }

}
