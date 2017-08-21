package com.dingqiqi.qijuproject.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.mode.BaseMode;
import com.dingqiqi.qijuproject.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 为RecyclerView添加头部尾部适配器
 * Created by dingqiqi on 2016/12/27.
 */
public abstract class BaseListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * view类型的标志
     */
    public static final int HEAD_TAG = 10000;
    public static final int BODY_TAG = 20000;
    public static final int FOOT_TAG = 30000;
    public static final int MORE_TAG = 40000;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * item数据源
     */
    private List<? extends BaseMode> mList;
    /**
     * 头部控件列表
     */
    private List<View> mHeadViews;
    /**
     * 尾部控件列表
     */
    private List<View> mFooterViews;
    /**
     * 加载更多布局
     */
    private View mLoadMoreView;
    /**
     * 是否有加载更多
     */
    private boolean mIsAddFooter = false;

    public BaseListAdapter(Context context, List<? extends BaseMode> list) {
        this.mContext = context;
        this.mList = list;

        mHeadViews = new ArrayList<>();
        mFooterViews = new ArrayList<>();
    }


    /**
     * 增加头部
     *
     * @param view
     */
    public void addHeadView(View view) {
        mHeadViews.add(view);
        notifyDataSetChanged();
    }

    /**
     * 移除头部
     *
     * @param view
     */
    public void removeHeadView(View view) {
        boolean flag = mHeadViews.remove(view);
        notifyDataSetChanged();
    }

    /**
     * 增加尾部
     *
     * @param view
     */
    public void addFooterView(View view) {
        mFooterViews.add(view);
    }

    /**
     * 移除尾部
     */
    public void removeFooterView(View view) {
        boolean flag = mFooterViews.remove(view);
        LogUtil.i("removeFooterView " + mFooterViews.size());
    }

    /**
     * 增加尾部
     *
     * @param view
     */
    public void addLoadMore(View view) {
        mLoadMoreView = view;
    }

    /**
     * 开启
     *
     * @param flag true 加载更多布局  false 关闭加载更多布局
     */
    public void loadMore(boolean flag) {
        mIsAddFooter = flag;
        if (flag) {
            notifyItemInserted(getItemCount());
        } else {
            notifyItemRemoved(getItemCount());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mHeadViews.size()) {
            return HEAD_TAG - position;
        } else if (position < mHeadViews.size() + mList.size()) {
            return BODY_TAG;
        } else if (position < mHeadViews.size() + mList.size() + mFooterViews.size()) {
            return FOOT_TAG - position;
        } else {
            return MORE_TAG;
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder mHolder;

        if (viewType <= HEAD_TAG) {
            View view = mHeadViews.get(HEAD_TAG - viewType);
            if (view == null) {
                view = getCustomItemView(parent, viewType);
            }

            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_base_head_layout, parent, false);
                mHolder = new HeadHolder(view);
            } else {
                mHolder = getCustomItemHolder(view, viewType);
            }

            if (mHolder == null) {
                mHolder = new HeadHolder(view);
            }

        } else if (viewType <= BODY_TAG) {
            View view = getCustomItemView(parent, viewType);

            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_base_body_layout, parent, false);
                mHolder = new ItemHolder(view);
            } else {
                mHolder = getCustomItemHolder(view, viewType);
            }

            if (mHolder == null) {
                mHolder = new ItemHolder(view);
            }
        } else if (viewType <= FOOT_TAG) {
            View view = mFooterViews.get(FOOT_TAG - viewType - mHeadViews.size() - mList.size());
            if (view == null) {
                view = getCustomItemView(parent, viewType);
            }

            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.item_base_footer_layout, parent, false);
                mHolder = new FooterHolder(view);
            } else {
                mHolder = getCustomItemHolder(view, viewType);
            }

            if (mHolder == null) {
                mHolder = new FooterHolder(view);
            }

        } else {
            if (mLoadMoreView == null) {
                mLoadMoreView = LayoutInflater.from(mContext).inflate(R.layout.base_load_more_layout, parent, false);
            }
            mHolder = new MoreHolder(mLoadMoreView);
        }

        return mHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type <= HEAD_TAG) {
            if (holder instanceof HeadHolder) {
                onItemBindHolder(holder, position, type);
            }
        } else if (type <= BODY_TAG) {
            if (holder instanceof ItemHolder) {
                onItemBindHolder(holder, position - mHeadViews.size(), type);
            }
        } else if (type <= FOOT_TAG) {
            if (holder instanceof FooterHolder) {
                onItemBindHolder(holder, position - mHeadViews.size() - mList.size(), type);
            }
        } else {
            if (holder instanceof MoreHolder) {
                onItemBindHolder(holder, getItemCount() - 1, type);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        //GridLayoutManager设置头尾占父控件宽度
        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
            final GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();

            manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean flag = getItemViewType(position) <= BODY_TAG && getItemViewType(position) > HEAD_TAG;
                    return flag ? 1 : manager.getSpanCount();
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        //瀑布流设置头尾占父控件宽度
        if (isStaggeredGridLayout(holder)) {
            handleLayoutIfStaggeredGridLayout(holder, holder.getLayoutPosition());
        }
    }

    private boolean isStaggeredGridLayout(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams != null && layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
            return true;
        }
        return false;
    }

    protected void handleLayoutIfStaggeredGridLayout(RecyclerView.ViewHolder holder, int position) {
        boolean flag = getItemViewType(position) <= BODY_TAG && getItemViewType(position) > HEAD_TAG;
        if (!flag) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
            p.setFullSpan(true);
        }
    }


    @Override
    public int getItemCount() {
        return mHeadViews.size() + mList.size() + mFooterViews.size() + (mIsAddFooter ? 1 : 0);
    }

    /**
     * 头部Holder
     */
    public class HeadHolder extends RecyclerView.ViewHolder {

        public HeadHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 中部Holder
     */
    public class ItemHolder extends RecyclerView.ViewHolder {

        public ItemHolder(View itemView) {
            super(itemView);
        }
    }


    /**
     * 尾部Holder
     */
    public class FooterHolder extends RecyclerView.ViewHolder {

        public FooterHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 更多Holder
     */
    public class MoreHolder extends RecyclerView.ViewHolder {

        public MoreHolder(View itemView) {
            super(itemView);
        }
    }

    public abstract View getCustomItemView(ViewGroup parent, int viewType);

    public abstract ItemHolder getCustomItemHolder(View view, int viewType);

    public abstract void onItemBindHolder(RecyclerView.ViewHolder holder, int position, int viewType);

}


