package com.dingqiqi.qijuproject.http;

import com.dingqiqi.qijuproject.mode.DrivingOtherMode;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 驾考专题api
 * Created by dingqiqi on 2017/6/16.
 */
public interface IDrivingOtherApi {

    @GET("/tiku/shitiku/category/query")
    Call<DrivingOtherMode> getDrivingData(@Query("key") String key);

}
