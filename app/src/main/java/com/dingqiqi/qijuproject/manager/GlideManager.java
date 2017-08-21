package com.dingqiqi.qijuproject.manager;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dingqiqi.qijuproject.R;

/**
 * Glide图片缓存管理类
 * Created by admin on 2016/9/29.
 */
public class GlideManager {

    private Context mContext;
    private RequestManager mRequestManager;

    public GlideManager(Context context) {
        mContext = context;
        init();
    }

    private void init() {
        mRequestManager = Glide.with(mContext);
    }

    /**
     * 从指定url加载图片设置到ImageView
     *
     * @param url
     * @param view
     */
    public void LoadImage(String url, ImageView view) {
        if (mRequestManager != null) {
            if (TextUtils.isEmpty(url)) {
                return;
            }

            if (url.endsWith(".gif")) {
                mRequestManager.load(url)
                        .asGif()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .placeholder(R.mipmap.default_bg)//默认加载的图片
                        .error(R.mipmap.default_bg)  //加载错误时显示<可指定图片>
                        .dontAnimate()
                        //.crossFade()
                        .into(view);
            } else {
                mRequestManager.load(url)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .placeholder(R.mipmap.default_bg)//默认加载的图片
                        .error(R.mipmap.default_bg)  //加载错误时显示<可指定图片>
                        .dontAnimate()
                        //.crossFade()
                        .into(view);
            }
        }
    }

    /**
     * 加载指定资源id的图片设置到ImageView
     *
     * @param resourceId
     * @param view
     */
    public void LoadImage(Integer resourceId, ImageView view) {
        if (mRequestManager != null) {
            mRequestManager.load(resourceId)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .placeholder(R.mipmap.default_bg)//默认加载的图片
                    .error(R.mipmap.default_bg)  //加载错误时显示<可指定图片>
                    .dontAnimate()
                    //.crossFade()
                    .into(view);
        }
    }

    /**
     * 加载指定资源id的图片设置到ImageView
     *
     * @param url          当前要设置的图片的url地址
     * @param view
     * @param defaultResId 默认显示图片
     * @param failResId    加载错误显示图片
     */
    public void LoadImage(String url, ImageView view, Integer defaultResId, Integer failResId) {
        if (mRequestManager != null) {
            if (TextUtils.isEmpty(url)) {
                return;
            }

            if (url.endsWith(".gif")) {
                mRequestManager.load(url)
                        .asGif()
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .placeholder(defaultResId)//默认加载的图片
                        .error(failResId)         //加载错误时显示<可指定图片>
                        .dontAnimate()
                        //.crossFade()
                        .into(view);
            } else {
                mRequestManager.load(url)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .placeholder(defaultResId)//默认加载的图片
                        .error(failResId)         //加载错误时显示<可指定图片>
                        .dontAnimate()
                        //.crossFade()
                        .into(view);
            }
        }
    }
}
