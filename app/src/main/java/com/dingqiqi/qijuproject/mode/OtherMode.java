package com.dingqiqi.qijuproject.mode;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 驾考其他类
 * Created by dingqiqi on 2017/6/16.
 */
public class OtherMode implements Parcelable {
    private String cid;
    private String title;

    public OtherMode(String cid, String title) {
        this.cid = cid;
        this.title = title;
    }

    protected OtherMode(Parcel in) {
        cid = in.readString();
        title = in.readString();
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cid);
        dest.writeString(title);
    }

    public static final Creator<OtherMode> CREATOR = new Creator<OtherMode>() {
        @Override
        public OtherMode createFromParcel(Parcel in) {
            return new OtherMode(in);
        }

        @Override
        public OtherMode[] newArray(int size) {
            return new OtherMode[size];
        }
    };
}
