package com.dingqiqi.qijuproject.view;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.adapter.MenuFilterAdapter;
import com.dingqiqi.qijuproject.dialog.DialogFactory;
import com.dingqiqi.qijuproject.mode.BaseMode;
import com.dingqiqi.qijuproject.mode.MenuFilterMode;
import com.dingqiqi.qijuproject.util.DisplayUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单控件
 * Created by 丁奇奇 on 2017/7/19.
 */
public class MenuListView extends FrameLayout {
    /**
     * 选中文字
     */
    private TextView mTvSelect;
    /**
     * 查询
     */
    private TextView mTvQuery;
    /**
     * 左侧菜单布局
     */
    private LinearLayout mLinearLeftMenu;
    /**
     * 右侧菜单列表
     */
    private RecyclerView mRecyclerView;
    /**
     * 左侧菜单数据源
     */
    private List<BaseMode> mLeftList;
    /**
     * 右侧菜单数据源
     */
    private List<BaseMode> mRightList;
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 当前选中下标
     */
    private int mCurLeftIndex = 0;
    /**
     * 存储每个总菜谱类型包含子类的个数
     */
    private List<Integer> mCountList;
    /**
     * 标题高度
     */
    private int mTitleHeight;
    /**
     * 分割线高度
     */
    private int mDividingHeight;
    /**
     * 滑动状态 1手指滑动 2 滑动中 0停止
     */
    private int mScrollStatus = -1;
    /**
     * 滑动的高度
     */
    private int mScrollY;
    /**
     * 左侧菜单TextView列表
     */
    private List<TextView> mViewList;
    /**
     * 选中的下标
     */
    private int mSelectPos = -1;
    /**
     * 选中的标题和id
     */
    private String mText = "选中: ";
    private String mSelectText;
    private String mSelectId;
    /**
     * 菜单适配器
     */
    private MenuFilterAdapter mMenuAdapter;
    /**
     * 弹框
     */
    private DialogFragment mDialogFragment;

    private onQueryCallBack mCallBack;

    public MenuListView(Context context) {
        super(context);
        initView(context);
    }

    public MenuListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MenuListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 初始化
     *
     * @param context 上下文
     */
    private void initView(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.menu_list_layout, this);

        mLeftList = new ArrayList<>();
        mRightList = new ArrayList<>();
        mViewList = new ArrayList<>();
        mCountList = new ArrayList<>();

        mTvSelect = (TextView) findViewById(R.id.tv_select);
        mTvQuery = (TextView) findViewById(R.id.tv_query);
        mLinearLeftMenu = (LinearLayout) findViewById(R.id.linear_left_menu);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_menu);
    }

    public void setCallBack(onQueryCallBack mCallBack) {
        this.mCallBack = mCallBack;
    }

    /**
     * 设置菜单数据
     *
     * @param leftList 主菜单数据源 rightList 详细菜单数据源
     */
    public void setMenuData(List<? extends BaseMode> leftList, List<? extends BaseMode> rightList) {
        if (leftList == null || rightList == null) {
            throw new IllegalArgumentException("入参不能为 NULL");
        }

        mLeftList.clear();
        mLeftList.addAll(leftList);
        mRightList.clear();
        mRightList.addAll(rightList);

        mViewList.clear();

        addLeftMenu();
        addRightMenu();
        setListeners();

        mCountList.clear();

        int leftSize = leftList.size();
        int rightsize = rightList.size();

        for (int i = 0; i < leftSize; i++) {
            int count = 0;
            String parentId = ((MenuFilterMode) leftList.get(i)).getCtgId();

            for (int j = 0; j < rightsize; j++) {
                BaseMode baseMode = rightList.get(j);
                if (baseMode instanceof MenuFilterMode) {
                    MenuFilterMode menuMode = (MenuFilterMode) baseMode;

                    if (!TextUtils.isEmpty(menuMode.getParentId()) && menuMode.getParentId().equals(parentId)) {
                        count++;
                    } else {
                        //遍历时 count>0代表没有相等的了
                        if (count > 0) {
                            break;
                        }
                    }
                }
            }

            mCountList.add(count);
        }

        initTextView(0);
    }

    /**
     * 添加右边菜单
     */
    private void addRightMenu() {
        mMenuAdapter = new MenuFilterAdapter(mContext, mRightList);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mMenuAdapter);

        //标题
        RecyclerTitle recyclerTitle = new RecyclerTitle(mContext, mLeftList, mRightList);
        //获取标题高度 画线高度
        mTitleHeight = recyclerTitle.getTitleHeight();
        mDividingHeight = recyclerTitle.getDividingHeight();
        //添加标题
        mRecyclerView.addItemDecoration(recyclerTitle);

        //滑动监听
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mScrollY = mScrollY + dy;
                if (mScrollStatus == 1) {
                    int pos = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                    String parentId = ((MenuFilterMode) mRightList.get(pos)).getParentId();

                    int index = 0;
                    for (BaseMode baseMode : mLeftList) {
                        if (baseMode instanceof MenuFilterMode) {
                            MenuFilterMode menuMode = (MenuFilterMode) baseMode;
                            if (!TextUtils.isEmpty(parentId) && parentId.equals(menuMode.getCtgId())) {
                                initTextView(index);
                                break;
                            }
                        }
                        index++;
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        //手指滑动
                        mScrollStatus = 1;
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        //停止滑动
                        mScrollStatus = -1;
                        break;
                }
            }
        });
    }

    /**
     * 设置监听
     */
    private void setListeners() {
        /**
         * item点击事件
         */
        mMenuAdapter.setCallBack(new MenuFilterAdapter.onClickCallBack() {
            @Override
            public void onClick(int position, String parentId) {
//                if (mSelectPos != -1) {
//                    mMenuAdapter.notifyItemChanged(mSelectPos);
//                }
                mSelectPos = position;
                mMenuAdapter.setSelectPos(mSelectPos);

                //刷新数据源
                mMenuAdapter.notifyDataSetChanged();

                MenuFilterMode menuFilterMode = (MenuFilterMode) mRightList.get(mSelectPos);
                mSelectId = menuFilterMode.getCtgId();
                mSelectText = menuFilterMode.getName();
                mSelectText = mText + mSelectText;
                mTvSelect.setText(mSelectText);

                //自动跳转到下一个view
                //if (i < size - 1) {
                //    mViewList.get(i + 1).performClick();
                //}
            }
        });

        mTvSelect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContext instanceof FragmentActivity) {
                    mDialogFragment = DialogFactory.getInstance().showTwoBtnDialog(((FragmentActivity) mContext).getSupportFragmentManager(), "", "是否清除筛选选项!", "", "", new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mSelectPos = -1;
                            mMenuAdapter.setSelectPos(mSelectPos);

                            mSelectText = "";
                            mSelectId = "";
                            mTvSelect.setText(mText);

                            mMenuAdapter.notifyDataSetChanged();
                            mDialogFragment.dismiss();
                        }
                    });
                }
            }
        });

        mTvQuery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null) {
                    mCallBack.onQuery(mSelectId);
                }
            }
        });
    }

    /**
     * 添加左边菜单
     */
    private void addLeftMenu() {
        int indexId = 0;
        int size = mLeftList.size();
        mLinearLeftMenu.removeAllViews();
        for (BaseMode baseMode : mLeftList) {
            if (baseMode instanceof MenuFilterMode) {
                MenuFilterMode menuMode = (MenuFilterMode) baseMode;
                TextView textView = new TextView(mContext);
                textView.setId(indexId);
                textView.setClickable(true);
                textView.setText(menuMode.getName());
                textView.setTag(menuMode.getCtgId());
                textView.setTextColor(getResources().getColor(R.color.colorText));
                textView.setTextSize(16);
                textView.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(mContext, 50));
                textView.setLayoutParams(layoutParams);
                textView.setOnClickListener(mOnClickListener);

                mLinearLeftMenu.addView(textView);
                mViewList.add(textView);

                //中间加入间隔线
                if (indexId != size - 1) {
                    View lineView = new View(mContext);
                    lineView.setBackgroundColor(getResources().getColor(R.color.colorLine));
                    layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(mContext, 1));
                    lineView.setLayoutParams(layoutParams);
                    mLinearLeftMenu.addView(lineView);
                }

                indexId++;
            }
        }
    }

    /**
     * 按钮点击事件
     */
    public OnClickListener mOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = v.getId();
            if (index == mCurLeftIndex) {
                return;
            }

            int count = 0;
            int lineCount = 0;
            int flag;
            //向下滚
            if (mCurLeftIndex > index) {
                flag = -1;
                for (int i = index; i < mCurLeftIndex; i++) {
                    count = count + mCountList.get(i);
                    lineCount = lineCount + mCountList.get(i) - 1;
                }
            } else {
                flag = 1;
                //向上滚
                for (int i = mCurLeftIndex; i < index; i++) {
                    count = count + mCountList.get(i);
                    lineCount = lineCount + mCountList.get(i) - 1;
                }
            }

            if (mRecyclerView.getChildCount() > 0) {
                //单项高度
                int height = mRecyclerView.getChildAt(0).getMeasuredHeight();

                //显示的第一个position
                LinearLayoutManager manager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                //显示的第一个
                int pos = manager.findFirstVisibleItemPosition();
                //多滑动的量 = 总的滑动量 - 当前位置的高度
                pos = mScrollY - getCurPosHeight(pos);
                Log.i("aaa", "pos-- > " + pos);

                //标题+间隔线高度
                int titleHeight = mTitleHeight * Math.abs(mCurLeftIndex - index) + lineCount * mDividingHeight;
                //要滑动的总高度   =  总共要滑动的高度 -  多余滑动的高度
                int totalHeight = (height * count + titleHeight) * flag - pos;

                mRecyclerView.smoothScrollBy(0, totalHeight);
            }

            initTextView(index);
        }
    };

    /**
     * 根据当前位置的高度
     *
     * @return 当前类型的高度
     */
    private int getCurPosHeight(int pos) {
        String parentId = ((MenuFilterMode) mRightList.get(pos)).getParentId();
        int size = mLeftList.size();

        int count = 0;
        int lineCount = 0;

        for (int i = 0; i < size; i++) {
            BaseMode baseMode = mLeftList.get(i);
            if (baseMode instanceof MenuFilterMode) {
                MenuFilterMode menuMode = (MenuFilterMode) baseMode;

                if (!TextUtils.isEmpty(parentId) && parentId.equals(menuMode.getCtgId())) {
                    int height = mRecyclerView.getChildAt(0).getMeasuredHeight();
                    //总高度
                    return height * count + mTitleHeight * i + lineCount * mDividingHeight;
                } else {
                    count = count + mCountList.get(i);
                    lineCount = lineCount + mCountList.get(i) - 1;
                }
            }
        }

        return 0;
    }

    /**
     * 按钮选中初始化
     *
     * @param selectId 选中id
     */
    public void initTextView(int selectId) {
        mCurLeftIndex = selectId;

        int size = mLinearLeftMenu.getChildCount();

        for (int i = 0; i < size; i++) {
            View view = mLinearLeftMenu.getChildAt(i);

            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                if (textView.getId() == selectId) {
                    textView.setBackgroundColor(getResources().getColor(R.color.colorText));
                    textView.setTextColor(getResources().getColor(R.color.colorWhite));
                } else {
                    textView.setBackgroundColor(getResources().getColor(R.color.translate));
                    textView.setTextColor(getResources().getColor(R.color.colorText));
                }
            }
        }

    }

    public interface onQueryCallBack {
        void onQuery(String id);
    }

}
