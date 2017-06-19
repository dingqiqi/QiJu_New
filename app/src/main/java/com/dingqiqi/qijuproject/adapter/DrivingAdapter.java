package com.dingqiqi.qijuproject.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dingqiqi.qijuproject.fragment.BaseFragment;

import java.util.List;

/**
 * 驾考主页适配器
 * Created by dingqiqi on 2017/6/14.
 */
public class DrivingAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> mList;

    public DrivingAdapter(FragmentManager fm, List<BaseFragment> mList) {
        super(fm);
        this.mList = mList;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}
