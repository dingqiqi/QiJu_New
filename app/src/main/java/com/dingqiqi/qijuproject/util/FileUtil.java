package com.dingqiqi.qijuproject.util;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 文件帮助类
 * Created by Administrator on 2017/7/5.
 */
public class FileUtil {
    /**
     * 图片存储路径
     */
    public static final String IMAGE_PATH = "image";

    /**
     * 保存地址
     *
     * @return
     */
    public static String getDefaultPath(Context context, String name) {
        File dir;
        if (Environment.isExternalStorageEmulated()) {
            dir = new File(Environment.getExternalStorageDirectory() + "/dqq/" + context.getPackageName() + "/" + name);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        } else {
            dir = new File(context.getCacheDir().getAbsolutePath() + "/dqq/" + context.getPackageName() + "/" + name);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }

        return dir.getAbsolutePath();
    }

    /**
     * 保存地址
     *
     * @return
     */
    public static String getImagePath(Context context) {

        return getDefaultPath(context, IMAGE_PATH);
    }

    /**
     * 设置拍照获得的照片名字
     *
     * @return
     */
    public static String getPhotoFileName(Context context) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyyMMdd_HHmmss", Locale.getDefault());

        return context.getPackageName() + "_IMG_" + dateFormat.format(date) + ".jpg";
    }

    /**
     * 获取bitmap
     *
     * @param context
     * @param intent
     * @return
     */
    public static Bitmap getImageBitmap(Context context, Intent intent) {
        Bitmap bitmap = null;
        if (intent.getExtras() != null) {
            bitmap = (Bitmap) intent.getExtras().get("data");
        } else {
            Uri uri = intent.getData();
            try {
                bitmap = BitmapFactory.decodeStream(context
                        .getContentResolver().openInputStream(uri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
        return bitmap;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context
     *            The context.
     * @param uri
     *            The Uri to query.
     * @param selection
     *            (Optional) Filter used in the query.
     * @param selectionArgs
     *            (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    /**
     * @param uri
     *            The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri
                .getAuthority());
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getImagePath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * 是否是图片
     *
     * @param path
     * @return true 是 false 否
     */
    public static boolean isImage(String path) {
        if (path.endsWith(".jpg") || path.endsWith(".png")
                || path.endsWith(".jpeg") || path.endsWith(".gif")
                || path.endsWith(".JPG") || path.endsWith(".PNG")
                || path.endsWith(".JPEG") || path.endsWith(".GIF")) {
            return true;
        } else {
            return false;
        }
    }

}
