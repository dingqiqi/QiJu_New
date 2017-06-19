package com.dingqiqi.qijuproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.mode.DrivingMenuMode;

import java.util.List;

/**
 * 驾考专项适配器
 * Created by dingqiqi on 2017/6/9.
 */
public class DrivingFragmentAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<DrivingMenuMode> mList;

    private onItemClickListener mItemClickListener;

    public DrivingFragmentAdapter(Context mContext, List<DrivingMenuMode> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_driving_menu_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof Holder) {
            Holder mHolder = (Holder) holder;
            mHolder.mTvTitle.setText(mList.get(position).getName());
            mHolder.mIvIcon.setImageResource(mList.get(position).getIcon());

            mHolder.mViewClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onItemClick(position);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        public ImageView mIvIcon;
        private TextView mTvTitle;
        public View mViewClick;

        public Holder(View itemView) {
            super(itemView);
            mIvIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            mViewClick = itemView.findViewById(R.id.rl_item_bg);
        }
    }

    public void setItemClickListener(onItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
    }

    /**
     * 选项点击监听
     */
    public interface onItemClickListener {
        void onItemClick(int position);
    }

}
