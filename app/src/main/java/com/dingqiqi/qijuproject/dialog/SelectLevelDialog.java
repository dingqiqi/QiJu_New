package com.dingqiqi.qijuproject.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import com.dingqiqi.qijuproject.R;

public class SelectLevelDialog extends DialogFragment {
    /**
     * 点击回调
     */
    private onClickCallBack mListener;

    @SuppressWarnings("deprecation")
    @Override
    public void onStart() {
        super.onStart();
        LayoutParams params = getDialog().getWindow().getAttributes();

        params.width = getDialog().getWindow().getWindowManager()
                .getDefaultDisplay().getWidth();

        getDialog().getWindow().setAttributes(params);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.dialog_select_level_layout, container,
                false);

        Button mBtn2 = (Button) view.findViewById(R.id.btn_level_2);
        Button mBtn3 = (Button) view.findViewById(R.id.btn_level_3);
        Button mBtn4 = (Button) view.findViewById(R.id.btn_level_4);
        Button mBtn5 = (Button) view.findViewById(R.id.btn_level_5);

        mBtn2.setOnClickListener(mOnClickListener);
        mBtn3.setOnClickListener(mOnClickListener);
        mBtn4.setOnClickListener(mOnClickListener);
        mBtn5.setOnClickListener(mOnClickListener);

        return view;
    }

    /**
     * 设置监听
     *
     * @param listener 回调
     */
    public void setListener(onClickCallBack listener) {
        mListener = listener;
    }

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_level_2:
                    if (mListener != null) {
                        mListener.onClick(2);
                    }
                    break;
                case R.id.btn_level_3:
                    if (mListener != null) {
                        mListener.onClick(3);
                    }
                    break;
                case R.id.btn_level_4:
                    if (mListener != null) {
                        mListener.onClick(4);
                    }
                    break;
                case R.id.btn_level_5:
                    if (mListener != null) {
                        mListener.onClick(5);
                    }
                    break;
            }
        }
    };

    /**
     * 回调监听
     */
    public interface onClickCallBack {
        void onClick(int num);
    }

}
