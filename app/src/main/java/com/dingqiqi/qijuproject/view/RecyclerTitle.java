package com.dingqiqi.qijuproject.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.mode.BaseMode;
import com.dingqiqi.qijuproject.mode.MenuFilterMode;
import com.dingqiqi.qijuproject.util.DisplayUtil;

import java.util.List;

/**
 * RecyclerView标题 间隔线
 * Created by Administrator on 2017/7/20.
 */
@SuppressWarnings("deprecation")
public class RecyclerTitle extends RecyclerView.ItemDecoration {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 主标题列表
     */
    private List<? extends BaseMode> mMenuList;
    /**
     * 总列表
     */
    private List<? extends BaseMode> mList;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * 分割线高度
     */
    private int mHeight;
    /**
     * 标题高度
     */
    private int mTitleHeight;
    /**
     * 计算文字宽高
     */
    private Rect mTextBounds = new Rect();

    public RecyclerTitle(Context mContext, List<? extends BaseMode> mMenuList, List<? extends BaseMode> mList) {
        this.mContext = mContext;
        this.mMenuList = mMenuList;
        this.mList = mList;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mContext.getResources().getColor(R.color.colorLine));
        mPaint.setTextSize(DisplayUtil.dp2px(mContext, 17));

        mHeight = DisplayUtil.dp2px(mContext, 1);
        mTitleHeight = DisplayUtil.dp2px(mContext, 38);
    }

    /**
     * 获取标题高度
     * @return
     */
    public int getTitleHeight(){
        return mTitleHeight;
    }

    /**
     * 获取分割线高度
     * @return
     */
    public int getDividingHeight(){
        return mHeight;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int count = parent.getChildCount();

        int left = parent.getPaddingLeft();
        int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        int top, bottom;

        for (int i = 0; i < count; i++) {
            //是否画标题
            boolean isDrawTitle;
            //获取当前view下标
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) parent.getChildAt(i).getLayoutParams();
            int pos = params.getViewLayoutPosition();
            int marginTop = params.topMargin;

            //下标为0一定画标题
            if (pos == 0) {
                top = parent.getChildAt(i).getTop() - marginTop - mTitleHeight;
                bottom = top + mTitleHeight;

                isDrawTitle = true;
            } else {
                MenuFilterMode curMode = (MenuFilterMode) mList.get(pos);
                MenuFilterMode topMode = (MenuFilterMode) mList.get(pos - 1);

                //上下标题不一致 画标题
                if (topMode != null && curMode != null && !TextUtils.isEmpty(topMode.getCtgId()) && !topMode.getParentId().equals(curMode.getParentId())) {
                    top = parent.getChildAt(i).getTop() - marginTop - mTitleHeight;
                    bottom = top + mTitleHeight;

                    isDrawTitle = true;
                } else {
                    top = parent.getChildAt(i).getTop() - marginTop - mHeight;
                    bottom = top + mHeight;

                    isDrawTitle = false;
                }
            }

            if (isDrawTitle) {
                mPaint.setColor(mContext.getResources().getColor(R.color.colorLine));
            } else {
                mPaint.setColor(mContext.getResources().getColor(R.color.colorText));
            }
            c.drawRect(left, top, right, bottom, mPaint);

            //画标题
            if (isDrawTitle) {
                MenuFilterMode curMode = (MenuFilterMode) mList.get(pos);
                if (curMode != null) {
                    String text = curMode.getName();

                    //查找总分类
                    for (BaseMode mode : mMenuList) {
                        MenuFilterMode menuMode = (MenuFilterMode) mode;

                        //找到分类相同的名称
                        if (curMode.getParentId().equals(menuMode.getCtgId())) {
                            text = menuMode.getName();
                            break;
                        }
                    }

                    mPaint.getTextBounds(text, 0, text.length(), mTextBounds);
                    mPaint.setColor(mContext.getResources().getColor(R.color.colorBlack));
                    //文字左对齐
                    c.drawText(text, left + DisplayUtil.dp2px(mContext, 16), (top + bottom) / 2 + mTextBounds.height() / 2, mPaint);
                }
            }
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
        boolean flag = false;

        //获取显示的下标
        int pos = 0;
        if (parent.getLayoutManager() instanceof LinearLayoutManager) {
            pos = ((LinearLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();

        } else if (parent.getLayoutManager() instanceof GridLayoutManager) {
            pos = ((GridLayoutManager) parent.getLayoutManager()).findFirstVisibleItemPosition();
        }

        //初始时候会有-1
        if (pos < 0) {
            return;
        }

        int left = parent.getPaddingLeft();
        int top = parent.getTop();
        int right = parent.getMeasuredWidth() - parent.getPaddingRight();
        int bottom = top + mTitleHeight;

        //获取当前的View
        View child = parent.findViewHolderForAdapterPosition(pos).itemView;

        //不是最后一个
        if (mList.size() > pos - 1) {
            MenuFilterMode curMode = (MenuFilterMode) mList.get(pos);
            MenuFilterMode nextMode = (MenuFilterMode) mList.get(pos + 1);

            //两个标题不一样 进行切换
            if (!curMode.getParentId().equals(nextMode.getParentId())) {

                if (child.getTop() + child.getHeight() < mTitleHeight) {
                    c.save();
                    flag = true;

                    //上推风格
                    c.translate(0, child.getTop() + child.getHeight() - mTitleHeight);

                    //覆盖风格
                    //c.clipRect(left, top, right, top + child.getTop() + child.getHeight());
                }
            }
        }

        //画边框
        mPaint.setColor(mContext.getResources().getColor(R.color.colorLine));
        c.drawRect(left, top, right, bottom, mPaint);

        MenuFilterMode curMode = (MenuFilterMode) mList.get(pos);
        if (curMode != null) {
            String text = curMode.getName();
            //查找总分类
            for (BaseMode mode : mMenuList) {
                MenuFilterMode menuMode = (MenuFilterMode) mode;

                //找到分类相同的名称
                if (curMode.getParentId().equals(menuMode.getCtgId())) {
                    text = menuMode.getName();
                    break;
                }
            }

            mPaint.getTextBounds(text, 0, text.length(), mTextBounds);
            mPaint.setColor(mContext.getResources().getColor(R.color.colorBlack));
            //文字左对齐
            c.drawText(text, left + DisplayUtil.dp2px(mContext, 16), (top + bottom) / 2 + mTextBounds.height() / 2, mPaint);
        }

        if (flag) {
            c.restore();
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //当前pos
        int pos = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewLayoutPosition();

        if (pos > -1) {
            //第一个肯定有标题
            if (pos == 0) {
                outRect.set(0, mTitleHeight, 0, 0);
            } else {
                MenuFilterMode curMode = (MenuFilterMode) mList.get(pos);
                MenuFilterMode topMode = (MenuFilterMode) mList.get(pos - 1);
                //上下不一致有标题
                if (topMode != null && curMode != null && !TextUtils.isEmpty(topMode.getCtgId()) && !topMode.getParentId().equals(curMode.getParentId())) {
                    outRect.set(0, mTitleHeight, 0, 0);
                } else {
                    //上下一致分割线
                    outRect.set(0, mHeight, 0, 0);
                }
            }
        }

    }

}
