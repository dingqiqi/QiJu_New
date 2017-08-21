package com.dingqiqi.qijuproject.http;

import com.dingqiqi.qijuproject.mode.IdiomMode;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 成语大全
 * Created by dingqiqi on 2017/6/29.
 */
public interface IIdiomApi {

    @GET("/appstore/idiom/query")
    Call<IdiomMode> getIdiomData(@Query("key") String key, @Query("name") String name);

}
