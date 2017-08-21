package com.dingqiqi.qijuproject.dialog;

import android.app.DialogFragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dingqiqi.qijuproject.R;

public class ShowImageDialog extends DialogFragment {

    private ImageView mIvShow;

    private Bitmap mBitmap;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.Dialog_FullScreen);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_show_image_layout, container,
                false);

        mIvShow = (ImageView) view.findViewById(R.id.iv_show);
        mIvShow.setImageBitmap(mBitmap);

        return view;
    }

    /**
     * 设置监听
     *
     * @param bitmap 图片
     */
    @SuppressWarnings("deprecation")
    public void setBackground(Bitmap bitmap) {
        mBitmap = bitmap;
        if (mIvShow != null) {
            mIvShow.setImageBitmap(mBitmap);
        }
    }
}
