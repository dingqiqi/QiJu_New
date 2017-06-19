package com.dingqiqi.qijuproject.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.mode.TrainMode;

import java.util.List;

/**
 * 火车时刻表适配器
 * Created by dingqiqi on 2017/6/19.
 */
public class TrainDetailAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<TrainMode> mList;

    public TrainDetailAdapter(Context mContext, List<TrainMode> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_train_detail_layout, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Holder) {
            Holder mHolder = (Holder) holder;
            mHolder.mCirle.setVisibility(View.VISIBLE);
            mHolder.mLine.setVisibility(View.VISIBLE);

            if (position == 0 || position == getItemCount() - 1) {
                mHolder.mCirle.setBackgroundResource(R.drawable.bg_circle_fill_shape);
            } else {
                mHolder.mCirle.setBackgroundResource(R.drawable.bg_circle_stroke_shape);
            }

            if (mList == null || mList.size() == 0) {
                return;
            }

            if (position == getItemCount() - 1) {
                mHolder.mLine.setVisibility(View.INVISIBLE);
            }

            mHolder.mTvSort.setText(mList.get(position).getStationNo());
            mHolder.mTvEnd.setText(mList.get(position).getStationName());
            mHolder.mTvTL.setText(mList.get(position).getStopoverTime());
            mHolder.mTvStartTime.setText(mList.get(position).getArriveTime());
            mHolder.mTvEndTime.setText(mList.get(position).getStartTime());

            mHolder.mTvSort.setTextColor(Color.WHITE);
            mHolder.mTvEnd.setTextColor(Color.WHITE);
            mHolder.mTvTL.setTextColor(Color.WHITE);
            mHolder.mTvStartTime.setTextColor(Color.WHITE);
            mHolder.mTvEndTime.setTextColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        if (mList == null) {
            return 0;
        }
        return mList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        //站点顺序
        public TextView mTvSort;
        //到达站
        public TextView mTvEnd;
        //停留
        public TextView mTvTL;
        public View mCirle;
        public View mLine;
        //到达时间
        public TextView mTvStartTime;
        //出发时间
        public TextView mTvEndTime;

        public Holder(View itemView) {
            super(itemView);
            mTvSort = (TextView) itemView.findViewById(R.id.tv_train_item_sort);
            mTvEnd = (TextView) itemView.findViewById(R.id.tv_train_end);
            mTvTL = (TextView) itemView.findViewById(R.id.tv_train_tl);
            mCirle = itemView.findViewById(R.id.view_train_circle);
            mLine = itemView.findViewById(R.id.view_train_line);
            mTvStartTime = (TextView) itemView.findViewById(R.id.tv_train_end_time);
            mTvEndTime = (TextView) itemView.findViewById(R.id.tv_train_start_time);
        }
    }

}
