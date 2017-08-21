package com.dingqiqi.qijuproject.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dingqiqi.qijuproject.dialog.DialogFactory;
import com.dingqiqi.qijuproject.dialog.LoadingDialog;

/**
 * Fragment 基类
 * Created by dingqiqi on 2017/6/14.
 */
public abstract class BaseFragment extends Fragment {
    //第一次加载
    private boolean mIsPrepared;

    public View mView;

    protected LoadingDialog mLoadingDialog;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIsPrepared = true;
        setUserVisibleHint(getUserVisibleHint());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = createView(inflater, container, savedInstanceState);
        initView(mView);
        return mView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //懒加载,只有在显示的时候才加载(取消预加载)
        if (mIsPrepared) {
            if (isVisibleToUser) {
                loadData();
            } else {
                onInvisible();
            }
        }
    }

    /**
     * 显示加载框
     */
    protected void showProgress() {
        mLoadingDialog = DialogFactory.getInstance().showProgress(getActivity().getSupportFragmentManager());
    }

    /**
     * 隐藏加载框
     */
    protected void dismissProgress() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }


    //初始化view
    public abstract View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);

    //初始化控件
    public abstract void initView(View view);

    //加载数据
    public abstract void loadData();

    //隐藏
    public abstract void onInvisible();

    //后退
    public abstract boolean onBack();

}
