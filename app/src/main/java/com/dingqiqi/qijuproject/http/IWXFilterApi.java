package com.dingqiqi.qijuproject.http;

import com.dingqiqi.qijuproject.mode.WXFilterMode;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 精选文章
 * Created by 丁奇奇 on 2017/8/3.
 */
public interface IWXFilterApi {

    @GET("/wx/article/search")
    Call<WXFilterMode> getWXFilterData(@Query("key") String key, @Query("cid") String cid, @Query("page") int page, @Query("size") int size);

}
