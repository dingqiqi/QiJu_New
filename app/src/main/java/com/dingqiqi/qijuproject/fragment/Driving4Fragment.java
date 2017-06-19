package com.dingqiqi.qijuproject.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.dialog.DialogFactory;
import com.dingqiqi.qijuproject.dialog.LoadingDialog;
import com.dingqiqi.qijuproject.http.IDriving1Api;
import com.dingqiqi.qijuproject.http.IDriving4Api;
import com.dingqiqi.qijuproject.manager.GlideManager;
import com.dingqiqi.qijuproject.manager.RetrofitManager;
import com.dingqiqi.qijuproject.mode.DrivingMode;
import com.dingqiqi.qijuproject.util.CommonUtil;
import com.dingqiqi.qijuproject.util.SharedPreUtil;
import com.dingqiqi.qijuproject.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 科目一Fragment
 * Created by dingqiqi on 2017/6/14.
 */
public class Driving4Fragment extends BaseFragment {

    //数据
    private DrivingMode mData;
    //当前页
    private int mCurPage = 1;
    //一页请求个数
    private final int mPageSize = 20;

    private TextView mTvTitle;
    private ImageView mIvShow;
    private RadioButton mRbItemA;
    private RadioButton mRbItemB;
    private RadioButton mRbItemC;
    private RadioButton mRbItemD;
    private TextView mTvOk;
    private TextView mTvDetail;
    private TextView mTvBack;
    //    private TextView mTvNext;
    private View mLineView;
    private View mLinearView;

    private RadioGroup mRadioGroup;

    //当前题目
    private int mCurIndex = 1;
    /**
     * 正确答案
     */
    private int mCurResult;
    /**
     * 选中答案
     */
    private int mSelResult;
    /**
     * 当前题目模式(判断题or选择题)
     */
    private String mCurType;
    /**
     * 选择题值
     */
    private final String mSelectType = "select";
    /**
     * glide 加载图片manager
     */
    private GlideManager mManager;
    /**
     * 错误题库
     */
    private List<DrivingMode.Mode> mErrorList;
    /**
     * 题目数据
     */
    private DrivingMode.Mode mMode;
    /**
     * 是否第一次选中
     */
    private boolean mIsFirst;
    /**
     * 弹框
     */
    private DialogFragment mDialogFragment;
    /**
     * 快速做题
     */
    private int mDrivingSetting;


    @Override
    public View createView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_driving4_layout, container, false);
    }

    @Override
    public void initView(View view) {
        mErrorList = new ArrayList<>();

        mTvTitle = (TextView) view.findViewById(R.id.tv_title_driving4);
        mIvShow = (ImageView) view.findViewById(R.id.iv_show_driving4);
        mRbItemA = (RadioButton) view.findViewById(R.id.rb_item_a_driving4);
        mRbItemB = (RadioButton) view.findViewById(R.id.rb_item_b_driving4);
        mRbItemC = (RadioButton) view.findViewById(R.id.rb_item_c_driving4);
        mRbItemD = (RadioButton) view.findViewById(R.id.rb_item_d_driving4);
        mTvOk = (TextView) view.findViewById(R.id.tv_yes_driving4);
        mTvDetail = (TextView) view.findViewById(R.id.tv_detail_driving4);
        mTvBack = (TextView) view.findViewById(R.id.tv_back_driving4);
        TextView mTvNext = (TextView) view.findViewById(R.id.tv_next_driving4);
        TextView mTvError = (TextView) view.findViewById(R.id.tv_error_driving4);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.rg_item_driving4);
        mLineView = view.findViewById(R.id.line_view);
        mLinearView = view.findViewById(R.id.linear_bottom);

        if (mManager == null) {
            mManager = new GlideManager(getActivity());
        }

        mTvBack.setText("上一题");
        mTvNext.setText("下一题");
        mTvError.setText("错题集锦");

        mTvError.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mErrorList.size() == 0) {
                    ToastUtil.showToast("您还没有做错题目哟!");
                } else {

                }
            }
        });

        mTvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mData != null) {
                    if (mCurIndex != 1) {
                        mCurIndex--;
                        setData();
                    }
                }
            }
        });

        mTvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextShowView();
            }
        });

        //是否快速选中模式
        mDrivingSetting = SharedPreUtil.getInstance(getActivity(), SharedPreUtil.mDefaultFileName).getInt(SharedPreUtil.mSettingDrivingKey, 0);
        //是否第一次做题
        mIsFirst = SharedPreUtil.getInstance(getActivity(), SharedPreUtil.mDefaultFileName).getBoolean(SharedPreUtil.mDrivingFirstKey, true);

        mRbItemA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //可点击并选中
                if (mRbItemA.isChecked() && mRbItemA.isEnabled()) {
                    //快速开关打开
                    if (mDrivingSetting == 1) {
                        nextShowView();
                    } else if (mIsFirst) {
                        //第一次做题选答案
                        mIsFirst = false;
                        SharedPreUtil.getInstance(getActivity(), SharedPreUtil.mDefaultFileName).put(SharedPreUtil.mDrivingFirstKey, mIsFirst);
                        showSelectHintDialog();
                    }
                }
            }
        });

        mRbItemB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //可点击并选中
                if (mRbItemB.isChecked() && mRbItemB.isEnabled()) {
                    //快速开关打开
                    if (mDrivingSetting == 1) {
                        nextShowView();
                    } else if (mIsFirst) {
                        //第一次做题选答案
                        mIsFirst = false;
                        SharedPreUtil.getInstance(getActivity(), SharedPreUtil.mDefaultFileName).put(SharedPreUtil.mDrivingFirstKey, mIsFirst);
                        showSelectHintDialog();
                    }
                }
            }
        });

        mRbItemC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //可点击并选中
                if (mRbItemC.isChecked() && mRbItemC.isEnabled()) {
                    //快速开关打开
                    if (mDrivingSetting == 1) {
                        nextShowView();
                    } else if (mIsFirst) {
                        //第一次做题选答案
                        mIsFirst = false;
                        SharedPreUtil.getInstance(getActivity(), SharedPreUtil.mDefaultFileName).put(SharedPreUtil.mDrivingFirstKey, mIsFirst);
                        showSelectHintDialog();
                    }
                }
            }
        });

        mRbItemD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //可点击并选中
                if (mRbItemD.isChecked() && mRbItemD.isEnabled()) {
                    //快速开关打开
                    if (mDrivingSetting == 1) {
                        nextShowView();
                    } else if (mIsFirst) {
                        //第一次做题选答案
                        mIsFirst = false;
                        SharedPreUtil.getInstance(getActivity(), SharedPreUtil.mDefaultFileName).put(SharedPreUtil.mDrivingFirstKey, mIsFirst);
                        showSelectHintDialog();
                    }
                }
            }
        });

    }

    /**
     * 下一步要做的事情
     */
    private void nextShowView() {
        if (mData != null) {
            //获取选中答案的值
            if (isResult()) {
                //选中答案和标准答案不一致
                mMode.setSelectVal(mSelResult);
                if (mCurResult != mSelResult) {
                    if (mTvOk.getVisibility() == View.GONE) {
                        mErrorList.add(mMode);
                        showErrorView();
                        return;
                    }
                }

                if (mData.getResult() != null && mData.getResult().getList() != null && mCurIndex == mData.getResult().getList().size()) {
                    if (mCurPage * mPageSize < mData.getResult().getTotal()) {
                        mCurPage++;
                        mCurIndex++;
                        requestData(false);
                    } else {
                        ToastUtil.showToast("科目一题目已做完,好棒!");
                    }
                } else {
                    mCurIndex++;
                    setData();
                }
            } else {
                ToastUtil.showToast("请选择答案!");
            }
        }
    }

    /**
     * 先是答错界面
     */
    private void showErrorView() {
        mTvOk.setVisibility(View.VISIBLE);
        mTvDetail.setVisibility(View.VISIBLE);

        mRbItemA.setEnabled(false);
        mRbItemB.setEnabled(false);
        mRbItemC.setEnabled(false);
        mRbItemD.setEnabled(false);
    }

    /**
     * 判断结果
     *
     * @return true 有值 false  错误
     */
    private boolean isResult() {
        //选择题
        if (mSelectType.equals(mCurType)) {
            if (mRbItemA.isChecked()) {
                mSelResult = 1;
                return true;
            } else if (mRbItemB.isChecked()) {
                mSelResult = 2;
                return true;
            } else if (mRbItemC.isChecked()) {
                mSelResult = 3;
                return true;
            } else if (mRbItemD.isChecked()) {
                mSelResult = 4;
                return true;
            }
        } else {
            if (mRbItemA.isChecked()) {
                mSelResult = 1;
                return true;
            } else if (mRbItemB.isChecked()) {
                mSelResult = 2;
                return true;
            }
        }

        return false;
    }

    /**
     * 设置按钮颜色
     */
    private void setBackColor() {
        if (mCurIndex == 1) {
            mTvBack.setClickable(false);
            mTvBack.setTextColor(Color.GRAY);
        } else {
            mTvBack.setClickable(true);
            //noinspection deprecation
            mTvBack.setTextColor(getResources().getColor(R.color.colorBlack));
        }
    }

    //设置数据
    private void setData() {
        if (mCurIndex > mData.getResult().getList().size()) {
            ToastUtil.showToast("题目数据错误");
            return;
        }

        setBackColor();

        mMode = mData.getResult().getList().get(mCurIndex - 1);
        String title = mCurIndex + "、" + mMode.getTitle();
        mTvTitle.setText(title);

        if (TextUtils.isEmpty(mMode.getFile())) {
            mIvShow.setVisibility(View.GONE);
        } else {
            mIvShow.setVisibility(View.VISIBLE);
            mManager.LoadImage(mMode.getFile(), mIvShow);
        }


        if (mRadioGroup.getVisibility() == View.GONE) {
            mRadioGroup.setVisibility(View.VISIBLE);
            mLinearView.setVisibility(View.VISIBLE);
            mLineView.setVisibility(View.VISIBLE);
        }

        int selectVal = mMode.getSelectVal() - 1;
        //第一次进入题目
        if (selectVal == -1) {
            mRbItemA.setEnabled(true);
            mRbItemB.setEnabled(true);
            mRbItemC.setEnabled(true);
            mRbItemD.setEnabled(true);

            mRadioGroup.clearCheck();

            mTvOk.setVisibility(View.GONE);
            mTvDetail.setVisibility(View.GONE);
        } else {
            //已经选择过答案
            mRbItemA.setEnabled(false);
            mRbItemB.setEnabled(false);
            mRbItemC.setEnabled(false);
            mRbItemD.setEnabled(false);

            mTvOk.setVisibility(View.VISIBLE);
            mTvDetail.setVisibility(View.VISIBLE);

            if (selectVal < mRadioGroup.getChildCount()) {
                ((RadioButton) mRadioGroup.getChildAt(selectVal)).setChecked(true);
            }
        }

        String text = "A、" + mMode.getA();
        mRbItemA.setText(text);
        text = "B、" + mMode.getB();
        mRbItemB.setText(text);

        //判断题
        if (TextUtils.isEmpty(mMode.getC())) {
            mRbItemC.setVisibility(View.GONE);
            mRbItemD.setVisibility(View.GONE);
            mCurType = "unSelect";

            mRbItemC.setText(mMode.getC());
            mRbItemD.setText(mMode.getD());
        } else {
            //选择题
            text = "C、" + mMode.getC();
            mRbItemC.setText(text);
            text = "D、" + mMode.getD();
            mRbItemD.setText(text);

            mRbItemC.setVisibility(View.VISIBLE);
            mRbItemD.setVisibility(View.VISIBLE);
            mCurType = "select";
        }
        //设置题目类型
        mMode.setTikuType(mCurType);
        //设置正确答案
        mTvOk.setText(getResult(mCurType, mMode.getVal()));
        text = "正确解释:" + mMode.getExplainText();
        //设置正确答案解释
        mTvDetail.setText(text);

        try {
            //转变正确答案String 转int
            mCurResult = Integer.parseInt(mMode.getVal());
        } catch (Exception e) {
            mCurResult = -1;
        }
    }

    /**
     * 获取显示答案
     *
     * @param type 题目类型
     * @param val  正确值
     * @return 显示值
     */
    private String getResult(String type, String val) {
        if (mSelectType.equals(type)) {
            return "正确答案: " + getSelectResult(val);
        } else {
            return "正确答案: " + getYesOrNoResult(val);
        }
    }

    /**
     * 获取选择题答案
     *
     * @param val 正确值
     * @return 显示值
     */
    private String getSelectResult(String val) {
        try {
            int value = Integer.parseInt(val);
            switch (value) {
                case 1:
                    return "A";
                case 2:
                    return "B";
                case 3:
                    return "C";
                case 4:
                    return "D";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "答案有误!";
    }

    /**
     * 获取判断题答案
     *
     * @param val 正确值
     * @return 显示值
     */
    private String getYesOrNoResult(String val) {
        try {
            int value = Integer.parseInt(val);
            switch (value) {
                case 1:
                    return "正确";
                case 2:
                    return "错误";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "答案有误!";
    }

    /**
     * 加载数据
     */
    @Override
    public void loadData() {
        if (mData == null) {
//            ToastUtil.showToast("加载科目一数据");
            requestData(true);
        } else {
            setData();
        }
    }

    /**
     * 请求数据
     */
    private void requestData(final boolean isFirst) {
        IDriving4Api driving4Api = RetrofitManager.getInstance(getActivity()).getRetrofit().create(IDriving4Api.class);
        Log.i("aaa", "当前页 " + String.valueOf(mCurPage) + "页大小 " + String.valueOf(mPageSize));
        Call<DrivingMode> call = driving4Api.getDriving1Data(CommonUtil.getKey(), String.valueOf(mCurPage), String.valueOf(mPageSize));

        final LoadingDialog mDialog = DialogFactory.getInstance().showProgress(getActivity().getSupportFragmentManager());

        call.enqueue(new Callback<DrivingMode>() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onResponse(Call<DrivingMode> call, Response<DrivingMode> response) {
                mDialog.dismiss();
                if (response.body() != null && response.body().getResult() != null) {
                    if (response.body().getResult().getList() != null && response.body().getResult().getList().size() > 0) {
                        //第一次加载数据
                        if (isFirst) {
                            mData = response.body();
                        } else {
                            mData.getResult().getList().addAll(response.body().getResult().getList());
                        }

                        setData();
                    } else {
                        ToastUtil.showToast(response.body().getMsg());
                    }
                } else {
                    ToastUtil.showToast("加载数据失败!");
                }
            }

            @Override
            public void onFailure(Call<DrivingMode> call, Throwable t) {
                mDialog.dismiss();
                ToastUtil.showToast("加载数据失败!");
            }
        });
    }

    @Override
    public void onInvisible() {
    }

    /**
     * 弹出选择提示框
     */
    private void showSelectHintDialog() {
        mDialogFragment = DialogFactory.getInstance().showTwoBtnDialog(getActivity().getSupportFragmentManager(), "系统提示",
                "是否设置快捷选中?选中正确答案后直接跳转到下一题,选错选项后观看正确选项点下一题跳转!", "否", "是", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialogFragment.dismiss();
                        mDrivingSetting = 1;
                        SharedPreUtil.getInstance(getActivity(), SharedPreUtil.mDefaultFileName).put(SharedPreUtil.mSettingDrivingKey, mDrivingSetting);
                        nextShowView();
                    }
                });
    }

    /**
     * 弹出退出提示框
     */
    private void showExitDialog() {
        DialogFactory.getInstance().showTwoBtnDialog(getActivity().getSupportFragmentManager(), "系统提示",
                "您正在进行科目四驾照模拟考试练习,是否退出练习?", "", "", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().finish();
                    }
                });
    }

    @Override
    public boolean onBack() {
        //第一题且未点击下一题,可直接退出
        if (mCurIndex == 1 && mSelResult == 0) {
            return false;
        }

        showExitDialog();
        return true;
    }

}
