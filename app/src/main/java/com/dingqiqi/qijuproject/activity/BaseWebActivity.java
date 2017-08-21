package com.dingqiqi.qijuproject.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.util.ToastUtil;

import java.lang.reflect.Field;

/**
 * 通用WebView Activity
 * Created by 丁奇奇 on 2017/8/3.
 */
public class BaseWebActivity extends BaseActivity {

    private ProgressBar mProgressBar;

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_webview_layout);

        initView();

        setConfigCallback((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mProgressBar = (ProgressBar) findViewById(R.id.progress_view);

        mWebView = new WebView(getApplicationContext());
        WebSettings setting = mWebView.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setAppCacheEnabled(true);
        setting.setSupportZoom(true);

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 0) {
                    mProgressBar.setVisibility(View.VISIBLE);
                } else if (newProgress == 100) {
                    mProgressBar.setVisibility(View.GONE);
                    return;
                }
                mProgressBar.setProgress(newProgress);
            }
        });

        LinearLayout layout = (LinearLayout) findViewById(R.id.linear);
        layout.addView(mWebView);

        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("url");

        if (!TextUtils.isEmpty(title)) {
            mToolbar.setTitle(title);
        }

        if (!TextUtils.isEmpty(url)) {
            mWebView.loadUrl(url);
        } else {
            ToastUtil.showToast("请求地址为空!");
            finish();
        }
    }

    /**
     * 设置引用
     *
     * @param windowManager 服务
     */
    public void setConfigCallback(WindowManager windowManager) {
        try {
            Field field = WebView.class.getDeclaredField("mWebViewCore");
            field = field.getType().getDeclaredField("mBrowserFrame");
            field = field.getType().getDeclaredField("sConfigCallback");
            field.setAccessible(true);
            Object configCallback = field.get(null);

            if (null == configCallback) {
                return;
            }

            field = field.getType().getDeclaredField("mWindowManager");
            field.setAccessible(true);
            field.set(configCallback, windowManager);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        setConfigCallback(null);

        ViewParent parent = mWebView.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(mWebView);
        }


        mWebView.stopLoading();
        mWebView.removeAllViews();

        try {
            mWebView.destroy();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        mWebView = null;
        //杀掉进程
        System.exit(0);
    }
}
