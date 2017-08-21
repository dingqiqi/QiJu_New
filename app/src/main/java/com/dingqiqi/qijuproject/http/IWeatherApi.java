package com.dingqiqi.qijuproject.http;

import com.dingqiqi.qijuproject.mode.WeatherMode;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 天气请求接口
 * Created by dingqiqi on 2017/6/7.
 */
public interface IWeatherApi {
    @GET("/v1/weather/query")
    Call<WeatherMode> getWeatherData(@Query("key") String key, @Query("city") String city);

}
