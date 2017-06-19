package com.dingqiqi.qijuproject.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.dingqiqi.qijuproject.util.DisplayUtil;

/**
 * Created by dingqiqi on 2017/5/25.
 */
public class LineChartView extends View {

    private Context mContext;
    private Paint mPaintLine;
    private Paint mPaintPoint;
    private Paint mPaintXYLine;
    private Paint mPaintText;

    private int mColorLine;
    private int mColorPoint;

    //控件上下左右间距
    private int mPaddingWidthLength = 40;
    private int mPaddingHeightLength = 30;
    /**
     * 轴坐标个数
     */
    private float mXLineCount = 5f, mYLineCount = 5f;
    //控件的实际宽高
    private int mWidth, mHeight;
    //用于计算字体大小
    private Rect mTextBounds;
    //xy轴文字
    private String[] mXLabel, mYLabel;
    //刻度长度
    private int mKeyHieght = 4;
    //偏移量,使得xy轴圆点相交在一起
    private int mMoveValue = -3;
    //Y数据
    private float[] mData = new float[]{1, 4, 2, 5, 3, 2, 1, 4, 1};
    //xy轴刻度平均值
    private float mXValue, mYValue;
    //路径
    private Path mPathLine;
    //按下的x值
    private float startX;
    //x轴滑动偏移量
    private float mTransX;
    //xy轴单位
    private String mXEnd, mYEnd;
    //是否画y的原点
    private int mIsDrawStartY;

    public LineChartView(Context context) {
        super(context);
        intiView(context);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        intiView(context);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intiView(context);
    }

    private void intiView(Context context) {
        mContext = context;

        mTextBounds = new Rect();

        mColorLine = Color.WHITE;
        mColorPoint = Color.RED;

        mPaintXYLine = new Paint();
        mPaintXYLine.setAntiAlias(true);
        mPaintXYLine.setStyle(Paint.Style.FILL);
        mPaintXYLine.setColor(mColorLine);
        mPaintXYLine.setStrokeWidth(DisplayUtil.dp2px(mContext, 3));
        mPaintXYLine.setTextSize(DisplayUtil.dp2px(mContext, 16));

        mPaintPoint = new Paint(mPaintXYLine);
        mPaintPoint.setColor(mColorPoint);
        mPaintPoint.setStrokeWidth(DisplayUtil.dp2px(mContext, 30));

        mPaintLine = new Paint(mPaintXYLine);
        mPaintLine.setStyle(Paint.Style.STROKE);
        mPaintLine.setStrokeWidth(DisplayUtil.dp2px(mContext, 2));

        mPaintText = new Paint(mPaintLine);
        mPaintText.setStyle(Paint.Style.FILL);
        mPaintText.setStrokeWidth(DisplayUtil.dp2px(mContext, 2));

        mPaddingWidthLength = DisplayUtil.dp2px(mContext, mPaddingWidthLength);
        mPaddingHeightLength = DisplayUtil.dp2px(mContext, mPaddingHeightLength);

        mXLabel = mYLabel = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};

        mKeyHieght = DisplayUtil.dp2px(mContext, mKeyHieght);

        mPathLine = new Path();
    }

    public void setColorLine(int mColorLine) {
        this.mColorLine = mColorLine;
        postInvalidate();
    }

    public void setColorPoint(int mColorPoint) {
        this.mColorPoint = mColorPoint;
        postInvalidate();
    }

    public void setData(String[] mXLabel, float[] mData, int mYLineCount, boolean isDrawStartY) {
        if (mXLabel.length == 0 || mData.length == 0 || mYLineCount <= 0) {
            throw new IllegalArgumentException("入参格式错误");
        }

        if (isDrawStartY) {
            mIsDrawStartY = -1;
        } else {
            mIsDrawStartY = 0;
        }

        scrollTo(0, 0);
        int min, max;
        min = max = (int) mData[0];

        int length = mData.length;
        for (int i = 0; i < length; i++) {
            if (min > mData[i]) {
                min = (int) mData[i];
            }
            if (max < mData[i]) {
                max = (int) mData[i];
            }
        }

        int value = (int) Math.ceil((max - min) / (mYLineCount * 1f));

        mYLabel = new String[length + 1];
        //最低刻度标上
        mYLabel[0] = String.valueOf(min);
        for (int i = 0; i < length; i++) {
            mYLabel[i - mIsDrawStartY] = String.valueOf(min + (i + 1) * value);
            mData[i] = (mData[i] - min) / value;
        }

        this.mXLabel = mXLabel;
        this.mData = mData;
        this.mYLineCount = mYLineCount * 1f;

        postInvalidate();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mWidth = getMeasuredWidth() - 2 * mPaddingWidthLength;
        mHeight = getMeasuredHeight() - 2 * mPaddingHeightLength;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(mPaddingWidthLength, mHeight + mPaddingHeightLength);
        drawXYLine(canvas);

        drawDataLine(canvas);

        canvas.restore();
    }

    /**
     * 画xy轴
     *
     * @param canvas
     */
    private void drawXYLine(Canvas canvas) {
        //x轴
        canvas.drawLine(mMoveValue, 0, mWidth + getScrollX() - mMoveValue, 0, mPaintXYLine);
        //y轴
        canvas.drawLine(0, mMoveValue, 0, -mHeight + 2 * mMoveValue, mPaintXYLine);

        //x轴平均刻度
        mXValue = mWidth / mXLineCount;
        //一个页面最长五个刻度
        if (mData.length > mXLineCount) {
            mXValue = mWidth / 5;
        }

        //可移动的宽度
        mTransX = mXValue * (mData.length - 1) - mWidth - mMoveValue + mPaddingWidthLength;

        for (int i = 0; i < mData.length - 1; i++) {
            if ((i + 1) * mXValue > mWidth + getScrollX()) {
                break;
            }

            mPaintText.getTextBounds(mXLabel[i], 0, mXLabel[i].length(), mTextBounds);
            //画x轴刻度
            canvas.drawLine((i + 1) * mXValue + mMoveValue, 0, (i + 1) * mXValue + mMoveValue, mKeyHieght, mPaintText);
            //画x轴文字
            canvas.drawText(mXLabel[i + 1], (i + 1) * mXValue - mTextBounds.width() / 2 + mMoveValue, mPaddingHeightLength / 2 + mTextBounds.height() / 2, mPaintText);
        }

        //y轴平均刻度
        mYValue = -mHeight / (mYLineCount);

        for (int i = 0; i < mYLineCount - mIsDrawStartY; i++) {
            mPaintText.getTextBounds(mYLabel[i], 0, mYLabel[i].length(), mTextBounds);
            //画y轴文字
            canvas.drawText(mYLabel[i], -mTextBounds.width() / 2 - mPaddingWidthLength / 2, (i + 1 + mIsDrawStartY) * mYValue + mTextBounds.height() / 2 - mMoveValue, mPaintText);

            //画y轴刻度(从原点开始画的话最后一个就不画)
            if (mIsDrawStartY == -1) {
                if (i == mYLineCount - 1) {
                    break;
                }
            }
            //画y轴刻度
            canvas.drawLine(0, (i + 1) * mYValue, -mKeyHieght, (i + 1) * mYValue, mPaintText);
        }

        mXEnd = "(d)";
        mPaintText.getTextBounds(mXEnd, 0, mXEnd.length(), mTextBounds);
        canvas.drawText(mXEnd, getScrollX() + mWidth + mTextBounds.width() / 2 + 2 * mMoveValue, -mTextBounds.height() / 2, mPaintText);

        mYEnd = "(aqi)";
        mPaintText.getTextBounds(mYEnd, 0, mYEnd.length(), mTextBounds);
        canvas.drawText(mYEnd, -mTextBounds.width() / 2, -mHeight - mTextBounds.height() / 2 + 2 * mMoveValue, mPaintText);
    }

    private void drawDataLine(Canvas canvas) {
        int length = mData.length;

        mPathLine.reset();
        mPathLine.moveTo(0, mData[0] * mYValue - mMoveValue);

        for (int i = 0; i < length - 1; i++) {
            float startX = i * mXValue;
            float startY = mData[i] * mYValue;
            float endX = (i + 1) * mXValue;
            float endY = mData[i + 1] * mYValue;

            float centerX = (startX + endX) / 2 + mMoveValue;

            float flag = startY > endY ? 1.3f : 0.7f;

            float centerY = (startY + endY) / 2 * flag;

            if (endX > mWidth + getScrollX()) {
                break;
            }
//            mPathLine.quadTo(centerX, endY, endX + mMoveValue, endY);
            mPathLine.lineTo(endX, endY);
        }

        canvas.drawPath(mPathLine, mPaintLine);

        for (int i = 1; i < length; i++) {
            if (i * mXValue > mWidth + getScrollX()) {
                break;
            }
            canvas.drawCircle(i * mXValue + mMoveValue, mData[i] * mYValue, 5, mPaintPoint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                float curX = event.getX();
                float moveX = curX - startX;

                if (moveX > 0) {
                    if (getScrollX() - moveX >= 0) {
                        scrollBy((int) -moveX, 0);
                        postInvalidate();
                        startX = curX;
                    }
                } else {
                    if (getScrollX() - moveX <= mTransX) {
                        scrollBy((int) -moveX, 0);
                        postInvalidate();
                        startX = curX;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }

        return true;
    }
}
