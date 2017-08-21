package com.dingqiqi.qijuproject.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.activity.CookDetailActivity;
import com.dingqiqi.qijuproject.activity.itemActivity.MenuListActivity;
import com.dingqiqi.qijuproject.adapter.MenuListAdapter;
import com.dingqiqi.qijuproject.db.SqLiteUtil;
import com.dingqiqi.qijuproject.http.IMenuFilterApi;
import com.dingqiqi.qijuproject.http.IMenuListApi;
import com.dingqiqi.qijuproject.manager.RetrofitManager;
import com.dingqiqi.qijuproject.mode.MenuFilterMode;
import com.dingqiqi.qijuproject.mode.MenuMode;
import com.dingqiqi.qijuproject.popWindow.SelectMorePop;
import com.dingqiqi.qijuproject.util.CommonUtil;
import com.dingqiqi.qijuproject.util.LogUtil;
import com.dingqiqi.qijuproject.util.SharedPreUtil;
import com.dingqiqi.qijuproject.util.TimeUtil;
import com.dingqiqi.qijuproject.util.ToastUtil;
import com.dingqiqi.qijuproject.view.BaseRecyclerRefresh;
import com.dingqiqi.qijuproject.view.MenuListView;
import com.dingqiqi.qijuproject.view.RecyLine;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 菜谱大全
 * Created by 丁奇奇 on 2017/7/20.
 */
public class MenuFragment extends BaseFragment implements BaseRecyclerRefresh.BaseCallBack, MenuListAdapter.onClickCallBack {
    /**
     * 菜谱列表
     */
    private BaseRecyclerRefresh mRecyclerRefresh;
    /**
     * 菜单控件
     */
    private MenuListView mMenuListView;
    /**
     * 菜单列表
     */
    private List<MenuFilterMode> mLeftList = new ArrayList<>();
    private List<MenuFilterMode> mRightList = new ArrayList<>();
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
    private int mPageSize = 20;
    /**
     * 总共条数
     */
    private int mTotal;
    /**
     * 当前搜索的id
     */
    private String mCurId;
    /**
     * 当前类型
     */
    private BaseRecyclerRefresh.Status mStatus = BaseRecyclerRefresh.Status.REFRESHING;

    private MenuListActivity mMenuActivity;
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

    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_menu_layout, container, false);
    }

    @Override
    public void initView(View view) {
        mRecyclerRefresh = (BaseRecyclerRefresh) view.findViewById(R.id.recycler_refresh);
        mMenuListView = (MenuListView) view.findViewById(R.id.menuListView);

        mMenuListView.setCallBack(new MenuListView.onQueryCallBack() {
            @Override
            public void onQuery(String id) {
                //搜索全部
                if (TextUtils.isEmpty(id)) {
                    return;
                } else {
                    mCurId = id;
                }
                mStatus = BaseRecyclerRefresh.Status.REFRESHING;
                loadMenuList(true);
            }
        });

        RecyclerView recyclerView = mRecyclerRefresh.getRecyclerView();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        RecyLine mLine = new RecyLine(getActivity());
        recyclerView.addItemDecoration(mLine);

        MenuListAdapter mAdapter = new MenuListAdapter(getActivity(), mList);
        mRecyclerRefresh.setAdapter(mAdapter);
        mAdapter.setCallBack(this);
        mRecyclerRefresh.setCallBack(this);
        mRecyclerRefresh.setInitPage(1);

        FragmentActivity activity = getActivity();
        if (activity instanceof MenuListActivity) {
            mMenuActivity = (MenuListActivity) activity;
        }


        mPopView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_select_layout, null, false);

        mPopView.findViewById(R.id.tv_select_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuMode menuMode = mList.get(mSelPos);
                ToastUtil.showToast(menuMode.getName() + " 收藏成功!");
                SqLiteUtil util = SqLiteUtil.getInstance(getActivity());

                if (util.queryMenuColumn(menuMode.getMenuId()) == null) {
                    util.addMenuColumn(menuMode);
                }

                mPopSelect.dismiss();
            }
        });
    }

    @Override
    public void refresh(BaseRecyclerRefresh.Status status, int curPage) {
        mStatus = status;
        mCurPage = curPage;

        if (status == BaseRecyclerRefresh.Status.REFRESHING) {
            loadMenuList(false);

        } else if (status == BaseRecyclerRefresh.Status.LOADMORE) {
            loadMenuList(false);
        }
    }

    private void loadMenuList(final boolean isShowProgress) {
        if (isShowProgress) {
            showProgress();
        } else {
            if (mStatus == BaseRecyclerRefresh.Status.LOADMORE) {
                showProgress();
            }
        }

        IMenuListApi iMenuListApi = RetrofitManager.getInstance(getActivity()).getRetrofit().create(IMenuListApi.class);
        Call<MenuMode> modeCall = iMenuListApi.getMenuListData(CommonUtil.getKey(), mCurId, mCurPage, mPageSize);

        modeCall.enqueue(new Callback<MenuMode>() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onResponse(Call<MenuMode> call, Response<MenuMode> response) {
                if (isShowProgress || mStatus == BaseRecyclerRefresh.Status.LOADMORE) {
                    dismissProgress();
                }

                if (response.body() != null && response.body().getResult() != null) {

                    mTotal = response.body().getResult().getTotal();

                    if (mCurPage * mPageSize >= mTotal) {
                        mRecyclerRefresh.setLoadMoreEnable(false);
                    } else {
                        mRecyclerRefresh.setLoadMoreEnable(true);
                    }

                    if (mStatus == BaseRecyclerRefresh.Status.REFRESHING) {
                        mList.clear();
                    }

                    mList.addAll(response.body().getResult().getList());
                    mRecyclerRefresh.notifyItemChanged(-1);

                    if (mRecyclerRefresh.getVisibility() == View.GONE) {
                        if (mMenuActivity != null) {
                            mMenuActivity.setVisible(View.GONE);
                        }
                        mRecyclerRefresh.setVisibility(View.VISIBLE);
                        mMenuListView.setVisibility(View.GONE);
                    }

                    if (mStatus == BaseRecyclerRefresh.Status.LOADMORE) {
                        mRecyclerRefresh.stopLoadMore();
                    } else {
                        mRecyclerRefresh.stopRefresh();
                        mRecyclerRefresh.smoothScrollTo();
                    }
                } else {
                    if (mStatus == BaseRecyclerRefresh.Status.LOADMORE) {
                        mRecyclerRefresh.stopErrorLoad();
                    } else {
                        mRecyclerRefresh.stopRefresh();
                    }
                    String text = response.body() != null ? response.body().getMsg() : "获取数据失败!";
                    ToastUtil.showToast(text);
                }

            }

            @Override
            public void onFailure(Call<MenuMode> call, Throwable t) {
                if (isShowProgress || mStatus == BaseRecyclerRefresh.Status.LOADMORE) {
                    dismissProgress();
                }

                if (mStatus == BaseRecyclerRefresh.Status.LOADMORE) {
                    mRecyclerRefresh.stopErrorLoad();

                } else {
                    mRecyclerRefresh.stopRefresh();
                }
                ToastUtil.showToast(t.getMessage());
                LogUtil.d(t.getMessage());
            }
        });

    }

    @Override
    public void loadData() {
        if (mLeftList.size() == 0 && mRightList.size() == 0) {
            SharedPreUtil util = SharedPreUtil.getInstance(getActivity(), SharedPreUtil.mMenuFileName);
            String time = util.getString(SharedPreUtil.mMenuSaveTimeKey, "");

            //同一天只请求一次数据
            if (TimeUtil.getCompareCurTime(time) == 0) {
                String menuJson = util.getString(SharedPreUtil.mMenuKey, "");

                if (!TextUtils.isEmpty(menuJson)) {
                    MenuFilterMode menuMode = new Gson().fromJson(menuJson, MenuFilterMode.class);
                    setMenuData(menuMode);
                    return;
                }
            }
            requestMenuList();
        }
    }

    /**
     * 请求菜单数据
     */
    @SuppressWarnings("ConstantConditions")
    private void requestMenuList() {
        showProgress();
        IMenuFilterApi iMenuListApi = RetrofitManager.getInstance(getActivity()).getRetrofit().create(IMenuFilterApi.class);
        Call<MenuFilterMode> call = iMenuListApi.getMenuListData(CommonUtil.getKey());

        call.enqueue(new Callback<MenuFilterMode>() {
            @Override
            public void onResponse(Call<MenuFilterMode> call, Response<MenuFilterMode> response) {
                dismissProgress();
                if (response.body() != null && response.body().getResult() != null && response.body().getResult().getChilds() != null) {
                    String textJson = new Gson().toJson(response.body().getResult());
                    SharedPreUtil util = SharedPreUtil.getInstance(getActivity(), SharedPreUtil.mMenuFileName);
                    util.put(SharedPreUtil.mMenuKey, textJson);
                    util.put(SharedPreUtil.mMenuSaveTimeKey, TimeUtil.getCurTime());

                    setMenuData(response.body().getResult());
                    response = null;
                } else {
                    if (response.body() != null) {
                        LogUtil.d(response.body().getMsg());
                        Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "系统异常", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MenuFilterMode> call, Throwable t) {
                dismissProgress();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();
                LogUtil.d(t.getMessage());
            }
        });
    }

    /**
     * 设置菜单数据
     *
     * @param menuMode
     */
    private void setMenuData(MenuFilterMode menuMode) {
        MenuFilterMode allMenu = menuMode.getCategoryInfo();
        //allMenu.getName();
        //allMenu.getCtgId();

        mLeftList.clear();
        mRightList.clear();

        int size = menuMode.getChilds().size();

        for (int i = 0; i < size; i++) {
            MenuFilterMode leftMenu = menuMode.getChilds().get(i).getCategoryInfo();
            mLeftList.add(leftMenu);

            for (MenuFilterMode rightMenu : menuMode.getChilds().get(i).getChilds()) {
                mRightList.add(rightMenu.getCategoryInfo());
            }
        }

        mMenuListView.setMenuData(mLeftList, mRightList);
        allMenu = null;
    }

    @Override
    public void onInvisible() {

    }

    /**
     * 后退
     *
     * @return true 拦截后退
     */
    @Override
    public boolean onBack() {
        if (mRecyclerRefresh.getVisibility() == View.VISIBLE) {
            if (mMenuActivity != null) {
                mMenuActivity.setVisible(View.VISIBLE);
            }
            mMenuListView.setVisibility(View.VISIBLE);
            mRecyclerRefresh.setVisibility(View.GONE);
            mCurPage = 1;
            mRecyclerRefresh.setCurrentPage(mCurPage);
            return true;
        }

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLeftList.clear();
        mRightList.clear();
        mMenuListView.removeAllViews();
        mRecyclerRefresh.removeAllViews();
    }
}
