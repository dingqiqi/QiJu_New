package com.dingqiqi.qijuproject.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.mode.WeatherMode;
import com.dingqiqi.qijuproject.util.CommonUtil;
import com.dingqiqi.qijuproject.util.TimeUtil;
import com.dingqiqi.qijuproject.view.WeatherLineView;

import java.util.List;

/**
 * Created by dingqiqi on 2017/6/8.
 */
public class WeatherMoreAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<WeatherMode.mode> mList;
    //最大最小值
    private int mMaxValue, mMinValue;

    public WeatherMoreAdapter(Context mContext, List<WeatherMode.mode> mList) {
        this.mContext = mContext;
        this.mList = mList;
        setLevel();
    }

    public void setLevel() {
        int length = mList.size();
        for (int i = 0; i < length; i++) {
            int level = (int) (Math.random() * 5);
            mList.get(i).setLevel(level);

            String temperature = mList.get(i).getTemperature();
            int maxValue = 1, minValue = 0;

            if (temperature.contains("/")) {
                String[] strArray = temperature.split("/");
                try {
                    maxValue = Integer.parseInt(strArray[0].replace("°C", "").trim());
                    minValue = Integer.parseInt(strArray[1].replace("°C", "").trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (!TextUtils.isEmpty(temperature)) {
                    minValue = maxValue = Integer.parseInt(temperature.replace("°C", "").trim());
                }
            }
            //第一个先赋值
            if (i == 0) {
                mMaxValue = maxValue;
                mMinValue = minValue;
            }

            if (mMaxValue < maxValue) {
                mMaxValue = maxValue;
            }

            if (minValue < mMinValue) {
                mMinValue = minValue;
            }

            mList.get(i).setMaxTempera(maxValue);
            mList.get(i).setMinTempera(minValue);
        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_weather_more_layout, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder instanceof Holder) {
            Holder mHolder = (Holder) holder;
            String time = TimeUtil.getCurrentTimeName(mList.get(position).getDate());

            if (TextUtils.isEmpty(time)) {
                time = TimeUtil.dayForWeek(mList.get(position).getDate());
            }
            if ("今天".equals(time)) {
                mHolder.mLinearBg.setBackgroundColor(mContext.getResources().getColor(R.color.select_color));
                mHolder.mTvTime.setTextColor(Color.parseColor("#5BB8E8"));
            } else {
                mHolder.mLinearBg.setBackgroundColor(mContext.getResources().getColor(R.color.colorWhite));
                mHolder.mTvTime.setTextColor(mContext.getResources().getColor(R.color.colorBlackGray));
            }
            mHolder.mTvTime.setText(time);
            mHolder.mTvDate.setText(TimeUtil.getTimeMonthDay(mList.get(position).getDate()));

            mHolder.mTvStartName.setText(mList.get(position).getDayTime());
            mHolder.mTvEndName.setText(mList.get(position).getNight());

            String feng = mList.get(position).getWind();
            if (TextUtils.isEmpty(feng)) {
                mHolder.mTvFeng.setText("无风");
                mHolder.mTvFengValue.setText("0级");
            } else {
                //截取什么风,风等级
                if (feng.contains("风")) {
                    mHolder.mTvFeng.setText(feng.substring(0, feng.indexOf("风") + 1));
                    mHolder.mTvFengValue.setText(feng.substring(feng.length() - 2));
                } else {
                    mHolder.mTvFeng.setText(feng.substring(0, feng.length() - 2));
                    mHolder.mTvFengValue.setText(feng.substring(feng.length() - 2));
                }
            }
            //空气污染等级
            int level = mList.get(position).getLevel();
            int color = CommonUtil.getWeatherColor1(level);

            //获取空气质量shape
            GradientDrawable drawable = (GradientDrawable) mHolder.mTvWeatherHint.getBackground();
            drawable.setStroke(1, color);
            mHolder.mTvWeatherHint.setTextColor(color);
            mHolder.mTvWeatherHint.setText(CommonUtil.getWeatherHint1(level));
            mHolder.mTvWeatherHint.setBackgroundDrawable(drawable);

            boolean mIsLeft, mIsRight;
            float leftMaxY = 0, rightMaxY = 0, leftMinY = 0, rightMinY = 0;

            //第一项不画左边
            if (position == 0) {
                mIsLeft = false;
            } else {
                mIsLeft = true;
                leftMaxY = mList.get(position - 1).getMaxTempera();
                leftMinY = mList.get(position - 1).getMinTempera();
            }

            //最后一项不画右边
            if (position == getItemCount() - 1) {
                mIsRight = false;
            } else {
                mIsRight = true;
                rightMaxY = mList.get(position + 1).getMaxTempera();
                rightMinY = mList.get(position + 1).getMinTempera();
            }

            mHolder.mLineView.setData(mIsLeft, mIsRight, leftMaxY, rightMaxY, leftMinY, rightMinY, mList.get(position).getMinTempera(), mList.get(position).getMaxTempera(), mMinValue, mMaxValue);
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private class Holder extends RecyclerView.ViewHolder {

        public TextView mTvTime;
        public TextView mTvDate;
        public ImageView mIvStartIcon;
        public TextView mTvStartName;
        public ImageView mIvEndIcon;
        public TextView mTvEndName;
        public TextView mTvFeng;
        public TextView mTvFengValue;
        public TextView mTvWeatherHint;
        public LinearLayout mLinearBg;
        public WeatherLineView mLineView;

        public Holder(View itemView) {
            super(itemView);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_time_weather_more);
            mTvDate = (TextView) itemView.findViewById(R.id.tv_date_weather_more);
            mIvStartIcon = (ImageView) itemView.findViewById(R.id.iv_weather_start_icon);
            mTvStartName = (TextView) itemView.findViewById(R.id.tv_weather_start_weather_more);
            mIvEndIcon = (ImageView) itemView.findViewById(R.id.iv_weather_end_icon);
            mTvEndName = (TextView) itemView.findViewById(R.id.tv_weather_end_weather_more);
            mTvFeng = (TextView) itemView.findViewById(R.id.tv_feng_weather_more);
            mTvFengValue = (TextView) itemView.findViewById(R.id.tv_feng_value_weather_more);
            mTvWeatherHint = (TextView) itemView.findViewById(R.id.tv_weather_hint_weather_more);
            mLinearBg = (LinearLayout) itemView.findViewById(R.id.linear_weather_more);
            mLineView = (WeatherLineView) itemView.findViewById(R.id.weather_line_view);
        }
    }

}
