package com.dingqiqi.qijuproject.http;

import com.dingqiqi.qijuproject.mode.DictionaryMode;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 新华字典
 * Created by dingqiqi on 2017/6/29.
 */
public interface IDictionaryApi {

    @GET("/appstore/dictionary/query")
    Call<DictionaryMode> getDictionaryData(@Query("key") String key, @Query("name") String name);

}
