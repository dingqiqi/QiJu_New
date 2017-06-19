package com.dingqiqi.qijuproject.util;

import android.content.Context;
import android.text.TextUtils;

import com.dingqiqi.qijuproject.mode.MainModel;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dingqiqi on 2017/5/24.
 */
public class SaveUtil {
    /**
     * 存储主页菜单
     *
     * @param context
     * @param list
     */
    public static void saveMenuList(Context context, List<MainModel> list) {
        if (list == null || list.size() == 0) {
            return;
        }

        Gson gson = new Gson();
        StringBuffer buffer = new StringBuffer();

        for (MainModel mode : list) {
            String json = gson.toJson(mode);
            buffer.append(json + ";");
        }

        String json = buffer.substring(0, buffer.length() - 1);
        SharedPreUtil.getInstance(context, SharedPreUtil.mMainMenuFileName).put(SharedPreUtil.mMainMenuKey, json);
    }

    /**
     * 获取主页菜单
     *
     * @param context
     * @return 菜单列表
     */
    public static List<MainModel> getMenuList(Context context) {
        List<MainModel> list = new ArrayList<>();
        Gson gson = new Gson();
        String menuJson = SharedPreUtil.getInstance(context, SharedPreUtil.mMainMenuFileName).getString(SharedPreUtil.mMainMenuKey, "");

        if (TextUtils.isEmpty(menuJson)) {
            return list;
        }

        String[] strArray = menuJson.split(";");
        int length = strArray.length;

        for (int i = 0; i < length; i++) {
            MainModel model = gson.fromJson(strArray[i], MainModel.class);
            list.add(model);
        }

        return list;
    }

}
