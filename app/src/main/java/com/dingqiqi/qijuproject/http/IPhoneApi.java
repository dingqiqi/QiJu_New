package com.dingqiqi.qijuproject.http;

import com.dingqiqi.qijuproject.mode.IPhoneMode;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * 手机号码归属地
 * Created by dingqiqi on 2017/6/29.
 */
public interface IPhoneApi {

    @GET("/v1/mobile/address/query")
    Call<IPhoneMode> getPhotoData(@Query("key") String key, @Query("phone") String phone);

}
