package com.dingqiqi.qijuproject.http;

import com.dingqiqi.qijuproject.mode.TrainMode;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 列车时刻表
 * Created by Administrator on 2017/6/19.
 */
public interface ITrainApi {

    @GET("/train/tickets/queryByTrainNo")
    Call<TrainMode> getTrainData(@Query("key") String key, @Query("trainno") String trainno);

}
