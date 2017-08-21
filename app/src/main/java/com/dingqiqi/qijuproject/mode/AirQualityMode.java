package com.dingqiqi.qijuproject.mode;

import java.util.List;

/**
 * 空气实体类
 * Created by dingqiqi on 2017/5/25.
 */
public class AirQualityMode extends BaseMode {
    private List<AirQualityMode> result;
    private float aqi;
    private String city;
    private String district;
    private List<AirFutureMode> fetureData;
    private List<AirHourMode> hourData;
    private float no2;
    private float pm10;
    private float pm25;
    private String province;
    private String quality;
    private float so2;
    private String updateTime;

    public List<AirQualityMode> getResult() {
        return result;
    }

    public void setResult(List<AirQualityMode> result) {
        this.result = result;
    }

    public float getAqi() {
        return aqi;
    }

    public void setAqi(float aqi) {
        this.aqi = aqi;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public List<AirFutureMode> getFetureData() {
        return fetureData;
    }

    public void setFetureData(List<AirFutureMode> fetureData) {
        this.fetureData = fetureData;
    }

    public List<AirHourMode> getHourData() {
        return hourData;
    }

    public void setHourData(List<AirHourMode> hourData) {
        this.hourData = hourData;
    }

    public float getNo2() {
        return no2;
    }

    public void setNo2(float no2) {
        this.no2 = no2;
    }

    public float getPm10() {
        return pm10;
    }

    public void setPm10(float pm10) {
        this.pm10 = pm10;
    }

    public float getPm25() {
        return pm25;
    }

    public void setPm25(float pm25) {
        this.pm25 = pm25;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getQuality() {
        return quality;
    }

    public void setQuality(String quality) {
        this.quality = quality;
    }

    public float getSo2() {
        return so2;
    }

    public void setSo2(float so2) {
        this.so2 = so2;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
