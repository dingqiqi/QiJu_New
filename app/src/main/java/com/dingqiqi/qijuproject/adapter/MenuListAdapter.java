package com.dingqiqi.qijuproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.manager.GlideManager;
import com.dingqiqi.qijuproject.mode.BaseMode;
import com.dingqiqi.qijuproject.mode.MenuMode;

import java.util.List;

/**
 * 菜谱列表适配器
 * Created by dingqiqi on 2017/1/17.
 */
public class MenuListAdapter extends BaseListAdapter {

    private Context mContext;
    private List<? extends BaseMode> mList;

    private GlideManager mGlideManager;

    private onClickCallBack mCallBack;

    public MenuListAdapter(Context context, List<? extends BaseMode> list) {
        super(context, list);
        mContext = context;
        mList = list;
        mGlideManager = new GlideManager(mContext);
    }

    public void setCallBack(onClickCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    @Override
    public View getCustomItemView(ViewGroup parent, int viewType) {

        if (viewType == BaseListAdapter.BODY_TAG) {
            return LayoutInflater.from(mContext).inflate(R.layout.item_menu_layout, parent, false);
        }

        return null;
    }

    @Override
    public ItemHolder getCustomItemHolder(View view, int viewType) {
        if (viewType == BaseListAdapter.HEAD_TAG) {
            return new HeaderHolder(view);
        } else if (viewType == BaseListAdapter.BODY_TAG) {
            return new Holder(view);
        } else if (viewType == BaseListAdapter.FOOT_TAG) {
            return null;
        } else if (viewType == BaseListAdapter.MORE_TAG) {
            return null;
        }
        return null;
    }

    @Override
    public void onItemBindHolder(RecyclerView.ViewHolder holder, final int position, int viewType) {
        //body Holder
        if (holder instanceof Holder) {
            Holder mHolder = (Holder) holder;
            BaseMode baseMode = mList.get(position);

            if (baseMode instanceof MenuMode) {
                MenuMode menuMode = (MenuMode) baseMode;

                mHolder.mTvTitle.setText(menuMode.getName());
                mHolder.mTvType.setText(menuMode.getCtgTitles());


                String imgUrl = menuMode.getThumbnail();

                if (!TextUtils.isEmpty(imgUrl)) {
                    mGlideManager.LoadImage(imgUrl, mHolder.mIvIcon);
                } else {
                    mHolder.mIvIcon.setImageResource(R.mipmap.default_bg);
                }

                mHolder.mLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mCallBack != null) {
                            mCallBack.onClick(position);
                        }
                    }
                });

                mHolder.mLayout.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (mCallBack != null) {
                            mCallBack.onLongClick(position);
                        }
                        return true;
                    }
                });
            }
        }

    }

    /**
     * 重写bodyView
     */
    private class Holder extends ItemHolder {

        private RelativeLayout mLayout;
        private ImageView mIvIcon;
        private TextView mTvTitle;
        private TextView mTvType;

        public Holder(View itemView) {
            super(itemView);
            mLayout = (RelativeLayout) itemView.findViewById(R.id.relative_bg);
            mIvIcon = (ImageView) itemView.findViewById(R.id.iv_item_menu_icon);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_item_menu_title);
            mTvType = (TextView) itemView.findViewById(R.id.tv_item_menu_type);
        }
    }

    /**
     * 重写headView
     */
    private class HeaderHolder extends ItemHolder {

        public HeaderHolder(View itemView) {
            super(itemView);
        }
    }

    public interface onClickCallBack {
        void onClick(int pos);

        void onLongClick(int pos);
    }

}
