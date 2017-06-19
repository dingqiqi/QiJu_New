package com.dingqiqi.qijuproject.view;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.AbsListView.OnScrollListener;

import com.dingqiqi.qijuproject.adapter.LoopPageAdapter;

public class LoopViewPager extends ViewPager {

    private LoopPageAdapter mAdapter;
    private OnPageChangeListener mOutChangeListener;

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoopViewPager(Context context) {
        super(context);
        init();
    }

    private void init() {
        super.setOnPageChangeListener(mPageChangeListener);
    }

    @Override
    public void setAdapter(PagerAdapter arg0) {
        mAdapter = (LoopPageAdapter) arg0;
        super.setAdapter(mAdapter);
    }

    @Override
    public void setOnPageChangeListener(OnPageChangeListener listener) {
        mOutChangeListener = listener;
    }

    private OnPageChangeListener mPageChangeListener = new OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
            int position = arg0;
            if (mAdapter != null) {
                position = mAdapter.getRealPosition(arg0);
            }

            if (mOutChangeListener != null) {
                mOutChangeListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            int position = arg0;
            if (mAdapter != null) {
                position = mAdapter.getRealPosition(arg0);
            }

            if (mOutChangeListener != null) {
                mOutChangeListener.onPageScrolled(position, arg1, arg2);
            }
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
            if (arg0 == OnScrollListener.SCROLL_STATE_IDLE) {
                //Log.i("aaa", "-->" + getCurrentItem());
                // 跳最后一页
                if (getCurrentItem() == 0) {
                    if (mAdapter != null) {
                        setCurrentItem(mAdapter.getCount() - 2, false);
                    }

                    // 跳第一页
                } else if (getCurrentItem() == mAdapter.getCount() - 1) {
                    if (mAdapter != null) {
                        setCurrentItem(1, false);
                    }
                }
            }

            if (mOutChangeListener != null) {
                mOutChangeListener.onPageScrollStateChanged(arg0);
            }

        }
    };

}
