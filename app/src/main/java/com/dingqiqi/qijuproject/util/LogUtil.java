package com.dingqiqi.qijuproject.util;

import android.util.Log;

import com.dingqiqi.qijuproject.BuildConfig;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * Log工具，类似android.util.Log。 tag自动产生，格式:
 * customTagPrefix:className.methodName(Line:lineNumber),
 * customTagPrefix为空时只输出：className.methodName(Line:lineNumber)。
 *
 * @ClassName LogUtil
 */
public class LogUtil {
    // SD卡中的根目录
    public static final String LOG_PATH = "/logs";
    //获取时间
    private static DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    private LogUtil() {
    }

    /**
     * 初始化日志
     *
     * @param isTime 是否按照时间来生成日志文件名称
     */
    public static void init(boolean isTime) {
        String fileName = null;
        try {
            if (isTime) {
                fileName = CommonUtil.getWritePath(LOG_PATH) + "/logs_" + formatter.format(new Date(System.currentTimeMillis())) + ".txt";
            } else {
                fileName = CommonUtil.getWritePath(LOG_PATH) + "/logs_qiju_project.txt";
                File file = new File(fileName);

                if (!file.exists()) {

                    if (file.getParentFile().mkdirs()) {
                        Log.d("aaa", "创建路径成功!");
                    } else {
                        Log.d("aaa", "创建路径失败!");
                    }

                    boolean flag = file.createNewFile();
                    if (flag) {
                        Log.d("aaa", "创建文件成功!");
                    } else {
                        Log.d("aaa", "创建文件失败!");
                    }
                }
            }

            LogConfigurator logConfigurator = new LogConfigurator();
            logConfigurator.setFileName(fileName);
            logConfigurator.setRootLevel(Level.DEBUG);
            //设置输出日志文字格式
            logConfigurator.setFilePattern("%d{MM/dd/HH:mm:ss} %-5p [%c{2}] %n%m%n-------------------%n");
            //设置总文件大小
            logConfigurator.setMaxFileSize(1024 * 1024 * 5);
            //设置所有消息是否被立刻输出 默认为true,false 不输出
            logConfigurator.setImmediateFlush(true);
            //设置最大产生的文件个数
            logConfigurator.setMaxBackupSize(1);
            //设置是否启用文件附加,默认为true。false为覆盖文件(设置false不写文件，不知道为啥)
            logConfigurator.setUseFileAppender(true);
            logConfigurator.configure();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String generateTag(StackTraceElement caller) {
        String tag = "%s.%s(L:%d)"; // 占位符
        String callerClazzName = caller.getClassName(); // 获取到类名
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), caller.getLineNumber()); // 替换

        return tag;
    }

    public static void d(String content) {
        if (!BuildConfig.LOG_DEBUG) {
            return;
        }
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Logger.getLogger(tag).debug(content);
    }

    public static void d(String content, Throwable e) {
        if (!BuildConfig.LOG_DEBUG) {
            return;
        }

        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Logger.getLogger(tag).debug(content, e);
    }

    public static void e(String content) {
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Logger.getLogger(tag).error(content);
    }

    public static void e(Throwable e) {
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Logger.getLogger(tag).error(e.getMessage(), e);
    }

    public static void e(String content, Throwable e) {
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Logger.getLogger(tag).error(content, e);
    }

    public static void i(String content) {
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Logger.getLogger(tag).info(content);
    }

    public static void i(String content, Throwable e) {
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Logger.getLogger(tag).info(content, e);
    }

    public static void w(String content) {
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Logger.getLogger(tag).warn(content);
    }

    public static void w(String content, Throwable e) {
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Logger.getLogger(tag).warn(content, e);
    }

    public static void w(Throwable e) {
        StackTraceElement caller = getCallerStackTraceElement();
        String tag = generateTag(caller);

        Logger.getLogger(tag).warn(e.getMessage(), e);
    }

    private static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    private static class ReusableFormatter {

        private Formatter formatter;
        private StringBuilder builder;

        public ReusableFormatter() {
            builder = new StringBuilder();
            formatter = new Formatter(builder);
        }

        public String format(String msg, Object... args) {
            formatter.format(msg, args);
            String s = builder.toString();
            builder.setLength(0);
            return s;
        }
    }

    private static final ThreadLocal<ReusableFormatter> thread_local_formatter = new ThreadLocal<ReusableFormatter>() {
        protected ReusableFormatter initialValue() {
            return new ReusableFormatter();
        }
    };

    public static String format(String msg, Object... args) {
        ReusableFormatter formatter = thread_local_formatter.get();
        return formatter.format(msg, args);
    }

}