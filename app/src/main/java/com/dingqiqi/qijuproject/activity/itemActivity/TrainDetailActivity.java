package com.dingqiqi.qijuproject.activity.itemActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.activity.BaseActivity;
import com.dingqiqi.qijuproject.dialog.DialogFactory;
import com.dingqiqi.qijuproject.dialog.LoadingDialog;
import com.dingqiqi.qijuproject.dialog.TrainDetailDialog;
import com.dingqiqi.qijuproject.http.ITrainApi;
import com.dingqiqi.qijuproject.manager.RetrofitManager;
import com.dingqiqi.qijuproject.mode.TrainMode;
import com.dingqiqi.qijuproject.util.CommonUtil;
import com.dingqiqi.qijuproject.util.ToastUtil;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 火车时刻表
 * Created by dingqiqi on 2017/6/19.
 */
public class TrainDetailActivity extends BaseActivity {

    private TrainDetailDialog mDetailDialog;

    private LoadingDialog mLoadingDialog;

    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_detail_layout);
        initView();
    }

    private void initView() {
        setTitle("火车时刻表");
        mDetailDialog = new TrainDetailDialog();

        mEditText = (EditText) findViewById(R.id.et_train_name);
    }

    public void onClickQuery(View view) {
        if (!isFastClick()) {
            String text = mEditText.getText().toString().trim();
            if (TextUtils.isEmpty(text)) {
                ToastUtil.showToast("请输入车次信息!");
                return;
            }
            text = text.toUpperCase(Locale.getDefault());
            requestData(text);
        }
    }

    public void requestData(String text) {
        ITrainApi trainApi = RetrofitManager.getInstance(this).getRetrofit().create(ITrainApi.class);
        Call<TrainMode> call = trainApi.getTrainData(CommonUtil.getKey(), text);

        mLoadingDialog = DialogFactory.getInstance().showProgress(getSupportFragmentManager());

        call.enqueue(new Callback<TrainMode>() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onResponse(Call<TrainMode> call, Response<TrainMode> response) {
                mLoadingDialog.dismiss();
                if (response.body() != null && response.body().getResult() != null) {
                    mDetailDialog.show(getSupportFragmentManager(), "TrainDetailActivity");
                    mDetailDialog.setList(response.body().getResult());
                } else {
                    if (response.body() != null) {
                        ToastUtil.showToast(response.body().getMsg());
                    } else {
                        ToastUtil.showToast("系统异常!");
                    }
                }
            }

            @Override
            public void onFailure(Call<TrainMode> call, Throwable t) {
                mLoadingDialog.dismiss();
                ToastUtil.showToast("系统异常!");
            }
        });


    }

}
