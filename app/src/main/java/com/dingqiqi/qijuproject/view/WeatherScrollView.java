package com.dingqiqi.qijuproject.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.util.CommonUtil;
import com.dingqiqi.qijuproject.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingqiqi on 2017/6/12.
 */
public class WeatherScrollView extends View {

    private Context mContext;
    //控件高度 上面控件高度(最小温度及以上为上面高度) 文字加间距高度
    private int mHeight, mTopHeight, mTextHeight;

    private Paint mPaintWeather;
    private Paint mPaintCircle;
    private Paint mPaintLine;
    private Paint mPaintFeng;
    private Paint mPaintWuRan;
    private Paint mPaintTimeLine;
    private Paint mPaintTime;
    //圆半径
    private int mRadius = 3;
    //文字大小
    private int mTextSize = 18;
    //污染文字大小
    private int mWRTextSize = 15;
    //画线宽度
    private int mStrokeWidth = 2;
    //风等级污染度间隔
    private int mPadding = 10;
    //时间刻度间隔
    private int mTimePadding = 80;
    //总宽度
    private int mTotalWidth;

    //温度字体颜色
    private int mWeatherTextColor = getResources().getColor(R.color.colorBlackGray);
    //圆圈颜色
    private int mCircleColor = Color.parseColor("#24C3F1");
    //虚线颜色
    private int mLineColor = Color.parseColor("#DFDFDF");
    // 风等级颜色
    private int mFengTextColor = Color.parseColor("#94A7B1");
    // 线颜色
    private int mTimeLineColor = Color.parseColor("#DEDEDE");
    // 时间颜色
    private int mTimeColor = Color.parseColor("#7F7F7F");
    //画虚线效果 (float[] 实线长度 虚线长度 最少两个参数 0 指开始画线的偏移量)
    private PathEffect mPathEffect = new DashPathEffect(new float[]{10f, 5f}, 0);
    //当前温度
    private int mCurrentValue = 23;
    //温度值
    private int[] mValueStr = new int[24];
    //温度变化值
    private List<Integer> mWeatherStr = new ArrayList<>();
    //温度变化值
    private int[] mLevelStr = new int[24];

    //时间刻度值
    private String[] mTimeArray = new String[24];

    //最大最小温度
    private int mMaxValue, mMinValue;
    //计算文字宽高
    private Rect mTextBounds = new Rect();
    //污染边框范围
    private RectF mRectF = new RectF();
    //天气图片
    private Bitmap mBitmap;
    //图片宽高
    private int mImageSize = 25;

    public WeatherScrollView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public WeatherScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public WeatherScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        mRadius = DisplayUtil.dp2px(mContext, mRadius);
        mTextSize = DisplayUtil.dp2px(mContext, mTextSize);
        mStrokeWidth = DisplayUtil.dp2px(mContext, mStrokeWidth);
        mPadding = DisplayUtil.dp2px(mContext, mPadding);
        mTimePadding = DisplayUtil.dp2px(mContext, mTimePadding);
        mWRTextSize = DisplayUtil.dp2px(mContext, mWRTextSize);

        mPaintWeather = new Paint();
        mPaintWeather.setAntiAlias(true);
        mPaintWeather.setStyle(Paint.Style.FILL);
        mPaintWeather.setTextSize(mTextSize);
        mPaintWeather.setColor(mWeatherTextColor);
        mPaintWeather.setStrokeWidth(mStrokeWidth);

        mPaintCircle = new Paint(mPaintWeather);
        mPaintCircle.setStyle(Paint.Style.STROKE);
        mPaintCircle.setColor(mCircleColor);

        mPaintLine = new Paint(mPaintWeather);
        mPaintLine.setColor(mLineColor);
        mPaintLine.setPathEffect(mPathEffect);

        mPaintFeng = new Paint(mPaintWeather);
        mPaintFeng.setColor(mFengTextColor);
        mPaintFeng.setTextSize(mWRTextSize);

        mPaintWuRan = new Paint(mPaintWeather);
        mPaintWuRan.setTextSize(mWRTextSize);

        mPaintTimeLine = new Paint(mPaintWeather);
        mPaintTimeLine.setColor(mTimeLineColor);

        mPaintTime = new Paint(mPaintWeather);
        mPaintTime.setColor(mTimeColor);

        initData();
    }

    private void initData() {
        mValueStr[0] = mCurrentValue;
        mValueStr[1] = mCurrentValue;
        mValueStr[2] = mCurrentValue;
        mValueStr[3] = mCurrentValue - 1;
        mValueStr[4] = mCurrentValue - 2;
        mValueStr[5] = mCurrentValue - 2;
        mValueStr[6] = mCurrentValue - 1;
        mValueStr[7] = mCurrentValue;
        mValueStr[8] = mCurrentValue + 1;
        mValueStr[9] = mCurrentValue + 1;
        mValueStr[10] = mCurrentValue + 1;
        mValueStr[11] = mCurrentValue + 1;
        mValueStr[12] = mCurrentValue + 1;
        mValueStr[13] = mCurrentValue + 1;
        mValueStr[14] = mCurrentValue + 2;
        mValueStr[15] = mCurrentValue;
        mValueStr[16] = mCurrentValue + 1;
        mValueStr[17] = mCurrentValue;
        mValueStr[18] = mCurrentValue - 2;
        mValueStr[19] = mCurrentValue - 3;
        mValueStr[20] = mCurrentValue - 3;
        mValueStr[21] = mCurrentValue - 3;
        mValueStr[22] = mCurrentValue - 3;
        mValueStr[23] = mCurrentValue - 3;

        mWeatherStr.add(1);
        mWeatherStr.add(2);
        mWeatherStr.add(4);
        mWeatherStr.add(11);
        mWeatherStr.add(14);
        mWeatherStr.add(17);
        mWeatherStr.add(23);

        //计算最大最小值
        for (int i = 0; i < mValueStr.length; i++) {
            if (i == 0) {
                mMaxValue = mMinValue = mValueStr[i];
            }

            if (mMaxValue < mValueStr[i]) {
                mMaxValue = mValueStr[i];
            }

            if (mMinValue > mValueStr[i]) {
                mMinValue = mValueStr[i];
            }

            //0-500(随机污染指数)
            int level = (int) (Math.random() * 400);
            mLevelStr[i] = level;
        }

        for (int i = 0; i < mTimeArray.length; i++) {
            if (i < 10) {
                mTimeArray[i] = "0" + i + ":00";
            } else {
                mTimeArray[i] = i + ":00";
            }
        }

        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

        String str = "测试文字";
        mPaintWeather.getTextBounds(str, 0, str.length(), mTextBounds);

        mTextHeight = mTextBounds.height() + 10;
        //图形上面高度
        mTopHeight = mHeight / 3 - mRadius - mTextHeight - mPadding * 2;

        mTotalWidth = mValueStr.length * mTimePadding;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //一个温度所占的高度
        float value = mTopHeight / (1.0f * (mMaxValue - mMinValue));

        int width = mTimePadding / 2;
        for (int i = 0; i < mValueStr.length; i++) {
            float height = (mValueStr[i] - mMinValue) * value;

            String str = mValueStr[i] + "°C";
            mPaintWeather.getTextBounds(str, 0, str.length(), mTextBounds);

            //画温度(在固定的一条高度上)
            //canvas.drawText(str, width - mTextBounds.width() / 2, mTextBounds.height(), mPaintWeather);

            //(随着温度高低画文字)
            canvas.drawText(str, width - mTextBounds.width() / 2, mTextHeight + mPadding + mTopHeight - height - mRadius - mPadding, mPaintWeather);

            //画圆
            canvas.drawCircle(width, mTextHeight + mPadding + mTopHeight - height, mRadius, mPaintCircle);

            //刚开始这条线不用画
            if (i > 0) {
                float leftHeight = mTextHeight + mPadding + mTopHeight - (mValueStr[i - 1] - mMinValue) * value;
                float leftWidth = width - mTimePadding;

                //根据两点计算折线相对圆要移动的高度,这样显得更好看(不计算这个就是直接连接到圆的中间点,难看)
                float moveHeight = mRadius * (mTextHeight + mPadding + mTopHeight - height - leftHeight) / (width - 2 * mRadius - leftWidth);

                //画温度折线
                canvas.drawLine(leftWidth + mRadius, leftHeight + moveHeight, width - mRadius, mTextHeight + mPadding + mTopHeight - height - moveHeight, mPaintCircle);

            }

            //污染等级
            int level = mLevelStr[i];
            level = CommonUtil.getWeatherLever(level);
            //拼接显示文字
            String text = mLevelStr[i] + " " + CommonUtil.getWeatherHint1(level).substring(0, 1);
            //计算污染文字的宽高
            mPaintWuRan.getTextBounds(text, 0, text.length(), mTextBounds);

            //设置画污染的位置
            mPaintWuRan.setColor(CommonUtil.getWeatherColor1(level));
            mRectF.set(width - mTextBounds.width() / 2 - 10, mHeight - mTextHeight - mPadding - 3 * mTextBounds.height() / 2 - 10,
                    width + mTextBounds.width() / 2 + 10, mHeight - mTextHeight - mPadding - mTextBounds.height() / 2 + 10);

            //画圆角矩形
            canvas.drawRoundRect(mRectF, 7, 7, mPaintWuRan);

            mPaintWuRan.setColor(Color.WHITE);
            //画污染显示文字
            canvas.drawText(text, width - mTextBounds.width() / 2, mHeight - mTextHeight - mPadding - mTextBounds.height() / 2 - 2, mPaintWuRan);

            //画风等级
            text = "3 级";
            mPaintFeng.getTextBounds(text, 0, text.length(), mTextBounds);

            //画风等级高度位置
            int fengHeight = mHeight - mTextHeight - 2 * mPadding - mTextBounds.height() - mTextBounds.height();
            canvas.drawText(text, width - mTextBounds.width() / 2, fengHeight + mTextBounds.height() / 2, mPaintFeng);

            //画向下的虚线
            if (mWeatherStr.contains(i)) {
                //画向下的虚线
                canvas.drawLine(width, mTextHeight + mPadding + mTopHeight - height + 2 * mRadius, width, fengHeight - 2 * mPadding, mPaintLine);

                //获取下标
                int index = mWeatherStr.indexOf(i);

                //从第二个开始判断
                if (index > 0) {
                    //左边线下标
                    int leftIndex = mWeatherStr.get(index - 1);
                    //左边线位置
                    int leftWidth = mTimePadding / 2 + leftIndex * mTimePadding;
                    //右边线位置
                    int rightWidth = width;

                    //这么判断右边贴边图片显示不出来
                    if (leftWidth <= getScrollX() + getMeasuredWidth() - mImageSize * 2) {

                        //不能超过左边屏幕(随屏幕滑动改变位置)
                        if (leftWidth - getScrollX() < 0) {
                            leftWidth = getScrollX();
                        }

                        //右边小于 滑动距离+屏幕宽度
                        if (rightWidth > getScrollX() + getMeasuredWidth()) {
                            rightWidth = getScrollX() + getMeasuredWidth();
                        }

                        //左边滑动到距离右边只有图片宽度时就不再变化
                        if (leftWidth >= rightWidth - mImageSize * 2) {
                            leftWidth = rightWidth - mImageSize * 2;
                        }

                        //计算中间点
                        int centerWidth = (leftWidth + rightWidth) / 2;

                        //超过屏幕范围就不画
                        if (centerWidth > 0 && leftWidth < getScrollX() + getMeasuredWidth()) {
                            mRectF.set(centerWidth - mImageSize, fengHeight - mImageSize * 2 - 3 * mPadding, centerWidth + mImageSize, fengHeight - 3 * mPadding);
                            //画天气图片
                            canvas.drawBitmap(mBitmap, null, mRectF, null);
                        }
                    }


                }
            }

            //画时间刻度
            canvas.drawLine(width, mHeight - mTextHeight, width, mHeight - mTextHeight + mRadius, mPaintTimeLine);
            //计算时间文字高宽
            mPaintTime.getTextBounds(mTimeArray[i], 0, mTimeArray[i].length(), mTextBounds);
            //画时间
            canvas.drawText(mTimeArray[i], width - mTextBounds.width() / 2, mHeight, mPaintTime);
            //
            width = width + mTimePadding;
        }
        //画时间刻度长度线
        canvas.drawLine(0, mHeight - mTextHeight, mTotalWidth, mHeight - mTextHeight, mPaintTimeLine);
    }

    private float mDownX, mDownY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = event.getX();
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float curX = event.getX();
                float curY = event.getY();

                //上下滑动幅度大于左右滑动,不做处理
                if (Math.abs(curY - mDownY) > Math.abs(curX - mDownX) + 15) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    return false;
                } else {
                    //上下滑动幅度小于左右滑动,提示父控件本次事件自己处理
                    getParent().requestDisallowInterceptTouchEvent(true);
                }

                int moveX = (int) (1.5 * (mDownX - curX));
//                Log.i("aaa", getScrollX() + "  " + mTotalWidth + "  " + ((mDownX - curX) + getScrollX()));
                if (getScrollX() + moveX >= 0 && getScrollX() + moveX <= mTotalWidth - getMeasuredWidth()) {
                    scrollBy(moveX, 0);
                }

                mDownX = curX;
                break;
        }
        return true;
    }
}
