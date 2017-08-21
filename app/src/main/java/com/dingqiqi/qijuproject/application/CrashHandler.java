package com.dingqiqi.qijuproject.application;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;

import com.dingqiqi.qijuproject.util.CommonUtil;
import com.dingqiqi.qijuproject.util.LogUtil;
import com.dingqiqi.qijuproject.util.ToastUtil;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 应用异常崩溃处理器</br>
 * 1)重置应用异常处理器;
 * 2)捕获应用异常堆栈信息并存储;
 * 3)重启应用
 *
 * @author
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    /**
     * 日志存储路径
     */
    public static String PATH = "/logs/exception";

    private static CrashHandler mInstance = new CrashHandler();

    private Context mContext;
    /**
     * 默认异常接收器
     */
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    public static CrashHandler getInstance() {
        return mInstance;
    }

    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler(mInstance);

    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        LogUtil.e("thread = " + thread.getName());

        boolean handle = handleException(thread, ex);
        if (mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
            return;
        }
    }

    /**
     * 异常处理，自定义异常处理实现。
     *
     * @param thread 抛出异常的线程
     * @param ex     异常栈
     * @return 是否处理
     */
    private boolean handleException(Thread thread, Throwable ex) {
        if (ex == null) {
            return false;
        }

        Map<String, String> pkgInfo = collectApplicationPacketInfo(mContext, thread);
        String crashInfo = makeCrashInfo(pkgInfo, ex);

        String fileName = makeFileName();
        saveCrashInfo(fileName, crashInfo);
        refreshCrashDir(fileName);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                StringBuilder info = new StringBuilder();
                info.append("--------------------------------\n")
                        .append("应用异常, 请重启应用!");
                Context context = CustomApplication.getInstance().getTopActivity();

                if (context == null) {
                    CustomApplication.getInstance().exitApplication(1);
                    return;
                }
                ToastUtil.showToast("应用异常，请重启应用");

                showDialog(context, info.toString(), new OnConfirmListener() {
                    @Override
                    public void onConfirm() {
                        CustomApplication.getInstance().exitApplication(1);
                    }
                });

                Looper.loop();
            }
        }).start();

        return true;
    }

    /**
     * 刷新外部存储目录
     *
     * @param fileName 待刷新的文件名
     */
    private void refreshCrashDir(String fileName) {
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(CommonUtil.getWritePath(PATH) + File.separator + fileName);
        intent.setData(Uri.fromFile(file));
        mContext.sendBroadcast(intent);
    }

    /**
     * 生成异常日志文件名</br>
     * 生成规则：crash-yyyyMMddHHmmss.log
     *
     * @return 异常日志文件名
     */
    @SuppressLint("SimpleDateFormat")
    private static String makeFileName() {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String time = formatter.format(new Date());
        return "log_" + time + ".txt";
    }

    /**
     * 存储异常日志数据到外部存储的文件中
     *
     * @param fileName  异常日志文件名
     * @param crashInfo 异常日志数据
     * @return
     */
    private boolean saveCrashInfo(String fileName, String crashInfo) {
        String state = Environment.getExternalStorageState();
        if (!state.equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }

        String dirName = CommonUtil.getWritePath(PATH);
        File crashDir = new File(dirName);
        if (!crashDir.exists()) {
            crashDir.mkdirs();
        }

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        try {
            fos = new FileOutputStream(dirName + File.separator + fileName);
            bos = new BufferedOutputStream(fos);
            bos.write(crashInfo.getBytes("GBK"));
            bos.flush();
            fos.getFD().sync();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeQuietly(bos);
            closeQuietly(fos);
        }
        return false;
    }

    /**
     * 收集应用程序包和平台相关的信息
     *
     * @param context 应用上下文对象
     * @param thread  抛出异常的线程
     * @return 包和平台相关信息
     */
    private Map<String, String> collectApplicationPacketInfo(Context context, Thread thread) {
        Map<String, String> pkgInfo = new HashMap<String, String>();
        pkgInfo.put("Thread-ID", Long.toString(thread.getId()));
        pkgInfo.put("Thread-Name", thread.getName());
        pkgInfo.put("Thread-Priority", Integer.toString(thread.getPriority()));

        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                pkgInfo.put("versionName", versionName);
                pkgInfo.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                pkgInfo.put(field.getName(), field.get(null).toString());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return pkgInfo;
    }

    /**
     * 构造异常日志信息
     *
     * @param pkgInfo 应用包及平台相关信息
     * @param ex      异常栈
     * @return 异常日志信息
     */
    private String makeCrashInfo(Map<String, String> pkgInfo, Throwable ex) {
        StringBuffer info = new StringBuffer();
        for (Map.Entry<String, String> entry : pkgInfo.entrySet()) {
            info.append(entry.getKey())
                    .append(" = ")
                    .append(entry.getValue())
                    .append("\n");
        }
        info.append("--------------------------------------------------------------------------------\n");
        Writer stack = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stack);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        info.append(stack.toString());

        return info.toString();
    }

    /**
     * 关闭输出流对象
     *
     * @param os 输出流对象
     */
    private static void closeQuietly(Closeable os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void showDialog(Context context, String info, final OnConfirmListener listener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("系统提示");
        builder.setMessage(info);

        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LogUtil.e("setNegativeButton onClick dialog.dismiss");
                listener.onConfirm();
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        LogUtil.e("showDialog = " + dialog);
    }

    /**
     * 确认结果监听器
     */
    public interface OnConfirmListener {
        void onConfirm();
    }
}
