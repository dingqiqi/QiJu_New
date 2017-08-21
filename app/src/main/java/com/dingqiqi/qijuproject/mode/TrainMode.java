package com.dingqiqi.qijuproject.mode;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * 火车时刻表 mode
 * Created by 丁奇奇 on 2017/6/19.
 */
public class TrainMode extends BaseMode implements Parcelable {

    private List<TrainMode> result;
    //到达时间
    private String arriveTime;
    //重点站名
    private String endStationName;
    //始发站名
    private String startStationName;
    // 施发时间
    private String startTime;
    //站点名
    private String stationName;
    //站点序号
    private String stationNo;
    //车次名称
    private String stationTrainCode;
    //停留时间
    private String stopoverTime;
    //车次类型
    private String trainClassName;

    public TrainMode() {
    }

    protected TrainMode(Parcel in) {
        result = in.createTypedArrayList(TrainMode.CREATOR);
        arriveTime = in.readString();
        endStationName = in.readString();
        startStationName = in.readString();
        startTime = in.readString();
        stationName = in.readString();
        stationNo = in.readString();
        stationTrainCode = in.readString();
        stopoverTime = in.readString();
        trainClassName = in.readString();
    }

    public static final Creator<TrainMode> CREATOR = new Creator<TrainMode>() {
        @Override
        public TrainMode createFromParcel(Parcel in) {
            return new TrainMode(in);
        }

        @Override
        public TrainMode[] newArray(int size) {
            return new TrainMode[size];
        }
    };

    public List<TrainMode> getResult() {
        return result;
    }

    public void setResult(List<TrainMode> result) {
        this.result = result;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public void setArriveTime(String arriveTime) {
        this.arriveTime = arriveTime;
    }

    public String getEndStationName() {
        return endStationName;
    }

    public void setEndStationName(String endStationName) {
        this.endStationName = endStationName;
    }

    public String getStartStationName() {
        return startStationName;
    }

    public void setStartStationName(String startStationName) {
        this.startStationName = startStationName;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getStationNo() {
        return stationNo;
    }

    public void setStationNo(String stationNo) {
        this.stationNo = stationNo;
    }

    public String getStationTrainCode() {
        return stationTrainCode;
    }

    public void setStationTrainCode(String stationTrainCode) {
        this.stationTrainCode = stationTrainCode;
    }

    public String getStopoverTime() {
        return stopoverTime;
    }

    public void setStopoverTime(String stopoverTime) {
        this.stopoverTime = stopoverTime;
    }

    public String getTrainClassName() {
        return trainClassName;
    }

    public void setTrainClassName(String trainClassName) {
        this.trainClassName = trainClassName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(result);
        dest.writeString(arriveTime);
        dest.writeString(endStationName);
        dest.writeString(startStationName);
        dest.writeString(startTime);
        dest.writeString(stationName);
        dest.writeString(stationNo);
        dest.writeString(stationTrainCode);
        dest.writeString(stopoverTime);
        dest.writeString(trainClassName);
    }
}
