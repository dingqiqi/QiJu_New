package com.dingqiqi.qijuproject.http;

import com.dingqiqi.qijuproject.mode.MenuMode;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 菜单列表
 * Created by Administrator on 2017/7/20.
 */
public interface IMenuListApi {

    @GET("/v1/cook/menu/search")
    Call<MenuMode> getMenuListData(@Query("key") String key, @Query("cid") String cid, @Query("page") int page, @Query("size") int size);

}
