package com.dingqiqi.qijuproject.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dingqiqi.qijuproject.R;

/**
 * 全屏弹框
 * Created by dingqiqi on 2017/4/5.
 */
public class FullScreenDialog extends DialogFragment {

    private TextView mTvTitle;
    private TextView mTvMsg;
    private TextView mTvCancel;
    private TextView mTvOK;
    private TextView mTvTime;
    private EditText mEditText;

    private View mLineTop;
    private View mLineBottom;

    private LinearLayout mLinearBottom;
    /**
     * 变量
     */
    private String mTitle;
    private String mMsg;
    private String mCancel;
    private String mOk;
    /**
     * 倒计时时间
     */
    private int mTime;
    /**
     * 什么类型的弹框
     */
    private int mIndex;
    /**
     * 确定按钮监听
     */
    private View.OnClickListener mListener;
    //监听
    private onDialogClick mDialogClick;

    private View mView;
    //提示
    private String mHint;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.dialog_layout, container, false);

        mTvTitle = (TextView) mView.findViewById(R.id.tv_title);
        mTvMsg = (TextView) mView.findViewById(R.id.tv_content);
        mTvCancel = (TextView) mView.findViewById(R.id.tv_cancel);
        mTvOK = (TextView) mView.findViewById(R.id.tv_ok);
        mTvTime = (TextView) mView.findViewById(R.id.tv_time);
        mEditText = (EditText) mView.findViewById(R.id.et_input);

        mLineTop = mView.findViewById(R.id.line_top);
        mLineBottom = mView.findViewById(R.id.line_bottom);
        mLinearBottom = (LinearLayout) mView.findViewById(R.id.linear_bottom);

        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);

        mTitle = getArguments().getString("title");
        mMsg = getArguments().getString("msg");
        mCancel = getArguments().getString("cancel");
        mOk = getArguments().getString("ok");
        mTime = (int) (getArguments().getLong("time") / 1000);
        mIndex = getArguments().getInt("index");
        mHint = getArguments().getString("hint");

        if (mIndex == 2) {
            showTwoBtnDialog(mTitle, mMsg, mCancel, mOk, mListener);
        } else if (mIndex == 1) {
            showOneBtnDialog(mTitle, mMsg, mOk, mListener);
        } else if (mIndex == 3) {
            showOneBtnEdit(mTitle, mHint, mOk, mDialogClick);
        } else {
            showNoBtnDialog(mTitle, mMsg);
        }

        return mView;
    }

    /**
     * 设置监听
     *
     * @param listener 监听
     */
    public void setListener(View.OnClickListener listener) {
        this.mListener = listener;
    }


    public void setDialogClick(onDialogClick mDialogClick) {
        this.mDialogClick = mDialogClick;
    }

    /**
     * 显示两个按钮弹框
     *
     * @param title 标题
     * @param msg 消息
     * @param cancel 取消按钮文字
     * @param ok 确定按钮文字
     * @param listener 监听
     * @return 弹框对象
     */
    private DialogFragment showTwoBtnDialog(String title, String msg, String cancel, String ok, View.OnClickListener listener) {
        mTvTitle.setText(title);
        mTvMsg.setText(msg);

        if (!TextUtils.isEmpty(cancel)) {
            mTvCancel.setText(cancel);
        }

        if (!TextUtils.isEmpty(ok)) {
            mTvOK.setText(ok);
        }

        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (listener == null) {
            mTvOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else {
            mTvOK.setOnClickListener(listener);
        }
        return this;
    }

    /**
     * 显示一个按钮弹框
     *
     * @param title 标题
     * @param msg 消息
     * @param ok  确定按钮文字
     * @param listener 监听
     * @return 弹框对象
     */
    private DialogFragment showOneBtnDialog(String title, String msg, String ok, View.OnClickListener listener) {
        mTvTitle.setText(title);
        mTvMsg.setText(msg);

        mTvCancel.setVisibility(View.GONE);
        mLineBottom.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(ok)) {
            mTvOK.setText(ok);
        }

        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (listener == null) {
            mTvOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else {
            mTvOK.setOnClickListener(listener);
        }
        return this;
    }

    /**
     * 显示倒计时弹框
     *
     * @param title 标题
     * @param msg 消息
     * @return 弹框对象
     */
    private DialogFragment showNoBtnDialog(String title, String msg) {
        //屏蔽后退键
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            }
        });

        mTvTitle.setText(title);
        mTvMsg.setText(msg);

        mTvCancel.setVisibility(View.GONE);
        mLineTop.setVisibility(View.GONE);
        mLinearBottom.setVisibility(View.GONE);

        mTvTime.setVisibility(View.VISIBLE);
        StringBuffer mBuffer = new StringBuffer();
        mBuffer.append(mTime).append(" s");
        mTvTime.setText(mBuffer);

        mView.postDelayed(mRunnable, 1000);

        return this;
    }

    /**
     * 倒计时runnable
     */
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (mTime > 1) {
                mTime--;
                StringBuffer mBuffer = new StringBuffer();
                mBuffer.append(mTime).append(" s");
                mTvTime.setText(mBuffer);
                mView.postDelayed(mRunnable, 1000);
            } else {
                dismiss();
            }
        }
    };

    /**
     * 显示一个按钮弹框
     *
     * @param title    标题
     * @param hint     提示
     * @param ok       确定按钮文字
     * @param listener 监听
     * @return 当前弹窗
     */
    private DialogFragment showOneBtnEdit(String title, String hint, String ok, final onDialogClick listener) {
        mTvTitle.setText(title);
        mTvMsg.setVisibility(View.GONE);
        mEditText.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(hint)) {
            mEditText.setHint(hint);
        }

        mTvCancel.setVisibility(View.GONE);
        mLineBottom.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(ok)) {
            mTvOK.setText(ok);
        }

        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (listener == null) {
            mTvOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        } else {
            mTvOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    listener.onClick(mEditText.getText().toString());
                }
            });
        }
        return this;
    }

    public interface onDialogClick {
        void onClick(String text);
    }

}
