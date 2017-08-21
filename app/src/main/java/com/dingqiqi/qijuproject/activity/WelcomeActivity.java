package com.dingqiqi.qijuproject.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.view.statusbar.SystemBar;
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

//        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
//        startActivity(intent);
//        finish();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_DENIED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0x01);
            }
        }

        mProgressBall.setProgressListener(new CircleProgressBall360.onProgressListener() {
            @Override
            public void onProgress(int progress) {
                if (progress == 100) {
//                    Log.i("aaa", progress + "");

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
                mProgress = mProgress + 2;
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x01) {
            if (grantResults.length > 0 && grantResults[0] == PermissionChecker.PERMISSION_DENIED) {
                ActivityCompat.shouldShowRequestPermissionRationale(WelcomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }


}
