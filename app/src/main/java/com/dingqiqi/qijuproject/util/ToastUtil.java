package com.dingqiqi.qijuproject.util;

import android.content.Context;
import android.widget.Toast;

/**
 * 提示信息工具类
 */
public class ToastUtil {
    private static Toast toast;
    private static Context context;

    private ToastUtil() {}

    public static void init(Context ctx) {
        context = ctx;
    }

    public static void showToast(String text){
        if (toast != null){
            toast.cancel();
        }

        toast = Toast.makeText(context, text, Toast.LENGTH_LONG);

        toast.setText(text);
        toast.setDuration(Toast.LENGTH_LONG);

        toast.show();
    }

    public static void showToast(Context context, String text){
        if (toast == null){
            toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        }else {
            toast.setText(text);
            toast.setDuration(Toast.LENGTH_SHORT);
        }
        toast.show();
    }
}
