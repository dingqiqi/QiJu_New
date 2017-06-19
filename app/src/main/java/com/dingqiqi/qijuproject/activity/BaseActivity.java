package com.dingqiqi.qijuproject.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.application.CustomApplication;
import com.dingqiqi.qijuproject.util.ToastUtil;
import com.dingqiqi.qijuproject.util.statusbar.SystemBar;

/**
 * Activity基类
 * Created by dingqiqi on 2016/12/26.
 */
public class BaseActivity extends AppCompatActivity {
    //点击同一 view 最小的时间间隔，小于这个数则忽略此次单击。
    private static long intervalTime = 800;
    //最后点击时间
    private long lastClickTime = 0;
    //最后被单击的 View 的ID
    private long lastClickView = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //输入法隐藏
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //沉浸式状态栏
        SystemBar.setSystemBarColor(this, R.color.colorPrimary);

        CustomApplication.getInstance().addActivity(this);
    }

    /**
     * 是否快速多次点击(连续多点击）
     *
     * @param view 被点击view，如果前后是同一个view，则进行双击校验
     * @return 认为是重复点击时返回true。
     */
    public boolean isFastClick(View view) {
        long time = System.currentTimeMillis() - lastClickTime;
        if (time < intervalTime && lastClickView == view.getId()) {
            lastClickTime = System.currentTimeMillis();
            return true;
        }
        lastClickTime = System.currentTimeMillis();
        lastClickView = view.getId();

        return false;
    }

    /**
     * 是否快速多次点击(连续多点击）
     *
     * @return 认为是重复点击时返回true。
     */
    public boolean isFastClick() {
        long time = System.currentTimeMillis() - lastClickTime;

        if (time < intervalTime) {
            lastClickTime = System.currentTimeMillis();
            return true;
        }

        lastClickTime = System.currentTimeMillis();

        return false;
    }

    /**
     * 设置标题
     * @param title
     */
    protected void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(title);
            }
        }
    }

    public void nextActivity(Intent intent) {
        if (intent == null) {
            ToastUtil.showToast(getString(R.string.app_not_found));
        } else {
            try {
                startActivity(intent);
            } catch (Exception e) {
                ToastUtil.showToast(getString(R.string.app_not_found));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CustomApplication.getInstance().removeActivity(this);
    }
}

