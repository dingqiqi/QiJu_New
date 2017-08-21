package com.dingqiqi.qijuproject.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.mode.BaseMode;
import com.dingqiqi.qijuproject.mode.MenuFilterMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单适配器
 * Created by Administrator on 2017/7/20.
 */
public class MenuFilterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<? extends BaseMode> mList;

    private onClickCallBack mCallBack;
    /**
     * 选中的下标
     */
    private int mSelPos = -1;

    public MenuFilterAdapter(Context mContext, List<? extends BaseMode> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    public void setCallBack(onClickCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_menu_filter_layout, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof Holder) {
            Holder mHolder = (Holder) holder;
            BaseMode baseMode = mList.get(position);
            if (baseMode instanceof MenuFilterMode) {
                final MenuFilterMode menuMode = (MenuFilterMode) baseMode;

                if (mSelPos == position) {
                    mHolder.mLinearBg.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimaryStatusBar1));
                    mHolder.mTvTitle.setTextColor(Color.WHITE);
                } else {
                    mHolder.mLinearBg.setBackgroundColor(mContext.getResources().getColor(R.color.translate));
                    mHolder.mTvTitle.setTextColor(mContext.getResources().getColor(R.color.colorText));
                }

                mHolder.mTvTitle.setText(menuMode.getName());

                mHolder.mTvTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCallBack != null) {
                            mCallBack.onClick(position, menuMode.getParentId());
                        }
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void setSelectPos(int selectPos) {
        this.mSelPos = selectPos;
    }

    private class Holder extends RecyclerView.ViewHolder {

        private TextView mTvTitle;
        private LinearLayout mLinearBg;

        public Holder(View itemView) {
            super(itemView);

            mTvTitle = (TextView) itemView.findViewById(R.id.tv_item_menu);

            mLinearBg = (LinearLayout) itemView.findViewById(R.id.linear_bg);
        }
    }

    public interface onClickCallBack {
        void onClick(int position, String parentId);
    }
}
