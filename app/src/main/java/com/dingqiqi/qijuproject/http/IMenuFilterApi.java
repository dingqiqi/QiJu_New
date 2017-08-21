package com.dingqiqi.qijuproject.http;

import com.dingqiqi.qijuproject.mode.MenuFilterMode;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 菜单筛选条件列表
 * Created by Administrator on 2017/7/20.
 */
public interface IMenuFilterApi {

    @GET("/v1/cook/category/query")
    Call<MenuFilterMode> getMenuListData(@Query("key") String key);

}
