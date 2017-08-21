package com.dingqiqi.qijuproject.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.manager.GlideManager;
import com.dingqiqi.qijuproject.mode.WXFilterMode;

import java.util.List;

/**
 * 精选文章列表适配器
 * Created by 丁奇奇 on 2017/8/3.
 */
public class WXFilterAdapter extends BaseListAdapter {

    private final GlideManager mGlideManager;

    private Context mContext;
    private List<WXFilterMode> mList;
    //一张图片
    public final int TYPE_ONE = 1;
    //多张图片
    public final int TYPE_MORE = 2;
    //点击回调
    private onClick mOnClick;

    public WXFilterAdapter(Context mContext, List<WXFilterMode> mList) {
        super(mContext, mList);
        this.mContext = mContext;
        this.mList = mList;

        mGlideManager = new GlideManager(mContext);
    }

    //设置监听
    public void setOnClick(onClick mOnClick) {
        this.mOnClick = mOnClick;
    }

    @Override
    public int getItemViewType(int position) {
        List<String> list = mList.get(position).getImages();

        if (list != null && list.size() > 1) {
            return super.getItemViewType(position) - TYPE_MORE;
        }
        return super.getItemViewType(position) - TYPE_ONE;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public View getCustomItemView(ViewGroup parent, int viewType) {
        if (viewType == BaseListAdapter.BODY_TAG - TYPE_MORE) {
            return LayoutInflater.from(mContext).inflate(R.layout.item_wx_filter_more_layout, parent, false);
        } else if (viewType == BaseListAdapter.BODY_TAG - TYPE_ONE) {
            return LayoutInflater.from(mContext).inflate(R.layout.item_wx_filter_one_layout, parent, false);
        }

        return null;
    }

    @Override
    public ItemHolder getCustomItemHolder(View view, int viewType) {
        if (viewType == BaseListAdapter.BODY_TAG - TYPE_MORE) {
            return new HolderMore(view);
        } else if (viewType == BaseListAdapter.BODY_TAG - TYPE_ONE) {
            return new HolderOne(view);
        }
        return null;
    }

    @Override
    public void onItemBindHolder(final RecyclerView.ViewHolder holder, int position, int viewType) {

        if (holder instanceof HolderMore) {
            HolderMore mHolderMore = (HolderMore) holder;

            WXFilterMode wxFilterMode = mList.get(position);
            mHolderMore.mTvTitle.setText(wxFilterMode.getTitle());

            mHolderMore.mBgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClick != null) {
                        mOnClick.click(holder.getAdapterPosition());
                    }
                }
            });

            int size = wxFilterMode.getImages().size();
            for (int i = 0; i < size; i++) {
                switch (i) {
                    case 0:
                        String url = wxFilterMode.getImages().get(i);
                        if (TextUtils.isEmpty(url)) {
                            mHolderMore.mIvImage1.setVisibility(View.INVISIBLE);
                        } else {
                            mHolderMore.mIvImage1.setVisibility(View.VISIBLE);
                            mGlideManager.LoadImage(url, mHolderMore.mIvImage1);
                        }
                        break;
                    case 1:
                        url = wxFilterMode.getImages().get(i);
                        if (TextUtils.isEmpty(url)) {
                            mHolderMore.mIvImage2.setVisibility(View.INVISIBLE);
                        } else {
                            mHolderMore.mIvImage2.setVisibility(View.VISIBLE);
                            mGlideManager.LoadImage(url, mHolderMore.mIvImage2);
                        }
                        break;
                    case 2:
                        url = wxFilterMode.getImages().get(i);
                        if (TextUtils.isEmpty(url)) {
                            mHolderMore.mIvImage3.setVisibility(View.INVISIBLE);
                        } else {
                            mHolderMore.mIvImage3.setVisibility(View.VISIBLE);
                            mGlideManager.LoadImage(url, mHolderMore.mIvImage3);
                        }
                        break;
                }
            }
        } else if (holder instanceof HolderOne) {
            HolderOne mHolderOne = (HolderOne) holder;
            WXFilterMode wxFilterMode = mList.get(position);

            mHolderOne.mTvTitle.setText(wxFilterMode.getTitle());
            mHolderOne.mTvSubTitle.setText(wxFilterMode.getSubTitle());

            int size = wxFilterMode.getImages().size();
            if (size == 0) {
                mHolderOne.mIvImage.setVisibility(View.GONE);
            } else {
                String url = wxFilterMode.getImages().get(0);

                if (TextUtils.isEmpty(url)) {
                    mHolderOne.mIvImage.setVisibility(View.GONE);
                } else {
                    mHolderOne.mIvImage.setVisibility(View.VISIBLE);
                    mGlideManager.LoadImage(url, mHolderOne.mIvImage);
                }
            }

            mHolderOne.mBgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnClick != null) {
                        mOnClick.click(holder.getAdapterPosition());
                    }
                }
            });
        }
    }

    private class HolderOne extends ItemHolder {

        private View mBgView;
        private TextView mTvTitle;
        private TextView mTvSubTitle;
        private ImageView mIvImage;

        public HolderOne(View itemView) {
            super(itemView);

            mTvTitle = (TextView) itemView.findViewById(R.id.tv_item_wx_filter_title);
            mTvSubTitle = (TextView) itemView.findViewById(R.id.tv_item_wx_filter_sub_title);
            mBgView = itemView.findViewById(R.id.relative_wx_filter_bg);
            mIvImage = (ImageView) itemView.findViewById(R.id.iv_item_wx_filter_image);
        }
    }


    private class HolderMore extends ItemHolder {

        private View mBgView;
        private TextView mTvTitle;
        private ImageView mIvImage1;
        private ImageView mIvImage2;
        private ImageView mIvImage3;

        public HolderMore(View itemView) {
            super(itemView);

            mTvTitle = (TextView) itemView.findViewById(R.id.tv_item_wx_filter_title);
            mBgView = itemView.findViewById(R.id.relative_wx_filter_bg);
            mIvImage1 = (ImageView) itemView.findViewById(R.id.iv_item_wx_filter_image1);
            mIvImage2 = (ImageView) itemView.findViewById(R.id.iv_item_wx_filter_image2);
            mIvImage3 = (ImageView) itemView.findViewById(R.id.iv_item_wx_filter_image3);
        }
    }

    public interface onClick {
        void click(int pos);
    }
}
