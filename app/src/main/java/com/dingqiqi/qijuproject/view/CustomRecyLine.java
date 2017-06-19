package com.dingqiqi.qijuproject.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dingqiqi.qijuproject.util.DisplayUtil;

/**
 * RecyclerView画间隔线类
 * Created by dingqiqi on 2016/12/27.
 */
public class CustomRecyLine extends RecyclerView.ItemDecoration {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 间隔线 0 水平 1 垂直 2 水平垂直
     */
    private int mOrien = 0;
    /**
     * 间隔线高度
     */
    private int mHeight;
    /**
     * 间隔线画笔
     */
    private Paint mPaint;

    public CustomRecyLine(Context mContext) {
        this.mContext = mContext;
        mHeight = DisplayUtil.dp2px(mContext, 1);

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
    }

    public void setOrien(int orien) {
        this.mOrien = orien;
    }

    //画最上面一条
    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    //画中间的间隔线
    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int count = parent.getChildCount();

        if (mOrien == 0) {
            int left = parent.getPaddingLeft();
            int right = left + parent.getMeasuredWidth();

            for (int i = 0; i < count; i++) {
                int top = parent.getChildAt(i).getBottom();
                int bottom = top + mHeight;

                c.drawRect(left, top, right, bottom, mPaint);
            }
        } else if (mOrien == 1) {
            int top = parent.getPaddingTop();
            int bottom = top + parent.getMeasuredHeight();

            for (int i = 0; i < count; i++) {
                int left = parent.getChildAt(i).getRight();
                int right = left + mHeight;

                c.drawRect(left, top, right, bottom, mPaint);
            }
        } else {
            int left = parent.getPaddingLeft();
            int right = left + parent.getMeasuredWidth();

            for (int i = 0; i < count; i++) {
                int top = parent.getChildAt(i).getBottom();
                int bottom = top + mHeight;

                c.drawRect(left, top, right, bottom, mPaint);
            }

            int top = parent.getPaddingTop();
            int bottom = top + parent.getMeasuredHeight();

            for (int i = 0; i < count; i++) {
                left = parent.getChildAt(i).getRight();
                right = left + mHeight;

                c.drawRect(left, top, right, bottom, mPaint);
            }

        }

    }

    //设置间隔线高度
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mOrien == 0) {
            outRect.set(0, 0, 0, mHeight);
        } else if (mOrien == 1) {
            outRect.set(0, 0, mHeight, 0);
        } else {
            outRect.set(0, 0, mHeight, mHeight);
        }
    }
}
