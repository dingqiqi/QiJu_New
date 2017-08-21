package com.dingqiqi.qijuproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.activity.BaseWebActivity;
import com.dingqiqi.qijuproject.adapter.WXFilterAdapter;
import com.dingqiqi.qijuproject.http.IWXFilterApi;
import com.dingqiqi.qijuproject.manager.RetrofitManager;
import com.dingqiqi.qijuproject.mode.WXFilterMode;
import com.dingqiqi.qijuproject.util.CommonUtil;
import com.dingqiqi.qijuproject.util.LogUtil;
import com.dingqiqi.qijuproject.util.ToastUtil;
import com.dingqiqi.qijuproject.view.BaseRecyclerRefresh;
import com.dingqiqi.qijuproject.view.RecyLine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 精选文章
 * Created by 丁奇奇 on 2017/8/2.
 */
public class WXFilterFragment extends BaseFragment implements BaseRecyclerRefresh.BaseCallBack, WXFilterAdapter.onClick {

    private BaseRecyclerRefresh mRecyclerRefresh;

    private String mTitleId;

    private List<WXFilterMode> mList = new ArrayList<>();

    /**
     * 当前页
     */
    private int mCurPage = 1;
    /**
     * 一页大小
     */
    private int mPageSize = 10;
    /**
     * 总共条数
     */
    private int mTotal;
    /**
     * 当前类型
     */
    private BaseRecyclerRefresh.Status mStatus = BaseRecyclerRefresh.Status.REFRESHING;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_wx_filter_layout, container, false);
    }

    /**
     * 设置标题id
     *
     * @param id 标题id
     */
    public void setTitleId(String id) {
        mTitleId = id;
    }

    @Override
    public void initView(View view) {
        mRecyclerRefresh = (BaseRecyclerRefresh) view.findViewById(R.id.recycler_refresh_wx_filter);

        RecyclerView recyclerView = mRecyclerRefresh.getRecyclerView();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecyLine mLine = new RecyLine(getActivity());
        recyclerView.addItemDecoration(mLine);

        WXFilterAdapter wxFilterAdapter = new WXFilterAdapter(getActivity(), mList);
        mRecyclerRefresh.setAdapter(wxFilterAdapter);
        wxFilterAdapter.setOnClick(this);
        mRecyclerRefresh.setCallBack(this);
        mRecyclerRefresh.setInitPage(1);
    }

    @Override
    public void loadData() {
        //第一次加载
        if (mList.size() == 0) {
            mRecyclerRefresh.startRefresh();
        }
    }

    /**
     * 请求文章数据
     */
    private void requestWxData() {
        if (mStatus == BaseRecyclerRefresh.Status.LOADMORE) {
            showProgress();
        }

        IWXFilterApi filterApi = RetrofitManager.getInstance(getActivity()).getRetrofit().create(IWXFilterApi.class);
        Call<WXFilterMode> call = filterApi.getWXFilterData(CommonUtil.getKey(), mTitleId, mCurPage, mPageSize);
        call.enqueue(new Callback<WXFilterMode>() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onResponse(Call<WXFilterMode> call, Response<WXFilterMode> response) {
                if (mStatus == BaseRecyclerRefresh.Status.LOADMORE) {
                    dismissProgress();
                }

                if (response.body() != null && response.body().getResult() != null) {
                    mTotal = response.body().getResult().getTotal();

                    if (mCurPage * mPageSize >= mTotal) {
                        mRecyclerRefresh.setLoadMoreEnable(false);
                    } else {
                        mRecyclerRefresh.setLoadMoreEnable(true);
                    }

                    if (mStatus == BaseRecyclerRefresh.Status.REFRESHING) {
                        mList.clear();
                    }

                    List<WXFilterMode> list = response.body().getResult().getList();
                    int size = list.size();

                    for (int i = 0; i < size; i++) {
                        List<String> images = new ArrayList<>();
                        String thumbnails = list.get(i).getThumbnails();

                        if (!TextUtils.isEmpty(thumbnails)) {
                            String[] imgs = thumbnails.split("\\$");
                            Collections.addAll(images, imgs);
                        }

                        list.get(i).setImages(images);
                    }

                    mList.addAll(list);
                    mRecyclerRefresh.notifyItemChanged(-1);

                    if (mStatus == BaseRecyclerRefresh.Status.LOADMORE) {
                        mRecyclerRefresh.stopLoadMore();
                    } else {
                        mRecyclerRefresh.stopRefresh();
                        mRecyclerRefresh.smoothScrollTo();
                    }
                } else {
                    if (mStatus == BaseRecyclerRefresh.Status.LOADMORE) {
                        mRecyclerRefresh.stopErrorLoad();
                    } else {
                        mRecyclerRefresh.stopRefresh();
                    }
                    String text = response.body() != null ? response.body().getMsg() : "获取数据失败!";
                    ToastUtil.showToast(text);
                }

            }

            @Override
            public void onFailure(Call<WXFilterMode> call, Throwable t) {
                if (mStatus == BaseRecyclerRefresh.Status.LOADMORE) {
                    mRecyclerRefresh.stopErrorLoad();
                    dismissProgress();
                } else {
                    mRecyclerRefresh.stopRefresh();
                }
                ToastUtil.showToast(t.getMessage());
                LogUtil.d(t.getMessage());

            }
        });
    }

    @Override
    public void onInvisible() {

    }

    @Override
    public boolean onBack() {
        return false;
    }

    @Override
    public void refresh(BaseRecyclerRefresh.Status status, int curPage) {
        mStatus = status;
        mCurPage = curPage;

        if (status == BaseRecyclerRefresh.Status.REFRESHING) {
            requestWxData();

        } else if (status == BaseRecyclerRefresh.Status.LOADMORE) {
            requestWxData();
        }
    }

    @Override
    public void click(int pos) {
        Intent intent = new Intent(getActivity(), BaseWebActivity.class);
        intent.putExtra("title", "文章精选");
        intent.putExtra("url", mList.get(pos).getSourceUrl());
        getActivity().startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mList.clear();
        mRecyclerRefresh.notifyItemChanged(-1);
    }
}
