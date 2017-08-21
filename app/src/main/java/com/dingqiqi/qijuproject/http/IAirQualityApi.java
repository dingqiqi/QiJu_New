package com.dingqiqi.qijuproject.http;

import com.dingqiqi.qijuproject.mode.AirQualityMode;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 空气质量请求接口
 * Created by dingqiqi on 2017/5/25.
 */
public interface IAirQualityApi {

    @GET("/environment/query")
    Call<AirQualityMode> getAirData(@Query("key") String key, @Query("city") String city);

}
