package com.dingqiqi.qijuproject.mode;

import java.util.List;

/**
 * 天气实体类
 * Created by 丁奇奇 on 2017/6/7.
 */
public class WeatherMode extends BaseMode {
    //空气质量
    private String airCondition;
    //城市
    private String city;
    //感冒指数
    private String coldIndex;
    //日期
    private String date;
    //区县
    private String distrct;
    //穿衣指数
    private String dressingIndex;
    //运动指数
    private String exerciseIndex;
    //湿度
    private String humidity;
    //空气质量指数
    private String pollutionIndex;
    //省
    private String province;
    //日出时间
    private String sunrise;
    //日落时间
    private String sunset;
    //温度
    private String temperature;
    //时间
    private String time;
    //更新时间
    private String updateTime;
    //洗车指数
    private String washIndex;
    //天气
    private String weather;
    //星期
    private String week;
    //风向
    private String wind;
    //未来天气
    private List<mode> future;

    private List<WeatherMode> result;

    public List<WeatherMode> getResult() {
        return result;
    }

    public void setResult(List<WeatherMode> result) {
        this.result = result;
    }

    public String getAirCondition() {
        return airCondition;
    }

    public void setAirCondition(String airCondition) {
        this.airCondition = airCondition;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getColdIndex() {
        return coldIndex;
    }

    public void setColdIndex(String coldIndex) {
        this.coldIndex = coldIndex;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDistrct() {
        return distrct;
    }

    public void setDistrct(String distrct) {
        this.distrct = distrct;
    }

    public String getDressingIndex() {
        return dressingIndex;
    }

    public void setDressingIndex(String dressingIndex) {
        this.dressingIndex = dressingIndex;
    }

    public String getExerciseIndex() {
        return exerciseIndex;
    }

    public void setExerciseIndex(String exerciseIndex) {
        this.exerciseIndex = exerciseIndex;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPollutionIndex() {
        return pollutionIndex;
    }

    public void setPollutionIndex(String pollutionIndex) {
        this.pollutionIndex = pollutionIndex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getWashIndex() {
        return washIndex;
    }

    public void setWashIndex(String washIndex) {
        this.washIndex = washIndex;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    public List<mode> getFuture() {
        return future;
    }

    public void setFuture(List<mode> future) {
        this.future = future;
    }

    public class mode {
        //日期
        private String date;
        //白天天气
        private String dayTime;
        //晚上天气
        private String night;
        //温度
        private String temperature;
        //星期
        private String week;
        //风向
        private String wind;
        //污染等级
        private int level;
        //最大温度
        private int maxTempera;
        //最小温度
        private int minTempera;

        public int getMaxTempera() {
            return maxTempera;
        }

        public void setMaxTempera(int maxTempera) {
            this.maxTempera = maxTempera;
        }

        public int getMinTempera() {
            return minTempera;
        }

        public void setMinTempera(int minTempera) {
            this.minTempera = minTempera;
        }

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDayTime() {
            return dayTime;
        }

        public void setDayTime(String dayTime) {
            this.dayTime = dayTime;
        }

        public String getNight() {
            return night;
        }

        public void setNight(String night) {
            this.night = night;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }

        public String getWind() {
            return wind;
        }

        public void setWind(String wind) {
            this.wind = wind;
        }
    }
}
