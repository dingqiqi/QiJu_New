package com.dingqiqi.qijuproject.activity.itemActivity;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.activity.BaseActivity;
import com.dingqiqi.qijuproject.adapter.FragmentAdapter;
import com.dingqiqi.qijuproject.fragment.BaseFragment;
import com.dingqiqi.qijuproject.fragment.MenuFragment;
import com.dingqiqi.qijuproject.fragment.SaveMenuFragment;
import com.dingqiqi.qijuproject.util.DisplayUtil;
import com.dingqiqi.qijuproject.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜谱大全
 * Created by 丁奇奇 on 2017/7/19.
 */
public class MenuListActivity extends BaseActivity {

    private RadioGroup mRadioGroup;
    private View mLineView;
    private ViewPager mViewPager;

    private List<BaseFragment> mList;

    private View mLineBackView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list_layout);

        initView();
        setListener();
    }

    private void initView() {
        setTitle("菜谱大全");

        mList = new ArrayList<>();
        BaseFragment menuFragment = new MenuFragment();
        mList.add(menuFragment);
        BaseFragment saveMenuFragment = new SaveMenuFragment();
        mList.add(saveMenuFragment);

        mRadioGroup = (RadioGroup) findViewById(R.id.rg_menu_title);
        mLineView = findViewById(R.id.view_line);
        mLineBackView = findViewById(R.id.view_back_line);
        mViewPager = (ViewPager) findViewById(R.id.viewpager_menu);

        mLineView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mLineView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLineView.getLayoutParams();
                params.width = DisplayUtil.getScreenWidth(MenuListActivity.this) / 2;
                mLineView.setLayoutParams(params);
            }
        });

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), mList);
        mViewPager.setAdapter(adapter);

        ((RadioButton) mRadioGroup.getChildAt(0)).setChecked(true);
    }

    public void setVisible(int vis) {
        mLineBackView.setVisibility(vis);
    }

    /**
     * 设置监听
     */
    private void setListener() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int length = group.getChildCount();

                for (int i = 0; i < length; i++) {
                    RadioButton radioButton = (RadioButton) group.getChildAt(i);
                    if (radioButton.isChecked()) {
                        mViewPager.setCurrentItem(i);
                        break;
                    }
                }
            }
        });

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mLineView.getLayoutParams();
                params.leftMargin = (int) ((position + positionOffset) * DisplayUtil.getScreenWidth(MenuListActivity.this) / 2);
                mLineView.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int position) {
                ((RadioButton) mRadioGroup.getChildAt(position)).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mList.get(mViewPager.getCurrentItem()).onBack()) {
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mList.clear();
    }

}
