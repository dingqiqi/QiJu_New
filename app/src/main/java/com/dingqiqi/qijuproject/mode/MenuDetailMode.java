package com.dingqiqi.qijuproject.mode;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 菜谱详情
 * Created by 丁奇奇 on 2017/7/25.
 */
public class MenuDetailMode implements Parcelable {
    //图片
    private String img;
    //原料
    private String ingredients;
    //总体步奏
    private String method;
    //说明
    private String sumary;
    //标题
    private String title;
    //具体步奏
    private String step;

    public MenuDetailMode() {
    }

    protected MenuDetailMode(Parcel in) {
        img = in.readString();
        ingredients = in.readString();
        method = in.readString();
        sumary = in.readString();
        title = in.readString();
        step = in.readString();
    }

    public static final Creator<MenuDetailMode> CREATOR = new Creator<MenuDetailMode>() {
        @Override
        public MenuDetailMode createFromParcel(Parcel in) {
            return new MenuDetailMode(in);
        }

        @Override
        public MenuDetailMode[] newArray(int size) {
            return new MenuDetailMode[size];
        }
    };

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getSumary() {
        return sumary;
    }

    public void setSumary(String sumary) {
        this.sumary = sumary;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(img);
        dest.writeString(ingredients);
        dest.writeString(method);
        dest.writeString(sumary);
        dest.writeString(title);
        dest.writeString(step);
    }
}
