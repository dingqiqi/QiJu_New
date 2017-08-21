package com.dingqiqi.qijuproject.util;

import android.graphics.Color;
import android.os.Environment;

import com.dingqiqi.qijuproject.BuildConfig;
import com.dingqiqi.qijuproject.application.CustomApplication;

/**
 * Created by Administrator on 2017/5/23.
 */
public class CommonUtil {

    /**
     * 每行的个数
     */
    public static final int mCountNum = 2;
    /**
     * 用户key
     */
    public static final String mAppKey = "c380";
    /**
     * 空气污染颜色
     */
    public static final int[] mWeatherColor = new int[]{Color.parseColor("#02E300"),
            Color.parseColor("#FFFF00"), Color.parseColor("#FF7E00"), Color.parseColor("#FE0000"),
            Color.parseColor("#98004B"), Color.parseColor("#7E0123")};
    /**
     * 空气污染提示
     */
    private static final String[] mWeatherHint = new String[]{" 空气优 level  >", " 空气良 level  >", " 轻度污染 level  >",
            " 中度污染 level  >", " 重度污染 level  >", " 严重污染 level  >"};

    /**
     * 空气污染提示
     */
    private static final String[] mWeatherHint1 = new String[]{"优", "良", "轻度", "中度", "重度", "严重"};

    /**
     * 获取输出文件路径(包括日志,缓存)
     *
     * @param pathEndWith
     * @return
     */
    public static String getWritePath(String pathEndWith) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            return Environment.getExternalStorageDirectory().getPath() + "/DQQ_PROJECT/" + CustomApplication.getInstance().getPackageName() + pathEndWith;
        } else {
            //路径:android/包名/Cacnher/PATH
            return CustomApplication.getInstance().getCacheDir() + pathEndWith;
        }
    }

    public static String getKey() {
        return BuildConfig.APP_KEY + CommonUtil.mAppKey;
    }

    /**
     * 空气污染提示语
     *
     * @param level
     * @return
     */
    public static int getWeatherLever(int level) {
        if (level <= 50) {
            return 0;
        } else if (level <= 100) {
            return 1;
        } else if (level <= 150) {
            return 2;
        } else if (level <= 200) {
            return 3;
        } else if (level <= 300) {
            return 4;
        } else {
            return 5;
        }
    }

    /**
     * 空气污染提示语
     *
     * @param level 污染指数
     * @return
     */
    public static String getWeatherHint(int level) {
        return mWeatherHint[getWeatherLever(level)].replace("level", String.valueOf(level));
    }

    /**
     * 空气污染提示语
     *
     * @param level 污染指数
     * @return
     */
    public static String getWeatherHint1(int level) {
        return mWeatherHint1[level];
    }

    /**
     * 空气污染颜色
     *
     * @param level 污染指数
     * @return
     */
    public static int getWeatherColor(int level) {
        return mWeatherColor[getWeatherLever(level)];
    }

    /**
     * 空气污染颜色
     *
     * @param level 污染指数
     * @return
     */
    public static int getWeatherColor1(int level) {
        return mWeatherColor[level];
    }

}
