package com.dingqiqi.qijuproject.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.activity.itemActivity.DrivingTitleActivity;
import com.dingqiqi.qijuproject.activity.itemActivity.TrainDetailActivity;
import com.dingqiqi.qijuproject.activity.itemActivity.WeatherActivity;
import com.dingqiqi.qijuproject.adapter.LoopPageAdapter;
import com.dingqiqi.qijuproject.adapter.MainAdapter;
import com.dingqiqi.qijuproject.application.CustomApplication;
import com.dingqiqi.qijuproject.http.IAirQualityApi;
import com.dingqiqi.qijuproject.http.IMainAdverApi;
import com.dingqiqi.qijuproject.manager.RetrofitManager;
import com.dingqiqi.qijuproject.mode.AdverMode;
import com.dingqiqi.qijuproject.mode.AirFutureMode;
import com.dingqiqi.qijuproject.mode.AirHourMode;
import com.dingqiqi.qijuproject.mode.AirQualityMode;
import com.dingqiqi.qijuproject.mode.MainModel;
import com.dingqiqi.qijuproject.util.CommonUtil;
import com.dingqiqi.qijuproject.util.DisplayUtil;
import com.dingqiqi.qijuproject.util.LogUtil;
import com.dingqiqi.qijuproject.util.SaveUtil;
import com.dingqiqi.qijuproject.util.ToastUtil;
import com.dingqiqi.qijuproject.view.CusItemTouchHelper;
import com.dingqiqi.qijuproject.view.LineChartView;
import com.dingqiqi.qijuproject.view.LoopViewPager;
import com.dingqiqi.qijuproject.view.MarqueeView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 主页Activity
 */
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;

    private MainAdapter mAdapter;
    private List<MainModel> mList;

    //private CustomRecyLine mRecyLine;

    private MarqueeView mMarqueeView;

    private String[] mTitleArray = new String[]{"历史上的今天-》QQ957789074", "空气质量-》QQ957789074"};

    /**
     * 广告页相关
     */
    private List<View> mListPager;
    private LoopViewPager mLoopViewPager;
    private LoopPageAdapter mLoopPageAdapter;
    /**
     * 圆点相关
     * 圆点间距
     */
//    private int mPointWidth = 0;
    /**
     * 圆点父布局
     */
    private LinearLayout mPointLayout;
    /**
     * 页面数量
     */
    private int mPageNum = 5;

    private ValueAnimator mValueAnimator;

    private RelativeLayout mRlAdver;
    /**
     * 热点是否展开
     */
    private boolean mIsOpen = false;
    /**
     * 广告布局
     */
    private View mViewAdver, mViewChart;
    /**
     * 当前广告点击下标
     */
    private int mCurrentIndex;
    /**
     * 广告数据
     */
    private List<AdverMode> mAdverList;
    /**
     * 空气质量天
     */
    private List<AirFutureMode> mFutureList;
    /**
     * 空气质量小时
     */
    private List<AirHourMode> mHourList;
    /**
     * 空气质量图标显示类型 1 日 2小时
     */
    private int mAirType = 1;
    /**
     * 空气质量折线图
     */
    private LineChartView mLineChartView;
    /**
     * 主页目录
     */
    private Class[] mItemData;

    private long mExitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);

        initView();

        initData();

        initAdver();

        requestData();

        mItemData = new Class[]{WeatherActivity.class, DrivingTitleActivity.class, TrainDetailActivity.class};
    }

    private void initView() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //noinspection deprecation
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, CommonUtil.mCountNum));
        //间隔线
//        mRecyLine = new CustomRecyLine(this);
//        mRecyLine.setOrien(2);
//        mRecyclerView.addItemDecoration(mRecyLine);

        mList = new ArrayList<>();
        mAdapter = new MainAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);

        CusItemTouchHelper mCusItemTouchHelper = new CusItemTouchHelper(mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCusItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        mMarqueeView = (MarqueeView) findViewById(R.id.marqueueView);
        mMarqueeView.setTitleArray(mTitleArray);

        mViewAdver = findViewById(R.id.fl_ls);
        mViewChart = findViewById(R.id.tv_chart);

        mLineChartView = (LineChartView) findViewById(R.id.lineChart);

        mFutureList = new ArrayList<>();
        mHourList = new ArrayList<>();
    }

    private void initData() {
        List<MainModel> list = SaveUtil.getMenuList(this);
        if (list.size() == 0) {
            String[] strArray = getResources().getStringArray(R.array.main_menu);
            for (int i = 0; i < strArray.length; i++) {
                MainModel model = new MainModel();
                String[] array = strArray[i].split(",");
                model.setName(array[0]);
                model.setIcon(array[1]);
                model.setPosition(i);
                mList.add(model);
            }
        } else {
            mList.addAll(list);
        }

        mAdapter.notifyDataSetChanged();

        mMarqueeView.setClickListener(new MarqueeView.onTitleClickListener() {
            @Override
            public void onClick(int index, String title) {
                if (index == 0) {
                    mViewAdver.setVisibility(View.VISIBLE);
                    mViewChart.setVisibility(View.GONE);
                } else if (index == 1) {
                    mViewAdver.setVisibility(View.GONE);
                    mViewChart.setVisibility(View.VISIBLE);
                } else {
                    return;
                }

                if (mValueAnimator.isRunning()) {
                    return;
                }

                if (mIsOpen) {
                    if (mCurrentIndex == index) {
                        mValueAnimator.reverse();
                        mIsOpen = !mIsOpen;
                    }
                } else {
                    mValueAnimator.start();
                    mIsOpen = !mIsOpen;
                }
                mCurrentIndex = index;
            }
        });
    }

    private void initAdver() {
        //广告页初始化
        mListPager = new ArrayList<>();
        mLoopViewPager = (LoopViewPager) findViewById(R.id.loopviewpager);
        mLoopPageAdapter = new LoopPageAdapter(this, mListPager);
        mLoopViewPager.setAdapter(mLoopPageAdapter);
        mLoopViewPager.setCurrentItem(1);
        mLoopViewPager.setOnPageChangeListener(mChangeListener);

        //圆点初始化
        mPointLayout = (LinearLayout) findViewById(R.id.ll_point_hint);
        // 加载出来
//        mPointLayout.getViewTreeObserver().addOnGlobalLayoutListener(
//                new ViewTreeObserver.OnGlobalLayoutListener() {
//
//                    @Override
//                    public void onGlobalLayout() {
//                        int mPointWidth = mPointLayout.getChildAt(1).getLeft()
//                                - mPointLayout.getChildAt(0).getLeft();
//                    }
//                });

        setAdver(false);

        mRlAdver = (RelativeLayout) findViewById(R.id.rl_adver);

        mValueAnimator = ValueAnimator.ofInt(DisplayUtil.dp2px(this, 140));
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlAdver.getLayoutParams();
                params.height = value;
                mRlAdver.setLayoutParams(params);
            }
        });
        mValueAnimator.setDuration(300);
    }

    private void setAdver(boolean flag) {
        mListPager.clear();

        for (int i = 0; i < mPageNum; i++) {
            View view = getLayoutInflater().inflate(R.layout.loop_viewpager_layout,
                    null, false);
            LinearLayout linearBg = (LinearLayout) view.findViewById(R.id.linear_bg);
            TextView tvTitle = (TextView) view.findViewById(R.id.tv_adver_title);
            TextView tvMsg = (TextView) view.findViewById(R.id.tv_adver_message);
            if (flag) {
                linearBg.setBackgroundColor(Color.parseColor("#00000000"));
                tvTitle.setText(mAdverList.get(i).getTitle());
                tvMsg.setText(mAdverList.get(i).getEvent());
            } else {
                String text = "第" + i + "页";
                tvTitle.setText(text);
            }

            mListPager.add(view);
        }

        mPointLayout.removeAllViews();

        for (int i = 0; i < mPageNum; i++) {
            ImageView imageView = new ImageView(this);

            android.widget.LinearLayout.LayoutParams params = new android.widget.LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            if (i != 0) {
                imageView.setBackgroundResource(R.drawable.shape_oval_gray);
                params.leftMargin = 15;
            } else {
                imageView.setBackgroundResource(R.drawable.shape_oval_red);
            }

            imageView.setLayoutParams(params);
            mPointLayout.addView(imageView);
        }

        mLoopPageAdapter = new LoopPageAdapter(this, mListPager);
        mLoopViewPager.setAdapter(mLoopPageAdapter);
        mLoopViewPager.setCurrentItem(1);
    }

    /**
     * 初始化圆点颜色
     */
    private void initPointColor() {
        for (int i = 0; i < mPointLayout.getChildCount(); i++) {
            mPointLayout.getChildAt(i).setBackgroundResource(R.drawable.shape_oval_gray);
        }
    }

    /**
     * 广告页滑动监听
     */
    private ViewPager.OnPageChangeListener mChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int arg0) {
            initPointColor();
            mPointLayout.getChildAt(arg0).setBackgroundResource(R.drawable.shape_oval_red);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void requestData() {
        IMainAdverApi adverApi = RetrofitManager.getInstance(this).getRetrofit().create(IMainAdverApi.class);

        String day = new SimpleDateFormat("MMdd", Locale.getDefault()).format(new Date());
        Call<AdverMode> call = adverApi.getAdverData(CommonUtil.getKey(), day);

        call.enqueue(new Callback<AdverMode>() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onResponse(Call<AdverMode> call, Response<AdverMode> response) {
                LogUtil.i(response.toString());

                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getResult() != null) {
                        mAdverList = response.body().getResult();
                        if (mAdverList.size() > 10) {
                            mAdverList = mAdverList.subList(0, 10);
                        }
                        mPageNum = mAdverList.size();
                        setAdver(true);
                        LogUtil.d(response.body().getResult().size() + " 参数个数");
                    } else {
                        //ToastUtil.showToast(MainActivity.this, "历史上的今天返回数据失败");
                        LogUtil.d("历史上的今天的 数据为空  数据= " + response.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<AdverMode> call, Throwable t) {
                ToastUtil.showToast(MainActivity.this, "历史上的今天返回数据失败");
                LogUtil.d("历史上的今天 请求失败 " + t.getMessage());
            }
        });


        IAirQualityApi airQualityApi = RetrofitManager.getInstance(this).getRetrofit().create(IAirQualityApi.class);
        String address = TextUtils.isEmpty(CustomApplication.getAddress()) ? getResources().getString(R.string.default_address_city) : CustomApplication.getAddress();
        Call<AirQualityMode> airCall = airQualityApi.getAirData(CommonUtil.getKey(), address);

        airCall.enqueue(new Callback<AirQualityMode>() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onResponse(Call<AirQualityMode> call, Response<AirQualityMode> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null && response.body().getResult() != null) {

                        mFutureList = response.body().getResult().get(0).getFetureData();
                        mHourList = response.body().getResult().get(0).getHourData();

                        setAirData();
                    } else {
                        LogUtil.d("空气质量 数据为空  数据= " + response.message());
                    }
                } else {
                    LogUtil.d("空气质量 数据为空  数据= " + response.message());
                }
            }

            @Override
            public void onFailure(Call<AirQualityMode> call, Throwable t) {
                LogUtil.d("空气质量 请求失败 " + t.getMessage());
            }
        });
    }

    /**
     * 设置空气质量图片数据
     */
    private void setAirData() {
        if (mAirType == 1) {
            int length = mFutureList.size();
            if (length == 0) {
                return;
            }

            float aqiArr[] = new float[length];
            String xLabel[] = new String[length];

            for (int i = 0; i < length; i++) {
                aqiArr[i] = mFutureList.get(i).getAqi();
                xLabel[i] = mFutureList.get(i).getDate().substring(5);
            }

            //y轴上刻度个数
            int yLineCount = 5;
            mLineChartView.setData(xLabel, aqiArr, yLineCount, true);
        } else {
            int length = mFutureList.size();
            if (length == 0) {
                return;
            }

            float aqiArr[] = new float[length];
            String xLabel[] = new String[length];

            for (int i = 0; i < length; i++) {
                aqiArr[i] = mHourList.get(i).getAqi();
                xLabel[i] = mHourList.get(i).getDateTime().substring(10).trim();
            }

            //y轴上刻度个数
            int yLineCount = 5;
            mLineChartView.setData(xLabel, aqiArr, yLineCount, true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            ToastUtil.showToast(MainActivity.this, "下面方块可拖动排序哦!");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        //设置界面
        if (id == R.id.nav_setting) {
            nextActivity(new Intent(MainActivity.this, SettingActivity.class));
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() - mExitTime > 1500) {
                Toast.makeText(MainActivity.this, "再按一次退出!", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }
    }


    public void onItemClick(int position) {
        Intent intent = new Intent(this, mItemData[position]);
        nextActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mDrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
