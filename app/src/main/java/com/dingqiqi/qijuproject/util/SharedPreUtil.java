package com.dingqiqi.qijuproject.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * sp 工具
 */
public class SharedPreUtil {
    /**
     * 存储主菜单数据顺序sp名称
     */
    public static final String mMainMenuFileName = "main_menu_file";
    /**
     * 保存菜单的key
     */
    public static final String mMainMenuKey = "main_menu_key";
    /**
     * 存储主菜单数据顺序sp名称
     */
    public static final String mAddressFileName = "address_file";
    /**
     * 保存地址的key
     */
    public static final String mAddressKey = "address_key";
    /**
     * 存储其他数据的sp名称
     */
    public static final String mDefaultFileName = "default_file";
    /**
     * 保存驾考快速答题的key
     */
    public static final String mSettingDrivingKey = "setting_driving_key";
    /**
     * 第一次提示
     */
    public static final String mDrivingFirstKey = "driving_first_key";
    /**
     * 存储菜谱数据sp名称
     */
    public static final String mMenuFileName = "menu_file";
    /**
     * 保存菜谱的key
     */
    public static final String mMenuKey = "menu_key";
    /**
     * 保存菜谱的存储时间key
     */
    public static final String mMenuSaveTimeKey = "menu_save_time_key";
    /**
     * 保存标题的key
     */
    public static final String mWXTitleFileName = "wx_title_file";
    /**
     * 保存菜谱的key
     */
    public static final String mWXTitleKey = "wx_title_key";
    /**
     * 保存标题的存储时间key
     */
    public static final String mWXTitleSaveTimeKey = "wx_title_save_time_key";
    /**
     * sp 实例
     */
    private static SharedPreUtil mInstance;
    private SharedPreferences mSp;

    private static List<Mode> mList;

    private SharedPreUtil(Context context, String name) {
        int index = -1;
        for (Mode mode : mList) {
            if (mode.name.equals(name)) {
                index = 1;
                mSp = mode.preferences;
                break;
            }
        }

        if (index == -1) {
            mSp = context.getSharedPreferences(name, Context.MODE_PRIVATE);
            Mode mode = new Mode();
            mode.name = name;
            mode.preferences = mSp;
            mList.add(mode);
        }
    }

    public static SharedPreUtil getInstance(Context context, String name) {
        if (mList == null) {
            mList = new ArrayList<>();
        }

        if (mInstance == null) {
            mInstance = new SharedPreUtil(context.getApplicationContext(), name);
        }
        return mInstance;
    }

    public SharedPreferences getPreference() {
        return mSp;
    }

    public void put(String key, int value) {
        mSp.edit().putInt(key, value).apply();
    }

    public void put(String key, String value) {
        mSp.edit().putString(key, value).apply();
    }

    public void put(String key, float value) {
        mSp.edit().putFloat(key, value).apply();
    }

    public void put(String key, double value) {
        mSp.edit().putFloat(key, (float) value).apply();
    }

    public void put(String key, boolean value) {
        mSp.edit().putBoolean(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return mSp.getString(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return mSp.getInt(key, defaultValue);
    }

    public float getFloat(String key, float defaultValue) {
        return mSp.getFloat(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mSp.getBoolean(key, defaultValue);
    }

    private class Mode {
        public String name;
        public SharedPreferences preferences;
    }

}