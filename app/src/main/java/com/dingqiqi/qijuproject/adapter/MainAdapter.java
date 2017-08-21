package com.dingqiqi.qijuproject.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.activity.MainActivity;
import com.dingqiqi.qijuproject.mode.MainModel;
import com.dingqiqi.qijuproject.util.CommonUtil;
import com.dingqiqi.qijuproject.util.DisplayUtil;
import com.dingqiqi.qijuproject.util.SaveUtil;

import java.util.Collections;
import java.util.List;

/**
 * Created by 丁奇奇 on 2017/5/23.
 */
public class MainAdapter extends BaseMoveAdapter {

    private Context mContext;
    private List<MainModel> mList;

    public MainAdapter(Context mContext, List<MainModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_main_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        Holder holder = (Holder) viewHolder;
        holder.mTextView.setText(mList.get(position).getName());

        int id = mContext.getResources().getIdentifier(mList.get(position).getIcon(), "mipmap", mContext.getPackageName());
        Drawable drawable = mContext.getResources().getDrawable(id);
        holder.mIvIcon.setBackgroundDrawable(drawable);

        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof MainActivity) {
                    ((MainActivity) mContext).onItemClick(mList.get(position).getPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        public LinearLayout mLinearLayout;
        public TextView mTextView;
        public ImageView mIvIcon;

        public Holder(View itemView) {
            super(itemView);

            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.linear_item);
            mTextView = (TextView) itemView.findViewById(R.id.tv_item_title);
            mIvIcon = (ImageView) itemView.findViewById(R.id.iv_item_icon);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) mLinearLayout.getLayoutParams();
            params.width = (int) ((DisplayUtil.getScreenWidth(mContext) - CommonUtil.mCountNum * mContext.getResources().getDimension(R.dimen.item_main_margin) * 2) / 2);
            params.height = params.width * 3 / 4;

        }
    }

    @Override
    public void itemMoved(int position, int targetPosition) {
        Collections.swap(mList, position, targetPosition);
        notifyItemMoved(position, targetPosition);

        SaveUtil.saveMenuList(mContext, mList);
    }

    @Override
    public void itemSwipe(int position) {

    }
}
