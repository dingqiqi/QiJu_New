package com.dingqiqi.qijuproject.manager;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;
import com.dingqiqi.qijuproject.util.CommonUtil;

/**
 * 自定义GlideModule,自定义内存缓存和磁盘缓存
 * Created by dqq on 2016/9/29.
 */
public class GlideModuleManager implements GlideModule {

    public final static String DISK_CACHE_PATH = "/GlideCache";   // 磁盘缓存路径
    public final static int DISK_CACHE_SIZE = 30 * 1024 * 1024;// 磁盘缓存大小(当前30M)

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //获取系统分配给应用的总内存大小
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        //设置图片内存缓存占用八分之一
        int memoryCacheSize = maxMemory / 8;
        //设置内存缓存大小
        Log.d("LKLGlideModule", "memoryCacheSize = " + memoryCacheSize);
        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
        //设置磁盘路径和缓存大小
        final String diskFullPath = CommonUtil.getWritePath(DISK_CACHE_PATH);

        builder.setDiskCache(new DiskLruCacheFactory(diskFullPath, "glide", DISK_CACHE_SIZE));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        //glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(OkhttpManager.getOkHttpClient()));
    }
}
