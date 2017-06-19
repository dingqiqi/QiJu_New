package com.dingqiqi.qijuproject.util;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by dingqiqi on 2017/6/7.
 */
public class TimeUtil {

    private static final SimpleDateFormat mFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

    /**
     * 将最近时间转换为(今天明天)
     *
     * @param time 时间
     * @return 返回别称
     */
    public static String getCurrentTimeName(String time) {
        if (TextUtils.isEmpty(time)) {
            return "";
        }

        time = time.replace("-", "").replace("/", "").replace(" ", "");

        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
        if (mFormat.format(calendar.getTime()).equals(time)) {
            return "昨天";
        }

        calendar = Calendar.getInstance();
        calendar.setTime(date);
        if (mFormat.format(calendar.getTime()).equals(time)) {
            return "今天";
        }

        calendar.setTime(date);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);
        if (mFormat.format(calendar.getTime()).equals(time)) {
            return "明天";
        }

        return "";
    }

    public static String getTimeMonthDay(String time) {
        time = time.replace("-", "").replace("/", "").replace(" ", "");

        try {
            Date date = mFormat.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return (calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日";
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 根据日期获取周几
     *
     * @return
     */
    public static int dayForWeek(int year, int month, int day) {
        Calendar cal = new GregorianCalendar();
        cal.set(year, month, day);
        return cal.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 根据日期获得星期
     *
     * @param time
     * @return
     */
    public static String dayForWeek(String time) {
        time = time.replace("-", "").replace("/", "").replace(" ", "");
        try {
            Date date = mFormat.parse(time);
            return dayForWeek(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 根据日期获得星期
     *
     * @param date
     * @return
     */
    public static String dayForWeek(Date date) {
        String[] weekDaysName = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        String[] weekDaysCode = {"0", "1", "2", "3", "4", "5", "6"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }

}
