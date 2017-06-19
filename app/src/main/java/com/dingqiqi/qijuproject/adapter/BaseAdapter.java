package com.dingqiqi.qijuproject.adapter;

import android.support.v7.widget.RecyclerView;

/**
 * Created by dingqiqi on 2017/5/24.
 */
public abstract class BaseAdapter extends RecyclerView.Adapter {

    public abstract void itemMoved(int position, int targetPosition);

    public abstract void itemSwipe(int position);

}
