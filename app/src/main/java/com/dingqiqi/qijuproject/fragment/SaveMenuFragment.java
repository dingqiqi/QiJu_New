package com.dingqiqi.qijuproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.activity.CookDetailActivity;
import com.dingqiqi.qijuproject.adapter.MenuListAdapter;
import com.dingqiqi.qijuproject.db.SqLiteUtil;
import com.dingqiqi.qijuproject.mode.MenuMode;
import com.dingqiqi.qijuproject.popWindow.SelectMorePop;
import com.dingqiqi.qijuproject.util.ToastUtil;
import com.dingqiqi.qijuproject.view.BaseRecyclerRefresh;
import com.dingqiqi.qijuproject.view.RecyLine;
import com.dingqiqi.qijuproject.view.TestRadiusView;

import java.util.ArrayList;
import java.util.List;

/**
 * 收藏菜谱
 * Created by 丁奇奇 on 2017/7/20.
 */
public class SaveMenuFragment extends BaseFragment implements BaseRecyclerRefresh.BaseCallBack, MenuListAdapter.onClickCallBack {
    private BaseRecyclerRefresh mRecyclerRefresh;
    /**
     * 菜谱数据源
     */
    private List<MenuMode> mList = new ArrayList<>();
    /**
     * 当前页
     */
    private int mCurPage = 1;
    /**
     * 一页大小
     */
    private int mPageSize = 10;
    /**
     * 总共条数
     */
    private int mTotal;
    /**
     * 当前类型
     */
    private BaseRecyclerRefresh.Status mStatus = BaseRecyclerRefresh.Status.REFRESHING;
    /**
     * 弹框view
     */
    private View mPopView;
    /**
     * 长按的下标
     */
    private int mSelPos;
    /**
     * 选择
     */
    private PopupWindow mPopSelect;
    /**
     * 无数据显示view
     */
    private View mEmptyView;

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_save_menu_layout, container, false);
    }

    @Override
    public void initView(View view) {
        mEmptyView = view.findViewById(R.id.linear_empty);
        mRecyclerRefresh = (BaseRecyclerRefresh) view.findViewById(R.id.recycler_save_refresh);

        RecyclerView recyclerView = mRecyclerRefresh.getRecyclerView();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecyLine mLine = new RecyLine(getActivity());
        recyclerView.addItemDecoration(mLine);

        final MenuListAdapter mAdapter = new MenuListAdapter(getActivity(), mList);
        mRecyclerRefresh.setAdapter(mAdapter);
        mAdapter.setCallBack(this);
        mRecyclerRefresh.setCallBack(this);

        mRecyclerRefresh.setInitPage(1);

        mPopView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_select_layout, null, false);
        TextView textView = (TextView) mPopView.findViewById(R.id.tv_select_save);
        textView.setText("取消收藏");
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuMode menuMode = mList.get(mSelPos);
                ToastUtil.showToast(menuMode.getName() + " 取消收藏成功!");
                SqLiteUtil util = SqLiteUtil.getInstance(getActivity());

                util.deleteMenu(menuMode.getMenuId());

                mList.remove(mSelPos);
                mAdapter.notifyDataSetChanged();

                if (mList.size() > 0) {
                    mEmptyView.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.VISIBLE);
                }

                mTotal--;
                mPopSelect.dismiss();
            }
        });
    }

    @Override
    public void loadData() {
        mRecyclerRefresh.startRefresh();
    }

    @Override
    public void refresh(BaseRecyclerRefresh.Status status, int curPage) {
        mStatus = status;
        mCurPage = curPage;

        if (status == BaseRecyclerRefresh.Status.REFRESHING) {
            loadMenuList();

        } else if (status == BaseRecyclerRefresh.Status.LOADMORE) {
            loadMenuList();
        }
    }

    /**
     * 加载数据库数据
     */
    private void loadMenuList() {
        String id = "-1";
        if (mStatus == BaseRecyclerRefresh.Status.LOADMORE) {
            showProgress();

            int size = mList.size();
            if (size > 0) {
                id = mList.get(size - 1).getId() + "";
            }
        }

        SqLiteUtil util = SqLiteUtil.getInstance(getActivity());
        List<MenuMode> list = util.queryPageMenu(id, mPageSize);

        if (mStatus == BaseRecyclerRefresh.Status.REFRESHING) {
            mList.clear();
            mTotal = util.queryAllMenu().size();
        }

        if (mCurPage * mPageSize >= mTotal) {
            mRecyclerRefresh.setLoadMoreEnable(false);
        } else {
            mRecyclerRefresh.setLoadMoreEnable(true);
        }

        if (mStatus == BaseRecyclerRefresh.Status.LOADMORE) {
            mRecyclerRefresh.stopLoadMore();
            dismissProgress();
        } else {
            mRecyclerRefresh.stopRefresh();
            mRecyclerRefresh.smoothScrollTo();
        }

        mList.addAll(list);
        mRecyclerRefresh.notifyItemChanged(-1);

        if (mList.size() > 0) {
            mEmptyView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onInvisible() {

    }

    @Override
    public boolean onBack() {
        return false;
    }

    /**
     * 点击事件
     *
     * @param pos 点击下标
     */
    @Override
    public void onClick(int pos) {
        Intent intent = new Intent(getActivity(), CookDetailActivity.class);
        intent.putExtra("data", mList.get(pos).getRecipe());
        intent.putExtra("img", mList.get(pos).getThumbnail());
        startActivity(intent);
    }

    @Override
    public void onLongClick(int pos) {
        mSelPos = pos;
        View parentView = mRecyclerRefresh.getRecyclerView().findViewHolderForAdapterPosition(pos).itemView;

        mPopSelect = SelectMorePop.getInstance().showSelectView(getActivity(), mPopView, parentView);
    }
}
