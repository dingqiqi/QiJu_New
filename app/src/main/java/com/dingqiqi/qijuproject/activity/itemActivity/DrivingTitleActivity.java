package com.dingqiqi.qijuproject.activity.itemActivity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.KeyEvent;
import android.view.View;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.activity.BaseActivity;
import com.dingqiqi.qijuproject.adapter.DrivingAdapter;
import com.dingqiqi.qijuproject.fragment.BaseFragment;
import com.dingqiqi.qijuproject.fragment.Driving1Fragment;
import com.dingqiqi.qijuproject.fragment.Driving4Fragment;
import com.dingqiqi.qijuproject.fragment.DrivingOtherFragment;
import com.dingqiqi.qijuproject.view.ScrollColorBtn;

import java.util.ArrayList;
import java.util.List;

/**
 * 驾照考试
 * Created by dingqiqi on 2017/6/14.
 */
public class DrivingTitleActivity extends BaseActivity {

    private ViewPager mViewPager;

    private List<BaseFragment> mList;

    private List<ScrollColorBtn> mBtnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving_title_layout);

        initView();

        initData();
    }

    private void initView() {
        setTitle("驾考模拟");

        mViewPager = (ViewPager) findViewById(R.id.viewpager_driving);

        mBtnList = new ArrayList<>();

        ScrollColorBtn btn1 = (ScrollColorBtn) findViewById(R.id.tabbtn1);
        ScrollColorBtn btn2 = (ScrollColorBtn) findViewById(R.id.tabbtn2);
        ScrollColorBtn btn3 = (ScrollColorBtn) findViewById(R.id.tabbtn3);

        mBtnList.add(btn1);
        mBtnList.add(btn2);
        mBtnList.add(btn3);

        btn1.setOnClickListener(mListener);
        btn2.setOnClickListener(mListener);
        btn3.setOnClickListener(mListener);
        mBtnList.get(0).setIconAlpha(1);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0) {
                    mBtnList.get(position).setIconAlpha(1 - positionOffset);
                    mBtnList.get(position + 1).setIconAlpha(positionOffset);
                }
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private View.OnClickListener mListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.tabbtn1:
                    initBtn(0);
                    break;
                case R.id.tabbtn2:
                    initBtn(1);
                    break;
                case R.id.tabbtn3:
                    initBtn(2);
                    break;
                default:
                    break;
            }
        }
    };

    private void initBtn(int index) {
        for (int i = 0; i < mList.size(); i++) {
            mBtnList.get(i).setIconAlpha(0);
        }

        mBtnList.get(index).setIconAlpha(1);
        mViewPager.setCurrentItem(index);
    }

    private void initData() {
        mList = new ArrayList<>();
        Driving1Fragment fragment = new Driving1Fragment();
        mList.add(fragment);
        Driving4Fragment fragment1 = new Driving4Fragment();
        mList.add(fragment1);
        DrivingOtherFragment fragment2 = new DrivingOtherFragment();
        mList.add(fragment2);

        DrivingAdapter adapter = new DrivingAdapter(getSupportFragmentManager(), mList);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mList.get(mViewPager.getCurrentItem()).onBack()) {
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
