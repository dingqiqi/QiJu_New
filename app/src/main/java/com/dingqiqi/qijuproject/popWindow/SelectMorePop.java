package com.dingqiqi.qijuproject.popWindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.dingqiqi.qijuproject.util.DisplayUtil;

/**
 * 选择弹框
 * Created by dingqiqi on 2017/8/1.
 */
public class SelectMorePop {

    private static SelectMorePop mInstance;

    private SelectMorePop() {

    }

    public static SelectMorePop getInstance() {
        if (mInstance == null) {
            mInstance = new SelectMorePop();
        }

        return mInstance;
    }

    public PopupWindow showSelectView(Context context, View view, View showView) {
        PopupWindow popupWindow = new PopupWindow();
        popupWindow.setContentView(view);
        popupWindow.setWidth(160);
        popupWindow.setHeight(DisplayUtil.dp2px(context, 40));
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        //上面
        //popupWindow.showAsDropDown(showView, DisplayUtil.getScreenWidth(context) / 2 - 80, -DisplayUtil.dp2px(context, 40) - showView.getMeasuredHeight());
        //下面
        popupWindow.showAsDropDown(showView, DisplayUtil.getScreenWidth(context) / 2 - 80, 0);

        return popupWindow;
    }

}
