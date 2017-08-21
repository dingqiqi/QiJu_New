package com.dingqiqi.qijuproject.popWindow;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.adapter.WeatherMoreAdapter;
import com.dingqiqi.qijuproject.mode.WeatherMode;
import com.dingqiqi.qijuproject.util.DisplayUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 未来是天天气弹框
 * Created by dingqiqi on 2017/6/8.
 */
public class WeatherMorePop {

    //    private Context mContext;
    private static WeatherMorePop mInstance;
    private final PopupWindow mPopupWindow;

    private WeatherMoreAdapter mAdapter;

    private List<WeatherMode.mode> mList;
    //标题信息
    private final TextView mTvTitle;

    public static WeatherMorePop getInstance(Context context, List<WeatherMode.mode> list) {
        if (mInstance == null) {
            mInstance = new WeatherMorePop(context);
        }

        if (list != null && list.size() > 0) {
            mInstance.notifyAdapter(list);
        }

        return mInstance;
    }

    private WeatherMorePop(Context context) {
        mList = new ArrayList<>();
//        this.mContext = context;

        mPopupWindow = new PopupWindow();
        View view = LayoutInflater.from(context).inflate(R.layout.pop_weather_layout, null, false);

        mPopupWindow.setContentView(view);
        mPopupWindow.setWidth(DisplayUtil.getScreenWidth(context));
        mPopupWindow.setHeight(DisplayUtil.getScreenHeight(context) - DisplayUtil.getStatusHeight(context));
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_weather_pop);
        mTvTitle = (TextView) view.findViewById(R.id.tv_title_pop);
        ImageView ivClose = (ImageView) view.findViewById(R.id.iv_close_pop);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(manager);
        mAdapter = new WeatherMoreAdapter(context, mList);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void notifyAdapter(List<WeatherMode.mode> list) {
        mList.clear();
        mList.addAll(list);

        if (mAdapter != null) {
            String text = mList.size() + "天趋势预报";
            mTvTitle.setText(text);
            mAdapter.notifyDataSetChanged();
            mAdapter.setLevel();
        }
    }

    public void showPop(View view) {
        if (mPopupWindow != null) {
            mPopupWindow.showAtLocation(view, Gravity.START | Gravity.TOP, 0, 0);
        }
    }

    public void dismiss() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

}
