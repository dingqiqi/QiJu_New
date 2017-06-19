package com.dingqiqi.qijuproject.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.util.DisplayUtil;

public class ScrollColorBtn extends View {
    //文字大小
    private float mTextSize;
    /**
     * 颜色字体画笔
     */
    private Paint mPaint;
    /**
     * 黑色字体画笔
     */
    private Paint mPaintText;
    /**
     * 要画的字
     */
    private String mText = "";
    /**
     * 测量字
     */
    private Rect mTextBounds;
    /**
     * 画的图
     */
    private Bitmap mBitmap;
    /**
     * 图的左边距离
     */
    private int mLeft;
    /**
     * 图的上边距离
     */
    private int mTop;
    /**
     * 透明度
     */
    private int mAlpha = 0;
    /**
     * 颜色
     */
    private int mColor;
    //间隔
    private int mPadding = 6;

    /**
     * 设置透明度，最大的是255
     *
     * @param alpha
     */
    public void setIconAlpha(float alpha) {
        this.mAlpha = (int) Math.ceil(255 * alpha);

        mPaint.setAlpha(mAlpha);

        invalidate();
    }

    public ScrollColorBtn(Context context) {
        super(context, null);
        initView(context);
    }

    public ScrollColorBtn(Context context, AttributeSet attrs) {
        super(context, attrs);
        /**
         * 获取自定义属性
         */
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.scrollColorBtn);

        mTextSize = array.getDimension(R.styleable.scrollColorBtn_btnTextSize, 16);
        mText = array.getString(R.styleable.scrollColorBtn_btnText);
        mColor = array.getColor(R.styleable.scrollColorBtn_btnColor, getResources().getColor(R.color.colorPrimary));
        /**
         * 获取图片
         */
        BitmapDrawable bitmapDrawable = (BitmapDrawable) array
                .getDrawable(R.styleable.scrollColorBtn_btnIcon);
        if (bitmapDrawable != null) {
            mBitmap = bitmapDrawable.getBitmap();
        }

        array.recycle();
        initView(context);
    }

    private void initView(Context context) {
        mPadding = DisplayUtil.dp2px(context, mPadding);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mColor);

        mPaintText = new Paint(mPaint);
        mPaintText.setColor(Color.BLACK);

        mTextBounds = new Rect();
        mPaint.getTextBounds(mText, 0, mText.length(), mTextBounds);
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //计算图片宽度，高度
        int mBitmapWidth = Math.min(getMeasuredWidth() - getPaddingLeft()
                - getPaddingRight(), getMeasuredHeight() - getPaddingBottom()
                - getPaddingTop() - mTextBounds.height() - mTextBounds.height()
                / 2);

        if (mBitmap != null) {
            mBitmapWidth = Math.max(mBitmap.getWidth(), mBitmap.getHeight());
        }

        //计算图片左边距，上边距
        mLeft = getMeasuredWidth() / 2 - mBitmapWidth / 2;

        mTop = mPadding;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //话底层图片
        canvas.drawBitmap(mBitmap, mLeft, mTop, null);
        //获取上层图片
        Bitmap bitmap = drawSrcBitmap();
        //画上层图片，覆盖底层图片
        canvas.drawBitmap(bitmap, 0, 0, null);

        //画底层文字
        drawBgText(canvas);
        //画上层文字
        drawSrcText(canvas);
    }

    /**
     * 利用XferMode，获取到图片对应的形状
     *
     * @return
     */
    private Bitmap drawSrcBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getMeasuredWidth(),
                getMeasuredHeight(), Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mColor);
        paint.setAlpha(mAlpha);

        canvas.drawRect(mLeft, mTop, mLeft + mBitmap.getWidth(),
                mTop + mBitmap.getHeight(), paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));

        canvas.drawBitmap(mBitmap, mLeft, mTop, paint);

        return bitmap;
    }

    /**
     * 画黑色字
     *
     * @param canvas
     */
    private void drawBgText(Canvas canvas) {
        canvas.drawText(mText,
                getMeasuredWidth() / 2 - mTextBounds.width() / 2, getMeasuredHeight() - mPadding, mPaintText);
    }

    /**
     * 画带颜色字
     *
     * @param canvas
     */
    private void drawSrcText(Canvas canvas) {
        mPaint.setAlpha(mAlpha);
        canvas.drawText(mText,
                getMeasuredWidth() / 2 - mTextBounds.width() / 2, getMeasuredHeight() - mPadding, mPaint);
    }

}
