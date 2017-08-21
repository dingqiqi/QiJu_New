package com.dingqiqi.qijuproject.application;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.manager.LocationService;
import com.dingqiqi.qijuproject.util.LogUtil;
import com.dingqiqi.qijuproject.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Application
 * Created by dingqiqi on 2016/12/26.
 */
public class CustomApplication extends Application implements BDLocationListener {
    /**
     * 当前对象
     */
    private static CustomApplication mInstance;
    /**
     * 管理Activity列表
     */
    private static List<Activity> mList = new ArrayList<>();
    /**
     * 定位地址
     */
    private static String mAddress, mProvide;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        //日志初始化
        LogUtil.init(false);
        ToastUtil.init(this);
        //异常处理
        CrashHandler.getInstance().init(this);

        LocationService.initLocation(this);
        LocationService.setLocationListener(this);
        LocationService.startLocation();
    }

    public static CustomApplication getInstance() {
        return mInstance;
    }

    public void addActivity(FragmentActivity activity) {
        mList.add(activity);
    }

    public void removeActivity(FragmentActivity activity) {
        mList.remove(activity);
    }

    /**
     * 移除所有活动
     */
    public void finishAllActivity() {
        if (mList.size() == 0) {
            return;
        }

        for (int i = mList.size() - 1; i >= 0; i--) {
            if (!mList.get(i).isFinishing()) {
                mList.get(i).finish();
            }
            mList.remove(i);
        }
    }

    /**
     * 获取最上层的Activity，即当前顶层可见的Activity。
     *
     * @return Activity
     */
    public Activity getTopActivity() {
        if (mList.size() == 0) {
            return null;
        }

        return mList.get(mList.size() - 1);
    }

    /**
     * 退出应用
     * 关闭应用的所有的Activity
     *
     * @param exitCode 退出码
     */
    public void exitApplication(int exitCode) {
        finishAllActivity();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(exitCode);
    }

    public static String getAddress() {
        return mAddress;
    }

    public static String getProvide() {
        return mProvide;
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        mProvide = location.getProvince();
        String city = location.getCity();// 城市
        String district = location.getDistrict();// 区（或县）
        String street = location.getStreet();// 街道
        String streetNumber = location.getStreetNumber();// 街道号
        double latitude = location.getLatitude();// 纬度
        double longitude = location.getLongitude();// 精度

        LogUtil.i(city + district + street);
        LocationService.stopLocation();

        if (TextUtils.isEmpty(district)) {
//            Message message = mHandler.obtainMessage();
//            message.what = 0x02;
//            message.obj = "位置获取失败";
//            mHandler.sendMessage(message);
            ToastUtil.showToast("定位失败!");
            return;
        }

        //去掉区获取市
        if (getString(R.string.address_test).equals(district)) {
            mAddress = district.substring(0, district.length() - 2);
        } else {
            mAddress = district.substring(0, district.length() - 1);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        onDestroy();
    }

    public void onDestroy() {
        try {
            LocationService.stopLocation();
            LocationService.removeLocationListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
