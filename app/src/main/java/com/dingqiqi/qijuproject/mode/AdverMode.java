package com.dingqiqi.qijuproject.mode;

import java.util.List;

/**
 *历史上的今天实体类
 * Created by Administrator on 2017/5/24.
 */
public class AdverMode extends BaseMode {
    private String date;
    private int day;
    private String event;
    private String id;
    private int month;
    private String title;

    private List<AdverMode> result;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<AdverMode> getResult() {
        return result;
    }

    public void setResult(List<AdverMode> result) {
        this.result = result;
    }
}
