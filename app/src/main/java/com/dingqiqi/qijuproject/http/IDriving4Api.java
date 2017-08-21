package com.dingqiqi.qijuproject.http;

import com.dingqiqi.qijuproject.mode.DrivingMode;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2017/6/14.
 */
public interface IDriving4Api {

    @GET("/tiku/kemu4/query")//查询起始页：默认为1 查询页大小：默认为10
    public Call<DrivingMode> getDriving1Data(@Query("key") String key, @Query("page") String page, @Query("size") String size);

}
