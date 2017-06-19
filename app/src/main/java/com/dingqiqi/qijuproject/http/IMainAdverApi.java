package com.dingqiqi.qijuproject.http;

import com.dingqiqi.qijuproject.mode.AdverMode;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 主页 历史上的今天请求接口
 * Created by dingqiqi on 2017/5/24.
 */
public interface IMainAdverApi {

    @GET("/appstore/history/query")
    Call<AdverMode> getAdverData(@Query("key") String key, @Query("day") String day);

}
