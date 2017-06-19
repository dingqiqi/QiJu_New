package com.dingqiqi.qijuproject.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.util.statusbar.SystemBar;
import com.dingqiqi.qijuproject.view.CircleProgressBall360;

public class WelcomeActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();
    private CircleProgressBall360 mProgressBall;
    private int mProgress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //         WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome_layout);

        //沉浸式状态栏
        SystemBar.setSystemBarColor(this, R.color.colorPrimary);

        mProgressBall = (CircleProgressBall360) findViewById(R.id.progress);

        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();

        mProgressBall.setProgressListener(new CircleProgressBall360.onProgressListener() {
            @Override
            public void onProgress(int progress) {
                if (progress == 100) {
                    Log.i("aaa", progress + "");

                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void initAnimation() {
                mHandler.post(mRunnable);
            }
        });
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mProgress == 100) {
                mHandler.removeCallbacks(mRunnable);
                return;
            } else {
                mProgress = mProgress + 3;
                if (mProgress >= 100) {
                    mProgress = 100;
                } else {
                    mHandler.postDelayed(mRunnable, 20);
                }
                mProgressBall.setProgress(mProgress);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }


}
