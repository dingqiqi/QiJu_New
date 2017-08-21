package com.dingqiqi.qijuproject.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.util.SharedPreUtil;
import com.dingqiqi.qijuproject.util.ToastUtil;

/**
 * 设置界面
 * Created by dingqiqi on 2017/6/16.
 */
public class SettingActivity extends BaseActivity {
    //驾考快速答题提示标题
    private TextView mTvDrivingHint;
    //驾考快速答题提示选择开关
    private SwitchCompat mSwitchDrivingHint;
    //驾考标题
    private TextView mTvDriving;
    //驾考选择开关
    private SwitchCompat mSwitchDriving;
    //驾考快速选择值
    private int mDrivingSetting;
    //是否打开提示
    private boolean mIsOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_layout);

        intiView();

        setListener();
    }

    private void intiView() {
        setTitle("设置");

        mTvDriving = (TextView) findViewById(R.id.tv_setting_driving);
        mSwitchDriving = (SwitchCompat) findViewById(R.id.switch_setting_driving);

        mTvDrivingHint = (TextView) findViewById(R.id.tv_setting_hint_driving);
        mSwitchDrivingHint = (SwitchCompat) findViewById(R.id.switch_setting_hint_driving);

        //0 关 1开
        mDrivingSetting = SharedPreUtil.getInstance(this, SharedPreUtil.mDefaultFileName).getInt(SharedPreUtil.mSettingDrivingKey, 0);

        mIsOpen = SharedPreUtil.getInstance(this, SharedPreUtil.mDefaultFileName).getBoolean(SharedPreUtil.mDrivingFirstKey, true);

        if (mDrivingSetting == 0) {
            mSwitchDriving.setChecked(false);
        } else {
            mSwitchDriving.setChecked(true);
        }

        if (mIsOpen) {
            mSwitchDrivingHint.setChecked(true);
        } else {
            mSwitchDrivingHint.setChecked(false);
        }
    }

    /**
     * 设置监听
     */
    private void setListener() {
        mTvDriving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast("此选项开关打开后,驾考做题时,选中正确答案后直接跳转下一题!");
            }
        });

        mSwitchDriving.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mDrivingSetting = 1;
                } else {
                    mDrivingSetting = 0;
                }
            }
        });

        mTvDrivingHint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showToast("此选项开关打开后,驾考做题时,会提示您是否要快速答题!");
            }
        });

        mSwitchDrivingHint.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mIsOpen = true;
                } else {
                    mIsOpen = false;
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreUtil.getInstance(this, SharedPreUtil.mDefaultFileName).put(SharedPreUtil.mSettingDrivingKey, mDrivingSetting);
        SharedPreUtil.getInstance(this, SharedPreUtil.mDefaultFileName).put(SharedPreUtil.mDrivingFirstKey, mIsOpen);
    }
}
