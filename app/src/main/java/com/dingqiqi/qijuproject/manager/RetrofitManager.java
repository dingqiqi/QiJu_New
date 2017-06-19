package com.dingqiqi.qijuproject.manager;

import android.content.Context;

import com.dingqiqi.qijuproject.BuildConfig;
import com.dingqiqi.qijuproject.util.CommonUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 网络请求库
 * Created by dingqiqi on 2017/5/24.
 */
public class RetrofitManager {

    private static RetrofitManager mInstance;

    private Retrofit mRetrofit;

    private RetrofitManager(Context context) {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                .create();//使用 gson coverter，统一日期请求格式
        mRetrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).client(okHttpInit(context)).build();
    }

    private OkHttpClient okHttpInit(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);
        // 10M
        long cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(new File(CommonUtil.getWritePath("/http")), cacheSize);
        builder.cache(cache);

        //用于输出网络请求和结果的 Log com.squareup.okhttp3:logging-interceptor
        if (BuildConfig.LOG_DEBUG) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        return builder.build();
    }

    public static RetrofitManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new RetrofitManager(context);
        }

        return mInstance;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }
}
