package com.dingqiqi.qijuproject.activity.itemActivity;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.activity.BaseActivity;
import com.dingqiqi.qijuproject.adapter.DrivingActivityAdapter;
import com.dingqiqi.qijuproject.dialog.DialogFactory;
import com.dingqiqi.qijuproject.fragment.DrivingBaseFragment;
import com.dingqiqi.qijuproject.mode.OtherMode;
import com.dingqiqi.qijuproject.util.ToastUtil;
import com.dingqiqi.qijuproject.view.RecyLine;

import java.util.List;

/**
 * 驾照考试
 * Created by dingqiqi on 2017/6/14.
 */
public class DrivingOtherActivity extends BaseActivity {
    /**
     * 标题数据源
     */
    private List<OtherMode> mList;

    private RecyclerView mRecyclerView;

    private DialogFragment mDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driving_other_layout);
        //获取数据源
        mList = getIntent().getParcelableArrayListExtra("data");

        if (mList == null || mList.size() == 0) {
            ToastUtil.showToast("数据源获取失败!");
            finish();
        }

        initView();
    }

    private void initView() {
        setTitle("驾考题目类型");

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_driving_other_activity);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        //间隔线
        RecyLine mRecyLine = new RecyLine(this);
        mRecyLine.setOrien(0);
        mRecyclerView.addItemDecoration(mRecyLine);
        DrivingActivityAdapter mAdapter = new DrivingActivityAdapter(this, mList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setItemClickListener(new DrivingActivityAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String id = mList.get(position).getCid();
                mRecyclerView.setVisibility(View.GONE);
                setTitle(mList.get(position).getTitle());

                DrivingBaseFragment baseFragment = new DrivingBaseFragment();
                baseFragment.setCId(id);
                getSupportFragmentManager().beginTransaction().replace(R.id.content_layout, baseFragment).commit();
            }
        });
    }

    /**
     * 弹出退出提示框
     */
    private void showExitDialog() {
        mDialogFragment = DialogFactory.getInstance().showTwoBtnDialog(getSupportFragmentManager(), "系统提示",
                "您正在进行驾照模拟考试练习,是否退出练习?", "", "", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDialogFragment.dismiss();
                        setTitle("驾考题目类型");
                        mRecyclerView.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mRecyclerView.getVisibility() == View.GONE) {
                showExitDialog();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }


}
