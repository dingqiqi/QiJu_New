package com.dingqiqi.qijuproject.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class LoopPageAdapter extends PagerAdapter {

    // private Context mContext;
    private List<View> mList;

    public LoopPageAdapter(Context mContext, List<View> mList) {
        super();
        // this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size() + 2;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        position = getRealPosition(position);

        // 滑动到第一个或者最后一个不销毁
        if (position != 0 && position != mList.size() - 1) {

            container.removeView((View) object);
        }

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        position = getRealPosition(position);

        // View view = LayoutInflater.from(mContext).inflate(
        // R.layout.loop_viewpager_layout, null, false);
        // TextView item = (TextView) view.findViewById(R.id.tv);
        // item.setText("page " + position);

        // 加try catch是加入第一个或者最后一个会重复
        try {
            container.addView(mList.get(position));
        } catch (Exception e) {
        }

        return mList.get(position);
    }

    /**
     * 获取相应的position
     *
     * @param position
     * @return
     */
    public int getRealPosition(int position) {
        if (position == 0) {
            position = mList.size() - 1;
        } else if (position == getCount() - 1) {
            position = 0;
        } else {
            position -= 1;
        }

        return position;
    }

}
