package com.dingqiqi.qijuproject.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.activity.itemActivity.WeatherActivity;
import com.dingqiqi.qijuproject.dialog.DialogFactory;
import com.dingqiqi.qijuproject.dialog.FullScreenDialog;
import com.dingqiqi.qijuproject.mode.WeatherMode;
import com.dingqiqi.qijuproject.popWindow.WeatherMorePop;
import com.dingqiqi.qijuproject.util.CommonUtil;
import com.dingqiqi.qijuproject.util.DisplayUtil;
import com.dingqiqi.qijuproject.util.TimeUtil;
import com.dingqiqi.qijuproject.util.ToastUtil;
import com.dingqiqi.qijuproject.view.CircleProgress;

/**
 * 天气适配器
 * Created by dingqiqi on 2017/6/7.
 */
public class WeatherAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private WeatherMode mWeatherMode;

    private static final int[] mValueArray = new int[]{1, 2, 3, 4, 5};

    public WeatherAdapter(Context mContext, WeatherMode weatherMode) {
        this.mContext = mContext;
        mWeatherMode = weatherMode;
    }

    @Override
    public int getItemViewType(int position) {
        return getViewType(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder mHolder = null;
        if (viewType == mValueArray[0]) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.item_weather_one_layout, parent, false);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
            params.height = DisplayUtil.getScreenHeight(mContext) * 9 / 20;
            view.setLayoutParams(params);
            mHolder = new HolderOne(view);
        } else if (viewType == mValueArray[1]) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.items_weather_two_layout, parent, false);
            mHolder = new HolderTwo(view);
        } else if (viewType == mValueArray[2]) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.items_weather_three_layout, parent, false);
            mHolder = new HolderThree(view);
        } else if (viewType == mValueArray[3]) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.items_weather_four_layout, parent, false);
            mHolder = new HolderFour(view);
        } else if (viewType == mValueArray[4]) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.items_weather_five_layout, parent, false);
            mHolder = new HolderFive(view);
        }

        return mHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (mWeatherMode == null) {
            return;
        }
        int viewType = getViewType(position);
        if (viewType == mValueArray[0]) {
            HolderOne mHolder = (HolderOne) holder;
            if (!TextUtils.isEmpty(mWeatherMode.getCity()) && mWeatherMode.getCity().equals(mWeatherMode.getProvince())) {
                String text;
                if (mWeatherMode.getCity().equals(mWeatherMode.getDistrct())) {
                    text = mWeatherMode.getProvince() + "市 (手输位置!)";
                } else {
                    text = mWeatherMode.getProvince() + "市 " + mWeatherMode.getDistrct() + " (手输位置!)";
                }

                mHolder.mTvAddress.setText(text);
            } else {
                if (!TextUtils.isEmpty(mWeatherMode.getCity()) && mWeatherMode.getCity().equals(mWeatherMode.getDistrct())) {
                    String text = mWeatherMode.getProvince() + "省 " + mWeatherMode.getCity() + "市 (手输位置!)";
                    mHolder.mTvAddress.setText(text);
                } else {
                    String text = mWeatherMode.getProvince() + "省 " + mWeatherMode.getCity() + "市 " + mWeatherMode.getDistrct() + "县 (手输位置!)";
                    mHolder.mTvAddress.setText(text);
                }
            }

            mHolder.mTvTemperature.setText(mWeatherMode.getTemperature());
            mHolder.mTvWeather.setText(mWeatherMode.getWeather());

            mHolder.mTvAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContext instanceof WeatherActivity) {
                        final WeatherActivity activity = (WeatherActivity) mContext;
                        DialogFactory.getInstance().showOneBtnEditDialog(activity.getSupportFragmentManager(), "系统提示", "", "", new FullScreenDialog.onDialogClick() {
                            @Override
                            public void onClick(String text) {
                                if (TextUtils.isEmpty(text)) {
                                    return;
                                }

                                if (mContext.getString(R.string.address_test).equals(text)) {
                                    text = text.substring(0, text.length() - 2);
                                }

                                if (text.endsWith("省") || text.endsWith("市") || text.endsWith("县") || text.endsWith("区") || text.endsWith("镇")) {
                                    text = text.substring(0, text.length() - 1);
                                }

                                activity.initData(text);
                            }
                        });
                    }
                }
            });

            String value = mWeatherMode.getPollutionIndex();
            try {
                mHolder.mTvKongQi.setText(CommonUtil.getWeatherHint(Integer.parseInt(value)));
                mHolder.mTvKongQi.setTextColor(CommonUtil.getWeatherColor(Integer.parseInt(value)));
            } catch (Exception e) {
                value = value + "  >";
                mHolder.mTvKongQi.setText(value);
                mHolder.mTvKongQi.setTextColor(Color.WHITE);
            }

            if (!TextUtils.isEmpty(mWeatherMode.getWind())) {
                String fengTitle = mWeatherMode.getWind().substring(0, mWeatherMode.getWind().length() - 2);
                String fengValue = mWeatherMode.getWind().substring(mWeatherMode.getWind().length() - 2, mWeatherMode.getWind().length());
                mHolder.mTvFeng.setText(fengTitle);
                mHolder.mTvFengValue.setText(fengValue);
            } else {
                mHolder.mTvFeng.setText("无风");
                mHolder.mTvFengValue.setText("0级");
            }

            if (!TextUtils.isEmpty(mWeatherMode.getHumidity()) && mWeatherMode.getHumidity().contains("：")) {
                mHolder.mTvShiDuValue.setText(mWeatherMode.getHumidity().split("：")[1]);
            } else {
                mHolder.mTvShiDuValue.setText("0%");
            }

            mHolder.mTvChuanYiValue.setText(mWeatherMode.getDressingIndex());

        } else if (viewType == mValueArray[1]) {
            int rePosition = position - 1;
            HolderTwo mHolder = (HolderTwo) holder;

            if (mWeatherMode.getFuture() != null && mWeatherMode.getFuture().size() > 3) {
                String time = TimeUtil.getCurrentTimeName(mWeatherMode.getFuture().get(rePosition).getDate());
                String week = mWeatherMode.getFuture().get(rePosition).getWeek();
                if (!TextUtils.isEmpty(week) && week.contains("星期")) {
                    week = week.replace("星期", "周");
                }
                time = TextUtils.isEmpty(time) ? week : time;
                mHolder.mTvTime.setText(time);

                String dayTime = mWeatherMode.getFuture().get(rePosition).getDayTime();
                String night = mWeatherMode.getFuture().get(rePosition).getNight();

                dayTime = TextUtils.isEmpty(dayTime) ? "" : dayTime;
                night = TextUtils.isEmpty(night) ? "" : night;

                mHolder.mViewVerLine.setVisibility(View.GONE);
                mHolder.mTvWeather1.setVisibility(View.GONE);
                if (dayTime.equals(night)) {
                    if (TextUtils.isEmpty(dayTime)) {
                        mHolder.mTvWeather.setText("暂无天气");
                    } else {
                        mHolder.mTvWeather.setText(dayTime);
                    }
                } else {
                    if (TextUtils.isEmpty(dayTime)) {
                        mHolder.mTvWeather.setText(night);
                    } else if (TextUtils.isEmpty(night)) {
                        mHolder.mTvWeather.setText(dayTime);
                    } else {
                        String text = dayTime + "转" + night;
                        mHolder.mTvWeather.setText(text);
                    }
                }
                mHolder.mTvTemperature.setText(mWeatherMode.getFuture().get(rePosition).getTemperature().replace("C", "").replace("c", ""));

                mHolder.mLinearMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mContext instanceof WeatherActivity) {
                            WeatherMorePop.getInstance(mContext, mWeatherMode.getFuture()).showPop(((WeatherActivity) mContext).getParentView());
                        }
                    }
                });
            }

            if (rePosition == 2) {
                mHolder.mViewLine.setVisibility(View.GONE);
            } else {
                mHolder.mViewLine.setVisibility(View.VISIBLE);
            }

        } else if (viewType == mValueArray[2]) {
            HolderThree mHolder = (HolderThree) holder;

            String text = "7天趋势预报";
            if (mWeatherMode.getFuture() != null && mWeatherMode.getFuture().size() > 0) {
                text = mWeatherMode.getFuture().size() + "天趋势预报";
            }
            mHolder.mTvMore.setText(text);
            mHolder.mTvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mWeatherMode.getFuture() != null && mWeatherMode.getFuture().size() > 0) {
                        if (mContext instanceof WeatherActivity) {
                            WeatherMorePop.getInstance(mContext, mWeatherMode.getFuture()).showPop(((WeatherActivity) mContext).getParentView());
                        }
                    } else {
                        ToastUtil.showToast("暂无更多天气数据!");
                    }
                }
            });

        } else if (viewType == mValueArray[3]) {
            HolderFour mHolder = (HolderFour) holder;

            String text = mWeatherMode.getSunrise() + "日出 ---- " + mWeatherMode.getSunset() + "日落";
            mHolder.mTvTitle.setText(text);

        } else if (viewType == mValueArray[4]) {
            HolderFive mHolder = (HolderFive) holder;

            String value = mWeatherMode.getPollutionIndex();
            try {
                mHolder.mTvWeather.setText(CommonUtil.getWeatherHint(Integer.parseInt(value)).replace(">", ""));
                mHolder.mProgress.setProgress(Integer.parseInt(value));
//                mHolder.mTvWeather.setTextColor(CommonUtil.getWeatherColor(Integer.parseInt(value)));
            } catch (Exception e) {
                value = "空气指数" + value;
                mHolder.mTvWeather.setText(value);
//                mHolder.mTvWeather.setTextColor(Color.WHITE);
            }
            String time = mWeatherMode.getTime() + "发布";
            mHolder.mTvTime.setText(time);
        }
    }

    @Override
    public int getItemCount() {
        return 7;
    }

    public class HolderOne extends RecyclerView.ViewHolder {

        public TextView mTvAddress;
        public TextView mTvTemperature;
        public TextView mTvWeather;
        public TextView mTvKongQi;
        public TextView mTvFeng;
        public TextView mTvFengValue;
        public TextView mTvShiDuValue;
        public TextView mTvChuanYiValue;

        public HolderOne(View itemView) {
            super(itemView);
            mTvAddress = (TextView) itemView.findViewById(R.id.tv_weather_one_address);
            mTvTemperature = (TextView) itemView.findViewById(R.id.tv_weather_one_temperature);
            mTvWeather = (TextView) itemView.findViewById(R.id.tv_weather_one_weather);
            mTvKongQi = (TextView) itemView.findViewById(R.id.tv_weather_one_kongqi);
            mTvFeng = (TextView) itemView.findViewById(R.id.tv_feng);
            mTvFengValue = (TextView) itemView.findViewById(R.id.tv_feng_value);
            mTvShiDuValue = (TextView) itemView.findViewById(R.id.tv_shidu_value);
            mTvChuanYiValue = (TextView) itemView.findViewById(R.id.tv_chuanyi_value);
        }
    }

    public class HolderTwo extends RecyclerView.ViewHolder {

        public TextView mTvTime;
        public ImageView mIvIcon;
        public TextView mTvWeather;
        public TextView mTvWeather1;
        public TextView mTvTemperature;
        public View mViewLine;
        public View mViewVerLine;
        public LinearLayout mLinearMore;

        public HolderTwo(View itemView) {
            super(itemView);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_two_time);
            mIvIcon = (ImageView) itemView.findViewById(R.id.iv_two_icon);
            mTvWeather = (TextView) itemView.findViewById(R.id.tv_two_weather);
            mTvWeather1 = (TextView) itemView.findViewById(R.id.tv_two_weather1);
            mTvTemperature = (TextView) itemView.findViewById(R.id.tv_two_temperature);
            mViewLine = itemView.findViewById(R.id.view_two_line);
            mViewVerLine = itemView.findViewById(R.id.view_two_ver_line);
            mLinearMore = (LinearLayout) itemView.findViewById(R.id.linear_two_more);
        }
    }

    public class HolderThree extends RecyclerView.ViewHolder {

        public TextView mTvMore;

        public HolderThree(View itemView) {
            super(itemView);
            mTvMore = (TextView) itemView.findViewById(R.id.tv_weather_more);
        }
    }

    public class HolderFour extends RecyclerView.ViewHolder {

        public TextView mTvTitle;

        public HolderFour(View itemView) {
            super(itemView);
            mTvTitle = (TextView) itemView.findViewById(R.id.tv_weather_four_title);
        }
    }

    public class HolderFive extends RecyclerView.ViewHolder {

        public TextView mTvWeather;
        public TextView mTvTime;
        public CircleProgress mProgress;

        public HolderFive(View itemView) {
            super(itemView);
            mTvWeather = (TextView) itemView.findViewById(R.id.tv_five_weather);
            mTvTime = (TextView) itemView.findViewById(R.id.tv_five_time);
            mProgress = (CircleProgress) itemView.findViewById(R.id.circle_progress_five);
        }
    }

    private int getViewType(int position) {
        switch (position) {
            case 0:
                return Type.ONE.getValue();
            case 1:
            case 2:
            case 3:
                return Type.TWO.getValue();
            case 4:
                return Type.THREE.getValue();
            case 5:
                return Type.FOUR.getValue();
            case 6:
                return Type.FIVE.getValue();
            default:
                return Type.FIVE.getValue();
        }
    }

    private enum Type {
        ONE(mValueArray[0]), TWO(mValueArray[1]), THREE(mValueArray[2]), FOUR(mValueArray[3]), FIVE(mValueArray[4]);

        private int value;

        Type(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
