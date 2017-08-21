package com.dingqiqi.qijuproject.activity.itemActivity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.activity.BaseActivity;
import com.dingqiqi.qijuproject.adapter.WeatherAdapter;
import com.dingqiqi.qijuproject.application.CustomApplication;
import com.dingqiqi.qijuproject.http.IWeatherApi;
import com.dingqiqi.qijuproject.manager.RetrofitManager;
import com.dingqiqi.qijuproject.mode.WeatherMode;
import com.dingqiqi.qijuproject.util.CommonUtil;
import com.dingqiqi.qijuproject.util.LogUtil;
import com.dingqiqi.qijuproject.util.SharedPreUtil;
import com.dingqiqi.qijuproject.util.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 天气活动类
 * Created by dingqiqi on 2017/6/5.
 */
public class WeatherActivity extends BaseActivity {
    //下拉刷新
    private SwipeRefreshLayout mRefreshLayout;
    //列表控件
    private RecyclerView mRecyclerView;
    //适配器
    private WeatherAdapter mWeatherAdapter;
    //地址
    private String mAddress;
    //从sp中获取到的地址
    private String mGetAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_layout);

        initView();
        initData("");
    }

    private void initView() {
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_weather);

        mRefreshLayout.setColorSchemeResources(R.color.holo_blue_light,
                R.color.holo_red_light, R.color.holo_orange_light,
                R.color.holo_green_light);

        mWeatherAdapter = new WeatherAdapter(this, null);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mWeatherAdapter);

        mRefreshLayout.setOnRefreshListener(mRefreshListener);
    }

    public void initData(String address) {
        if (!TextUtils.isEmpty(address)) {
            mAddress = address;
        } else {
            mGetAddress = mAddress = SharedPreUtil.getInstance(this, SharedPreUtil.mAddressFileName).getString(SharedPreUtil.mAddressKey, "");
        }

        if (mRefreshLayout != null) {
            mRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(true);
                    mRefreshListener.onRefresh();
                }
            });
        }
    }

    /**
     * 下拉刷新监听
     */
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            requestWeather();
        }
    };

    /**
     * 请求天气数据
     */
    private void requestWeather() {
        String address;
        if (!TextUtils.isEmpty(mAddress)) {
            address = mAddress;
        } else {
            address = TextUtils.isEmpty(CustomApplication.getAddress()) ? getResources().getString(R.string.default_address_city) : CustomApplication.getAddress();
        }

        IWeatherApi weatherApi = RetrofitManager.getInstance(this).getRetrofit().create(IWeatherApi.class);
        Call<WeatherMode> call = weatherApi.getWeatherData(CommonUtil.getKey(), address);
        call.enqueue(new Callback<WeatherMode>() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onResponse(Call<WeatherMode> call, Response<WeatherMode> response) {
                mRefreshLayout.setRefreshing(false);
                if (response.body() != null && response.body().getResult() != null && response.body().getResult().size() > 0) {
                    //从sp获取的地址和请求地址不一致,保存请求地址
                    if (!mAddress.equals(mGetAddress)) {
                        SharedPreUtil.getInstance(WeatherActivity.this, SharedPreUtil.mAddressFileName).put(SharedPreUtil.mAddressKey, mAddress);
                    }
                    mWeatherAdapter = new WeatherAdapter(WeatherActivity.this, response.body().getResult().get(0));
                    mRecyclerView.setAdapter(mWeatherAdapter);
                } else {
                    if (response.body() != null) {
                        ToastUtil.showToast(response.body().getMsg());
                    } else {
                        LogUtil.d("天气数据请求失败 " + response.body() != null ? response.body().getMsg() : "");
                    }
                }
            }

            @Override
            public void onFailure(Call<WeatherMode> call, Throwable t) {
                mRefreshLayout.setRefreshing(false);
                ToastUtil.showToast("数据请求失败 ");
                LogUtil.d("天气数据 请求失败 " + t.getMessage());
            }
        });

    }

    /**
     * 获取当前页面的全屏布局,用于显示全屏popwindow
     *
     * @return 返回全屏的view
     */
    public View getParentView() {
        return mRecyclerView;
    }


}
