package com.dingqiqi.qijuproject.util;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * 输入法相关
 * Created by Administrator on 2017/7/14.
 */
public class InputMethodUtil {

    private static InputMethodUtil mInstance;

    private InputMethodManager mInputMethodManager;

    private InputMethodUtil(Context context) {
        mInputMethodManager = (InputMethodManager) context.getSystemService(
                Context.INPUT_METHOD_SERVICE);
    }

    public static InputMethodUtil getInstance(Context context) {
        if (mInstance == null) {
            synchronized (InputMethodUtil.class) {
                mInstance = new InputMethodUtil(context);
            }
        }
        return mInstance;
    }

    /**
     * 强制隐藏输入法
     *
     * @param view 控件
     */
    public void hideSoftInput(View view) {
        if (mInputMethodManager.isActive()) {
            mInputMethodManager.hideSoftInputFromWindow(
                    view.getApplicationWindowToken(), 0);
        }
    }

    /**
     * 强制显示输入法
     *
     * @param view 控件
     */
    public void showSoftInput(View view) {
        if (!mInputMethodManager.isActive()) {
            mInputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
        }
    }

    /**
     * 动态显示隐藏输入法
     */
    public void showOrHideSoftInput() {
        mInputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
