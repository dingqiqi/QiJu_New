package com.dingqiqi.qijuproject.http;

import com.dingqiqi.qijuproject.mode.WXTitleMode;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 精选标题
 * Created by 丁奇奇 on 2017/8/2.
 */
public interface IWXTitleApi {

    @GET("/wx/article/category/query")
    Call<WXTitleMode> getWXTitleData(@Query("key") String key);

}
