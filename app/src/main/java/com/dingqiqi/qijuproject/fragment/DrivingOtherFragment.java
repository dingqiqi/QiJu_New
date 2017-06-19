package com.dingqiqi.qijuproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.activity.itemActivity.DrivingOtherActivity;
import com.dingqiqi.qijuproject.adapter.DrivingFragmentAdapter;
import com.dingqiqi.qijuproject.dialog.DialogFactory;
import com.dingqiqi.qijuproject.dialog.LoadingDialog;
import com.dingqiqi.qijuproject.http.IDriving1Api;
import com.dingqiqi.qijuproject.http.IDrivingOtherApi;
import com.dingqiqi.qijuproject.manager.RetrofitManager;
import com.dingqiqi.qijuproject.mode.DrivingMenuMode;
import com.dingqiqi.qijuproject.mode.DrivingMode;
import com.dingqiqi.qijuproject.mode.DrivingOtherMode;
import com.dingqiqi.qijuproject.mode.OtherMode;
import com.dingqiqi.qijuproject.util.CommonUtil;
import com.dingqiqi.qijuproject.util.LogUtil;
import com.dingqiqi.qijuproject.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 科目Fragment
 * Created by dingqiqi on 2017/6/14.
 */
public class DrivingOtherFragment extends BaseFragment {

    private DrivingOtherMode mData;

    private final String[] mTitleArray = new String[]{"小车科目一", "小车科目四", "客车科目一", "客车科目四", "货车科目一", "货车科目四"};

    private final int[] mIconArray = new int[]{R.mipmap.ic_card, R.mipmap.ic_card
            , R.mipmap.ic_bus, R.mipmap.ic_bus,
            R.mipmap.ic_truck, R.mipmap.ic_truck};

    private RecyclerView mRecyclerView;
    //列数
    private final int mCloumnNum = 2;

    private List<DrivingMenuMode> mList;

    private DrivingFragmentAdapter mAdapter;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driving_other_layout, container, false);
    }

    @Override
    public void initView(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_driving_other);
        GridLayoutManager manager = new GridLayoutManager(getActivity(), mCloumnNum);
        mRecyclerView.setLayoutManager(manager);

        mList = new ArrayList<>();
        for (int i = 0; i < mTitleArray.length; i++) {
            DrivingMenuMode mode = new DrivingMenuMode();
            mode.setIcon(mIconArray[i]);
            mode.setName(mTitleArray[i]);
            mList.add(mode);
        }

        mAdapter = new DrivingFragmentAdapter(getActivity(), mList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setItemClickListener(new DrivingFragmentAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mData == null) {
                    return;
                }
                ArrayList<OtherMode> list = null;
                if (position == 0) {
                    list = mData.get小车科目一考试题库2016();
                } else if (position == 1) {
                    list = mData.get小车科目四考试题库2016();
                } else if (position == 2) {
                    list = mData.get客车科目一考试题库2016();
                } else if (position == 3) {
                    list = mData.get客车科目四考试题库2016();
                } else if (position == 4) {
                    list = mData.get货车科目一考试题库2016();
                } else if (position == 5) {
                    list = mData.get货车科目四考试题库2016();
                } else {
                    return;
                }

                Intent intent = new Intent(getActivity(), DrivingOtherActivity.class);
                intent.putParcelableArrayListExtra("data", list);
                getActivity().startActivity(intent);
            }
        });
    }


    @Override
    public void loadData() {
//        ToastUtil.showToast("加载数据");
        if (mData == null) {
            requestData();
        }
    }

    /**
     * 请求数据
     */
    @SuppressWarnings("ConstantConditions")
    private void requestData() {
        final LoadingDialog mDialog = DialogFactory.getInstance().showProgress(getActivity().getSupportFragmentManager());

        IDrivingOtherApi otherApi = RetrofitManager.getInstance(getActivity()).getRetrofit().create(IDrivingOtherApi.class);
        Call<DrivingOtherMode> call = otherApi.getDrivingData(CommonUtil.getKey());
        call.enqueue(new Callback<DrivingOtherMode>() {
            @Override
            public void onResponse(Call<DrivingOtherMode> call, Response<DrivingOtherMode> response) {
                mDialog.dismiss();
                if (response != null && response.body() != null && response.body().getResult() != null) {
                    mData = response.body().getResult();
                } else {
                    LogUtil.d("驾考专题列表获取失败!");
                }
            }

            @Override
            public void onFailure(Call<DrivingOtherMode> call, Throwable t) {
                mDialog.dismiss();
                LogUtil.d(t.getMessage());
            }
        });
    }

    @Override
    public void onInvisible() {
//        Log.i("aaa","onInvisible other");
    }

    @Override
    public boolean onBack() {
        return false;
    }

}
