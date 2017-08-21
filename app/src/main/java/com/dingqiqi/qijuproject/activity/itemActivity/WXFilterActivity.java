package com.dingqiqi.qijuproject.activity.itemActivity;

import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.activity.BaseActivity;
import com.dingqiqi.qijuproject.adapter.FragmentAdapter;
import com.dingqiqi.qijuproject.fragment.BaseFragment;
import com.dingqiqi.qijuproject.fragment.WXFilterFragment;
import com.dingqiqi.qijuproject.http.IWXTitleApi;
import com.dingqiqi.qijuproject.manager.RetrofitManager;
import com.dingqiqi.qijuproject.mode.WXTitleMode;
import com.dingqiqi.qijuproject.util.CommonUtil;
import com.dingqiqi.qijuproject.util.DisplayUtil;
import com.dingqiqi.qijuproject.util.LogUtil;
import com.dingqiqi.qijuproject.util.SharedPreUtil;
import com.dingqiqi.qijuproject.util.TimeUtil;
import com.dingqiqi.qijuproject.util.ToastUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 精选
 * Created by 丁奇奇 on 2017/8/2.
 */
public class WXFilterActivity extends BaseActivity {
    //标题父控件
    private LinearLayout mLinearLayout;
    //滑动的线条
    private View mLineView;
    //标题数据源
    private List<WXTitleMode> mTitleList;
    //每个标题宽度
    private List<Integer> mWidthList;
    //文字左右宽度和
    private int mWidth;

    private ViewPager mViewPager;
    //标题水平滑动控件
    private HorizontalScrollView mScrollView;
    //水平滑动的距离
    private int mScrollX;

    private FragmentAdapter mAdapter;

    private List<BaseFragment> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wx_filter_layout);

        mWidth = DisplayUtil.dp2px(this, 20);

        initView();

        getTitleData();
        initTitleView(0);
    }

    /**
     * 初始化数据
     */
    private void initView() {
        setTitle("文章精选");

        mTitleList = new ArrayList<>();
        mWidthList = new ArrayList<>();

        mLineView = findViewById(R.id.view_line);
        mLinearLayout = (LinearLayout) findViewById(R.id.linear_title);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_wx_filter);
        mScrollView = (HorizontalScrollView) findViewById(R.id.scroll_view_wx_title_filter);

        mList = new ArrayList<>();
        mAdapter = new FragmentAdapter(getSupportFragmentManager(), mList);
        mViewPager.setAdapter(mAdapter);

        mLineView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mWidthList.size() > 0) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLineView.getLayoutParams();
                    params.leftMargin = mWidth / 2;
                    params.width = mWidthList.get(0) - mWidth;
                    mLineView.setLayoutParams(params);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mLineView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //划得动的时候
                if (positionOffset > 0) {
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLineView.getLayoutParams();
                    params.leftMargin = (int) (getScrollX(position, true) + mWidthList.get(position) * positionOffset);

                    int width = mWidthList.get(position) - mWidth;
                    int nextWidth = mWidthList.get(position + 1) - mWidth;
                    params.width = (int) (width + (nextWidth - width) * positionOffset);
                    mLineView.setLayoutParams(params);
                }
            }

            @Override
            public void onPageSelected(int position) {
                //手指向左边滑动超过三次scrollView跟着滑动
                mScrollView.scrollTo(getScrollX(position - 3, false), 0);
                initTitleView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    /**
     * 获取当前滑动的距离
     *
     * @param pos  当前下标
     * @param flag 是否是横线滑动
     */
    private int getScrollX(int pos, boolean flag) {
        int mScrollX = 0;
        if (flag) {
            mScrollX = mWidth / 2;
        }

        for (int i = 0; i < pos; i++) {
            mScrollX = mScrollX + mWidthList.get(i);
        }

        return mScrollX;
    }

    /**
     * 获取标题数据(一天一次)
     */
    private void getTitleData() {
        final SharedPreUtil util = SharedPreUtil.getInstance(this, SharedPreUtil.mWXTitleFileName);

        String time = util.getString(SharedPreUtil.mWXTitleSaveTimeKey, "");
        if (TimeUtil.getCompareCurTime(time) == 0) {
            String json = util.getString(SharedPreUtil.mWXTitleKey, "");

            if (!TextUtils.isEmpty(json)) {
                WXTitleMode titleMode = new Gson().fromJson(json, WXTitleMode.class);

                mTitleList.clear();
                mTitleList.addAll(titleMode.getResult());

                initViewPager();
                return;
            }
        }

        showProgress();
        IWXTitleApi titleApi = RetrofitManager.getInstance(this).getRetrofit().create(IWXTitleApi.class);
        Call<WXTitleMode> titleModeCall = titleApi.getWXTitleData(CommonUtil.getKey());
        titleModeCall.enqueue(new Callback<WXTitleMode>() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onResponse(Call<WXTitleMode> call, Response<WXTitleMode> response) {
                dismissProgress();
                if (response.body() != null && response.body().getResult() != null) {
                    String textJson = new Gson().toJson(response.body());
                    util.put(SharedPreUtil.mWXTitleKey, textJson);
                    util.put(SharedPreUtil.mWXTitleSaveTimeKey, TimeUtil.getCurTime());

                    mTitleList.clear();
                    mTitleList.addAll(response.body().getResult());

                    initViewPager();
                } else {
                    String msg = response.body() != null ? response.body().getMsg() : "返回数据错误!";
                    ToastUtil.showToast(msg);
                }

            }

            @Override
            public void onFailure(Call<WXTitleMode> call, Throwable t) {
                dismissProgress();
                LogUtil.d(t.getMessage());
            }
        });

    }

    private void initViewPager(){
        int size = mTitleList.size();
        mList.clear();
        for (int i = 0; i < size; i++) {
            WXFilterFragment fragment = new WXFilterFragment();
            fragment.setTitleId(mTitleList.get(i).getCid());
            mList.add(fragment);
        }
        mAdapter.notifyDataSetChanged();

        addTitleView();
    }

    /**
     * 增加标题view
     */
    private void addTitleView() {
        mLinearLayout.removeAllViews();
        mWidthList.clear();
        //计算文字宽度
        Paint paint = new Paint();
        paint.setTextSize(DisplayUtil.dp2px(this, 16));
        Rect mTextBound = new Rect();

        int size = mTitleList.size();
        for (int i = 0; i < size; i++) {
            TextView textView = new TextView(this);
            textView.setId(i);
            String text = mTitleList.get(i).getName();

            if (!TextUtils.isEmpty(text)) {
                paint.getTextBounds(text, 0, text.length(), mTextBound);
                int curWidth = mWidth + mTextBound.width();
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(curWidth, LinearLayout.LayoutParams.MATCH_PARENT);
                textView.setLayoutParams(params);

                textView.setText(text);
                textView.setTextColor(getResources().getColor(R.color.colorText));
                textView.setTextSize(16);
                textView.setGravity(Gravity.CENTER);
                textView.setOnClickListener(mOnClickListener);

                mWidthList.add(curWidth);
                mLinearLayout.addView(textView);
            }
        }

        if (size > 0) {
            mViewPager.setCurrentItem(0);
        }
    }

    /**
     * 初始化标题View
     */
    public void initTitleView(int index) {
        int size = mLinearLayout.getChildCount();

        for (int i = 0; i < size; i++) {
            TextView textView = (TextView) mLinearLayout.getChildAt(i);

            if (textView != null) {
                if (index == i) {
                    textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    textView.setTextColor(getResources().getColor(R.color.colorText));
                }
            }
        }
    }

    /**
     * 标题点击监听
     */
    public View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mViewPager != null) {
                mViewPager.setCurrentItem(v.getId());
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTitleList.clear();
    }

}
