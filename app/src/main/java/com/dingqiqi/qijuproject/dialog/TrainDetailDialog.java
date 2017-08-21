package com.dingqiqi.qijuproject.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.adapter.TrainDetailAdapter;
import com.dingqiqi.qijuproject.mode.TrainMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 全屏加载弹框
 * Created by dingqiqi on 2017/4/5.
 */
public class TrainDetailDialog extends DialogFragment {

    private List<TrainMode> mList = new ArrayList<>();
    private TrainDetailAdapter mAdapter;

    private TextView mTvCheCi;
    private TextView mTvTitle;

    public void setList(List<TrainMode> list) {
        mList.clear();
        this.mList.addAll(list);
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

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
        View view = inflater.inflate(R.layout.dialog_train_detail_layout, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_train);
        mTvCheCi = (TextView) view.findViewById(R.id.tv_checi);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title);

        //弹框区域外点击不关闭
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        mAdapter = new TrainDetailAdapter(getActivity(), mList);
        recyclerView.setAdapter(mAdapter);

        if (mList.size() > 0) {
            String text = mList.get(0).getStationTrainCode() + " (" + mList.get(0).getTrainClassName() + ")";
            mTvCheCi.setText(text);
            text = mList.get(0).getStartStationName() + "-->" + mList.get(0).getEndStationName();
            mTvTitle.setText(text);
        }

        //屏蔽后退键
        //        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
        //            @Override
        //            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        //                if (keyCode == KeyEvent.KEYCODE_BACK) {
        //                    return !mIsCancel;
        //                }
        //                return false;
        //            }
        //        });

        return view;
    }

}
