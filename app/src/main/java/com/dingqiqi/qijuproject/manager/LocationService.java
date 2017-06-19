package com.dingqiqi.qijuproject.manager;

import android.content.Context;

import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.dingqiqi.qijuproject.util.LogUtil;

/**
 * Created by dingqiqi on 2016/7/1.
 */
public class LocationService {

    private static LocationClient mLocationClient;

    private LocationService() {
    }

    /**
     * 初始化定位
     *
     * @param context
     * @return
     */
    public static LocationClient initLocation(Context context) {
        if (mLocationClient == null) {
            mLocationClient = new LocationClient(context);
            LocationClientOption option = new LocationClientOption();
            option.setCoorType("bd09ll");// bd09ll:设置坐标类型，为百度经纬坐标系
            option.setIsNeedAddress(true);
            option.setTimeOut(30000);
            option.setOpenGps(true);// 打开GPS定位
            option.setScanSpan(0);// 设置定时定位的时间间隔为1s
            mLocationClient.setLocOption(option);// 设置定位方式
        }

        return mLocationClient;
    }

    /**
     * 启动定位
     */
    public static void startLocation() {
        if (mLocationClient == null) {
            LogUtil.d("请初始化定位");
            return;
        }

        if (mLocationClient.isStarted()) {
            mLocationClient.stop();
        }

        mLocationClient.start();
    }

    /**
     * 停止定位
     */
    public static void stopLocation() {
        if (mLocationClient == null) {
            LogUtil.d("请初始化定位");
            return;
        }

        mLocationClient.stop();
    }

    /**
     * 注册监听
     */
    public static void setLocationListener(BDLocationListener listener) {
        if (mLocationClient == null) {
            LogUtil.d("请初始化定位");
            return;
        }

        if (listener == null) {
            LogUtil.d("定位监听不能为空");
            return;
        }

        mLocationClient.registerLocationListener(listener);
    }

    /**
     * 取消监听
     */
    public static void removeLocationListener(BDLocationListener listener) {
        if (mLocationClient == null) {
            LogUtil.d("请初始化定位");
            return;
        }

        if (listener == null) {
            LogUtil.d("定位监听不能为空");
            return;
        }
        mLocationClient.unRegisterLocationListener(listener);
    }

}
