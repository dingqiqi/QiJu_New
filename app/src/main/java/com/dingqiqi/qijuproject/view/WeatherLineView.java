package com.dingqiqi.qijuproject.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.util.DisplayUtil;

/**
 * Created by dingqiqi on 2017/6/9.
 */
public class WeatherLineView extends View {

    private Context mContext;
    //文字画笔
    private Paint mPaintText;
    //圆画笔
    private Paint mPaintCircle;
    //线画笔
    private Paint mPaintLine;
    //文字大小
    private int mTextSize = 16;
    //线宽度
    private int mLineStroke = 1;
    //圆半径
    private int mRadius = 3;
    //线距离圆间距
    private int mPadding = 2;
    //文字圆圈间距
    private int mPaddingValue = 13;
    //计算文字长宽
    private Rect mTextBounds = new Rect();
    //最大最小数据
    private int mMaxNum = 30;
    private int mMinNum = 20;
    //当前温度
    private int mCurMaxNum = 30;
    private int mCurMinNum = 20;
    //单位
    private final String mValueEnd = "℃";

    //最大最小温度
    private String mMaxValue;
    private String mMinValue;
    //中间可用高度
    private int mHeight;
    //是否画左边线,右边线
    private boolean mIsDrawLeft = true, mIsDrawRight = true;
    //左右线y轴
    private float mLeftMaxY, mLeftMinY, mRightMaxY, mRightMinY;

    public WeatherLineView(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public WeatherLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public WeatherLineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        mTextSize = DisplayUtil.dp2px(mContext, mTextSize);
        mLineStroke = DisplayUtil.dp2px(mContext, mLineStroke);
        mPaddingValue = DisplayUtil.dp2px(mContext, mPaddingValue);
        mRadius = DisplayUtil.dp2px(mContext, mRadius);
        mPadding = DisplayUtil.dp2px(mContext, mPadding);

        mPaintText = new Paint();
        mPaintText.setAntiAlias(true);
        mPaintText.setColor(mContext.getResources().getColor(R.color.colorBlackGray));
        mPaintText.setStyle(Paint.Style.FILL);
        mPaintText.setTextSize(mTextSize);

        mPaintLine = new Paint(mPaintText);
        mPaintLine.setStrokeWidth(mLineStroke);
        mPaintLine.setColor(mContext.getResources().getColor(R.color.colorLine));

        mPaintCircle = new Paint(mPaintLine);
        mPaintCircle.setStyle(Paint.Style.STROKE);
    }

    /**
     * 设置数据
     *
     * @param leftMaxY    左边线最大y轴
     * @param rightMaxY   右边线最大y轴
     * @param leftMinY    左边线最小y轴
     * @param rightMinY   右边线最小y轴
     * @param isDrawLeft  是否画左边线
     * @param isDrawRight 是否画右边线
     * @param curMinNum   当前最小数据
     * @param curMaxNum   当前最大数据
     * @param minNum      全局最小数据
     * @param maxNum      全局最大数据
     */
    public void setData(boolean isDrawLeft, boolean isDrawRight, float leftMaxY, float rightMaxY, float leftMinY,
                        float rightMinY, int curMinNum, int curMaxNum, int minNum, int maxNum) {
        this.mLeftMaxY = leftMaxY;
        this.mRightMaxY = rightMaxY;
        this.mLeftMinY = leftMinY;
        this.mRightMinY = rightMinY;
        this.mIsDrawLeft = isDrawLeft;
        this.mIsDrawRight = isDrawRight;
        this.mCurMinNum = curMinNum;
        this.mCurMaxNum = curMaxNum;
        this.mMinNum = minNum;
        this.mMaxNum = maxNum;
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //总高度-文字距离圆的高度(2个) - 2个圆半径(先减去半个圆高度是为了画的时候不用考虑在最上面或者最下面时圆显示不全)
        mHeight = getMeasuredHeight() - 2 * mPaddingValue - 2 * mRadius;
        //计算文字高度
        mPaintText.getTextBounds("测试", 0, "测试".length(), mTextBounds);
        //高度减去文字高度(获取最中间的高度)
        mHeight = mHeight - mTextBounds.height() * 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //获取要画的文字
        mMaxValue = mCurMaxNum + mValueEnd;
        mMinValue = mCurMinNum + mValueEnd;

        //计算文字高宽
        mPaintText.getTextBounds(mMaxValue, 0, mMaxValue.length(), mTextBounds);
        //获取最高温度占比高
        float value = (mCurMaxNum - mMinNum) / (1f * (mMaxNum - mMinNum)) * mHeight;

        //上面圆高度
        float height = mPaddingValue + mTextBounds.height() + mHeight - value + mPadding;

        //画上面文字
        canvas.drawText(mMaxValue, getMeasuredWidth() / 2 - mTextBounds.width() / 2, height - mPaddingValue + mPadding, mPaintText);

        //是否画左边的线
        if (mIsDrawLeft) {
            float leftValue = mHeight - ((mLeftMaxY + mCurMaxNum) / 2 - mMinNum) / (1f * (mMaxNum - mMinNum)) * mHeight + mTextBounds.height() + mPaddingValue + mPadding;
            //距离圆padding距离的高度
            float h = (mRadius + mPadding) * (height - leftValue) / (1f * getMeasuredWidth() / 2);
            //画左边线
            canvas.drawLine(0, leftValue, getMeasuredWidth() / 2 - mRadius - mPadding, height - h, mPaintLine);
        }

        //是否画右边的线
        if (mIsDrawRight) {
            //计算右边y轴高度
            float rightValue = mHeight - ((mRightMaxY + mCurMaxNum) / 2 - mMinNum) / (1f * (mMaxNum - mMinNum)) * mHeight + mTextBounds.height() + mPaddingValue + mPadding;
            //距离圆padding距离的高度
            float h = (mRadius + mPadding) * (height - rightValue) / (1f * getMeasuredWidth() / 2);
            //画右边线
            canvas.drawLine(getMeasuredWidth() / 2 + mRadius + mPadding, height - h, getMeasuredWidth(), rightValue, mPaintLine);
        }

        //画最高温圆(上面圆)
        canvas.drawCircle(getMeasuredWidth() / 2, height, mRadius, mPaintCircle);

        //最高温度最低温度相差高度
        value = (mCurMaxNum - mCurMinNum) / (1f * (mMaxNum - mMinNum)) * mHeight;
        height = height + value;

        //是否画左边的线
        if (mIsDrawLeft) {
            //中间高度+文字高度 + 文字与圆之间间距+ 文字距top间距 - (y轴占比高度)
            float leftValue = mHeight + mTextBounds.height() + mPaddingValue + mPadding - ((mLeftMinY + mCurMinNum) / 2 - mMinNum) / (1f * (mMaxNum - mMinNum)) * mHeight;
            //距离圆padding距离的高度
            float h = (mRadius + mPadding) * (height - leftValue) / (1f * getMeasuredWidth() / 2);
            //画左边线
            canvas.drawLine(0, leftValue, getMeasuredWidth() / 2 - mRadius - mPadding, height - h, mPaintLine);
        }

        //是否画右边的线
        if (mIsDrawRight) {
            //中间高度+文字高度 + 文字与圆之间间距+ 文字距top间距 - (y轴占比高度)
            float rightValue = mHeight + mTextBounds.height() + mPaddingValue + mPadding - ((mRightMinY + mCurMinNum) / 2 - mMinNum) / (1f * (mMaxNum - mMinNum)) * mHeight;
            //距离圆padding距离的高度
            float h = (mRadius + mPadding) * (height - rightValue) / (1f * getMeasuredWidth() / 2);
            //画右边线
            canvas.drawLine(getMeasuredWidth() / 2 + mRadius + mPadding, height - h, getMeasuredWidth(), rightValue, mPaintLine);
        }

        //画下面圆
        canvas.drawCircle(getMeasuredWidth() / 2, height, mRadius, mPaintCircle);
        //获取文字高宽
        mPaintText.getTextBounds(mMinValue, 0, mMinValue.length(), mTextBounds);
        //画最低温度文字
        canvas.drawText(mMinValue, getMeasuredWidth() / 2 - mTextBounds.width() / 2, height + mPaddingValue + mTextBounds.height(), mPaintText);
    }
}
