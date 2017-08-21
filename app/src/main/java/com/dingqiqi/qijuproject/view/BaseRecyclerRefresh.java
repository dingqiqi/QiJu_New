package com.dingqiqi.qijuproject.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.adapter.BaseListAdapter;

/**
 * 列表 刷新 加载更多
 * Created by 丁奇奇 on 2016/12/27.
 */
@SuppressWarnings("deprecation")
public class BaseRecyclerRefresh extends FrameLayout implements SwipeRefreshLayout.OnRefreshListener {
    /**
     * 默认初始页
     */
    private int mInitPage = 1;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 下拉刷新控件
     */
    private SwipeRefreshLayout mRefreshLayout;
    /**
     * 列表控件
     */
    private RecyclerView mRecyclerView;
    /**
     * 存储瀑布流下标
     */
    private int[] mLastPositions;
    /**
     * 是否能加载更多
     */
    private boolean mIsLoadMore = true;
    /**
     * 是否添加加载更多
     */
    private boolean mIsAddLoadMore = true;
    /**
     * 初始状态
     */
    private Status mStatus = Status.NONE;
    /**
     * 加载刷新回调
     */
    private BaseCallBack mCallBack;

    private View mFooterView;
    /**
     * 当前页
     */
    private int mCurrentPage;
    /**
     * 是否增加加载布局
     */
    private boolean mIsAddFooterView = false;
    /**
     * 适配器
     */
    private BaseListAdapter mAdapter;

    /**
     * 状态 枚举类型
     */
    public enum Status {
        REFRESHING,
        LOADMORE,
        NONE;
    }

    public BaseRecyclerRefresh(Context context) {
        super(context);
        mContext = context;
        initView();
    }

    public BaseRecyclerRefresh(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public BaseRecyclerRefresh(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        mCurrentPage = mInitPage;

        ViewGroup.inflate(mContext, R.layout.base_refresh_recycler_layout, this);

        mFooterView = LayoutInflater.from(mContext).inflate(R.layout.base_load_more_layout, this, false);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout_base);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycleView_base);

        //背景圆颜色
        mRefreshLayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        //圆圈颜色
        mRefreshLayout.setColorSchemeResources(R.color.holo_blue_light,
                R.color.holo_red_light, R.color.holo_orange_light,
                R.color.holo_green_light);
        //设置下拉刷新监听
        mRefreshLayout.setOnRefreshListener(this);

        //加载更多
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {

                    int index = 0;
                    //获取当前页面最后一个position
                    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        index = manager.findLastVisibleItemPosition();

                    } else if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
                        GridLayoutManager manager = (GridLayoutManager) recyclerView.getLayoutManager();
                        index = manager.findLastVisibleItemPosition();

                    } else if (recyclerView.getLayoutManager() instanceof StaggeredGridLayoutManager) {
                        StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
                        if (mLastPositions == null) {
                            mLastPositions = new int[manager.getSpanCount()];
                        }
                        manager.findLastVisibleItemPositions(mLastPositions);
                        index = findMax(mLastPositions);
                    }

                    //只有在空闲是才能加载更多
                    if (mStatus == Status.NONE) {
                        if (recyclerView.getAdapter() != null && index != 0 && recyclerView.getAdapter().getItemCount() - 1 == index && mIsLoadMore && mIsAddLoadMore) {
                            if (recyclerView.getAdapter() instanceof BaseListAdapter) {
                                mCurrentPage++;
                                mStatus = Status.LOADMORE;
                                mAdapter.loadMore(true);
                                if (mCallBack != null) {
                                    mCallBack.refresh(mStatus, mCurrentPage);
                                }
                            }
                        }
                    }

                }

            }
        });
    }

    public void setAdapter(BaseListAdapter adapter) {
        if (adapter != null) {
            mAdapter = adapter;
            mRecyclerView.setAdapter(adapter);
            adapter.addLoadMore(mFooterView);
        }
    }

    /**
     * 刷新列表
     *
     * @param pos 某一项item
     */
    public void notifyItemChanged(int pos) {
        if (mAdapter != null) {
            if (pos != -1) {
                mAdapter.notifyItemChanged(pos);
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * 设置初始页
     *
     * @param mInitPage 初始页
     */
    public void setInitPage(int mInitPage) {
        this.mInitPage = mInitPage;
    }

    /**
     * 获取当前页
     *
     * @return 当前页
     */
    public int getCurrentPage() {
        return mCurrentPage;
    }

    /**
     * 设置当前页
     */
    public void setCurrentPage(int mCurrentPage) {
        this.mCurrentPage = mCurrentPage;
    }

    /**
     * 获取当前列表控件
     *
     * @return 列表控件
     */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * 设置回调
     *
     * @param callBack 回调
     */
    public void setCallBack(BaseCallBack callBack) {
        this.mCallBack = callBack;
    }

    /**
     * 滑动
     */
    public void smoothScrollTo() {
        if (mRecyclerView != null) {
            mRecyclerView.smoothScrollToPosition(0);
        }
    }

    /**
     * 增加尾部
     */
    public void addFooterView() {
        if (mRecyclerView.getAdapter() != null && mRecyclerView.getAdapter() instanceof BaseListAdapter) {
            if (!mIsAddFooterView) {
                ((BaseListAdapter) mRecyclerView.getAdapter()).addFooterView(mFooterView);
                mIsAddFooterView = true;
            }
        }
    }

    /**
     * 启动刷新
     */
    public void startRefresh() {
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(true);
                mStatus = Status.REFRESHING;
            }
        });
        onRefresh();
    }

    /**
     * 刷新停止
     */
    public void stopRefresh() {
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
                mStatus = Status.NONE;
            }
        });
    }

    /**
     * 加载更多停止
     */
    public void stopLoadMore() {
        ((BaseListAdapter) mRecyclerView.getAdapter()).loadMore(false);
        mStatus = Status.NONE;
    }

    /**
     * 加载失败
     */
    public void stopErrorLoad() {
        stopLoadMore();
        if (mCurrentPage > mInitPage) {
            mCurrentPage--;
        }
    }

    /**
     * 设置能否加载更多
     */
    public void setLoadMoreEnable(boolean flag) {
        mIsLoadMore = flag;
    }

    /**
     * 设置是否添加加载更多功能
     */
    public void addLoadMoreEnable(boolean flag) {
        mIsAddLoadMore = flag;
    }

    /**
     * 设置能否下拉刷新
     *
     * @param flag true 能 false 不能
     */
    public void setRefreshEnable(boolean flag) {
        mRefreshLayout.setEnabled(flag);
    }

    /**
     * 获取最大下标
     *
     * @param lastPositions 最大下标
     * @return int
     */
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    @Override
    public void onRefresh() {
        if (mStatus == Status.LOADMORE) {
            //刷新时停止加载更多
            stopLoadMore();
        }
        if (mStatus == Status.NONE) {
            mCurrentPage = mInitPage;
            mStatus = Status.REFRESHING;
            if (mCallBack != null) {
                mCallBack.refresh(mStatus, mCurrentPage);
            }
        }
    }

    /**
     * 刷新加载回调
     */
    public interface BaseCallBack {
        void refresh(Status status, int curPage);
    }

}
