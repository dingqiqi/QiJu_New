package com.dingqiqi.qijuproject.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * 图片压缩工具类
 * Created by dingqiqi on 2017/7/5.
 */
public class ImageUtil {

    public static Bitmap compressImageFromFile(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;//只读边,不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        newOpts.inJustDecodeBounds = false;
        int curWidth = newOpts.outWidth;
        int curHeight = newOpts.outHeight;
        float height = 800f;//
        float width = 480f;//
        int be = 1;
        if (curWidth > curHeight && curWidth > width) {
            be = (int) (newOpts.outWidth / width);
        } else if (curWidth < curHeight && curHeight > height) {
            be = (int) (newOpts.outHeight / height);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置采样率

        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;//该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);

        return bitmap;
    }

}
