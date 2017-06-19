package com.dingqiqi.qijuproject.mode;

/**
 * 未来几天天气实体类
 * Created by dingqiqi on 2017/5/25.
 */
public class AirFutureMode {

    private float aqi;
    private String date;
    private String quality;

    public float getAqi() {
        return aqi;
    }

    public void setAqi(float aqi) {
        this.aqi = aqi;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }
}
