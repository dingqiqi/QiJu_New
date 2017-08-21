package com.dingqiqi.qijuproject.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dingqiqi.qijuproject.R;

/**
 * 全屏加载弹框
 * Created by dingqiqi on 2017/4/5.
 */
public class LoadingDialog extends DialogFragment {
    //提示
    private boolean mIsCancel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        //setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen);
        //只是去除标题
        setStyle(STYLE_NO_TITLE, R.style.Dialog_FullScreen);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_loading_layout, container, false);

        mIsCancel = getArguments().getBoolean(DialogFactory.mCancelKey);
        String text = getArguments().getString(DialogFactory.mLoadTextKey);

        if (!TextUtils.isEmpty(text)) {
            TextView textView = (TextView) view.findViewById(R.id.tv_loading_text);
            textView.setText(text);
        }

        //弹框区域外点击不关闭
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

        //屏蔽后退键
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return !mIsCancel;
                }
                return false;
            }
        });

        return view;
    }

}
