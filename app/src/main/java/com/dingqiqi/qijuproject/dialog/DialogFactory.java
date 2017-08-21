package com.dingqiqi.qijuproject.dialog;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

/**
 * Created by Administrator on 2017/4/5.
 */
public class DialogFactory {
    /**
     * 弹框是否能取消
     */
    public static final String mCancelKey = "cancelKey";
    /**
     * 加载文字key
     */
    public static final String mLoadTextKey = "loadingKey";

    private static DialogFactory mDialogFactory;

    private FullScreenDialog mScreenDialog;

    private DialogFactory() {
    }

    public static DialogFactory getInstance() {
        if (mDialogFactory == null) {
            mDialogFactory = new DialogFactory();
        }
        return mDialogFactory;
    }

    public DialogFragment showTwoBtnDialog(FragmentManager manager, String title, String msg, String cancel, String ok, View.OnClickListener listener) {
//        FullScreenDialog mScreenDialog=new FullScreenDialog(manager, title, msg, cancel, ok, listener);
        FullScreenDialog mScreenDialog = new FullScreenDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("msg", msg);
        bundle.putString("cancel", cancel);
        bundle.putString("ok", ok);
        bundle.putInt("index", 2);

        mScreenDialog.setArguments(bundle);
        mScreenDialog.setListener(listener);
        mScreenDialog.show(manager, "");
        return mScreenDialog;
    }

    public DialogFragment showOneBtnDialog(FragmentManager manager, String title, String msg, String ok, View.OnClickListener listener) {
//        FullScreenDialog mScreenDialog=new FullScreenDialog(manager, title, msg, ok, listener);
        FullScreenDialog mScreenDialog = new FullScreenDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("msg", msg);
        bundle.putString("ok", ok);
        bundle.putInt("index", 1);

        mScreenDialog.setArguments(bundle);
        mScreenDialog.setListener(listener);
        mScreenDialog.show(manager, "");
        return mScreenDialog;
    }

    public void showNoBtnDialog(FragmentManager manager, String title, String msg, long time) {
        int value = (int) (time / 1000);

        if (value <= 0) {
            return;
        }
//        new FullScreenDialog(manager, title, msg, time);
        FullScreenDialog mScreenDialog = new FullScreenDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("msg", msg);
        bundle.putLong("time", time);
        bundle.putInt("index", 0);

        mScreenDialog.setArguments(bundle);
        mScreenDialog.show(manager, "");
    }

    public DialogFragment showOneBtnEditDialog(FragmentManager manager, String title, String hint, String ok, FullScreenDialog.onDialogClick dialogClick) {
//        FullScreenDialog mScreenDialog=new FullScreenDialog(manager, title, msg, ok, listener);
        FullScreenDialog mScreenDialog = new FullScreenDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("hint", hint);
        bundle.putString("ok", ok);
        bundle.putInt("index", 3);

        mScreenDialog.setArguments(bundle);
        mScreenDialog.show(manager, "");
        mScreenDialog.setDialogClick(dialogClick);
        return mScreenDialog;
    }

    public LoadingDialog showProgress(FragmentManager manager) {
        return showProgress(manager, "", false);
    }

    public LoadingDialog showProgress(FragmentManager manager, String loadText, boolean isCancel) {
        LoadingDialog mLoadingDialog = new LoadingDialog();
        Bundle bundle = new Bundle();
        bundle.putBoolean(mCancelKey, isCancel);
        bundle.putString(mLoadTextKey, loadText);

        mLoadingDialog.setArguments(bundle);
        mLoadingDialog.show(manager, "");

        return mLoadingDialog;
    }

}
