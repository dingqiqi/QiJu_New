package com.dingqiqi.qijuproject.dialog;

import android.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.activity.BaseActivity;
import com.dingqiqi.qijuproject.util.FileUtil;

import java.io.File;

public class SelectImageDialog extends DialogFragment {
    /**
     * 当前照片文件
     */
    private File mCurPhotoFile;
    /**
     * 点击回调
     */
    private onClickCallBack mListener;
    /**
     * 接收获取的图案的activity
     */
    private BaseActivity mActivity;
    /**
     * 选择图片
     */
    public static final int SELECT_IMAGE = 98;
    /**
     * 拍照
     */
    public static final int SELECT_PHOTO = 99;
    /**
     * 裁剪
     */
    public static final int SELECT_ZOOM = 100;

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

        View view = inflater.inflate(R.layout.dialog_select_image_layout, container,
                false);

        Button mBtnFile = (Button) view.findViewById(R.id.btn_select_file);
        Button mBtnPhoto = (Button) view.findViewById(R.id.btn_select_photos);
        Button mBtnDefault = (Button) view.findViewById(R.id.btn_select_default);

        mBtnPhoto.setOnClickListener(mOnClickListener);
        mBtnFile.setOnClickListener(mOnClickListener);
        mBtnDefault.setOnClickListener(mOnClickListener);

        return view;
    }

    /**
     * 设置监听
     *
     * @param activity 活动
     * @param listener 回调
     */
    public void setListener(BaseActivity activity, onClickCallBack listener) {
        mActivity = activity;
        mListener = listener;
    }

    private OnClickListener mOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_select_photos:
                    //拍照
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mCurPhotoFile = new File(FileUtil.getImagePath(getActivity()),
                            FileUtil.getPhotoFileName(getActivity()));
                    Uri mImageUri;
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                        mImageUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".FileProvide",
                                mCurPhotoFile);
                    } else {
                        mImageUri = Uri.fromFile(mCurPhotoFile);
                    }
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
                    if (mActivity != null) {
                        mActivity.startActivityForResult(intent, SELECT_PHOTO);
                    }
                    if (mListener != null) {
                        mListener.onClick(mCurPhotoFile);
                    }
                    break;
                case R.id.btn_select_file:
                    //相册
                    intent = new Intent(Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    if (mActivity != null) {
                        mActivity.startActivityForResult(intent, SELECT_IMAGE);
                    }

                    if (mListener != null) {
                        mListener.onClick(mCurPhotoFile);
                    }
                    break;
                case R.id.btn_select_default:
                    if (mListener != null) {
                        mListener.onDefault();
                    }
                default:
                    break;
            }
        }
    };

    /**
     * 裁剪图片
     *
     * @param uri 图片uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        //申请权限(android N必加,不然报错)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        // 不能超过400
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);

        intent.putExtra("return-data", true);

        if (mActivity != null) {
            mActivity.startActivityForResult(intent, SELECT_ZOOM);
        }
    }

    /**
     * 回调监听
     */
    public interface onClickCallBack {
        void onClick(File file);

        void onDefault();
    }

}
