package com.dingqiqi.qijuproject.activity.itemActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.activity.BaseActivity;
import com.dingqiqi.qijuproject.http.IPhoneApi;
import com.dingqiqi.qijuproject.manager.RetrofitManager;
import com.dingqiqi.qijuproject.mode.IPhoneMode;
import com.dingqiqi.qijuproject.util.CommonUtil;
import com.dingqiqi.qijuproject.util.InputMethodUtil;
import com.dingqiqi.qijuproject.util.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 手机号码归属地
 * Created by 丁奇奇 on 2017/6/23.
 */
public class PhoneNumAttributionActivity extends BaseActivity {

    private TextView mTvPhone;
    private TextView mTvCity;
    private TextView mTvCityCode;
    private TextView mTvOperator;
    private TextView mTvProvide;
    private TextView mTvZipCode;
    private LinearLayout mLinearPhone;
    private EditText mEtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_num_attribution_layout);

        initView();
        setTitle("手机号码归属地");
    }

    private void initView() {
        mTvPhone = (TextView) findViewById(R.id.tv_phone_phone);
        mTvCity = (TextView) findViewById(R.id.tv_phone_city);
        mTvCityCode = (TextView) findViewById(R.id.tv_phone_city_code);
        mTvOperator = (TextView) findViewById(R.id.tv_phone_operator);
        mTvProvide = (TextView) findViewById(R.id.tv_phone_provide);
        mTvZipCode = (TextView) findViewById(R.id.tv_phone_zip_code);

        mEtName = (EditText) findViewById(R.id.et_phone_name);
        mLinearPhone = (LinearLayout) findViewById(R.id.linear_phone);

        mEtName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    onClickQuery(v);
                    return true;
                }

                return false;
            }
        });
    }

    /**
     * 查询
     *
     * @param view
     */
    public void onClickQuery(View view) {
        InputMethodUtil.getInstance(this).hideSoftInput(mEtName);

        if (!isFastClick()) {
            final String phone = mEtName.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                ToastUtil.showToast("请输入手机号！");
            } else {
                if (phone.length() != 11) {
                    ToastUtil.showToast("请输入11位手机号！");
                    return;
                }

                showProgress();
                IPhoneApi iPhoneApi = RetrofitManager.getInstance(this).getRetrofit().create(IPhoneApi.class);
                Call<IPhoneMode> call = iPhoneApi.getPhotoData(CommonUtil.getKey(), phone);

                call.enqueue(new Callback<IPhoneMode>() {
                    @SuppressWarnings("ConstantConditions")
                    @Override
                    public void onResponse(Call<IPhoneMode> call, Response<IPhoneMode> response) {
                        dismissProgress();
                        if (response.body() != null && response.body().getResult() != null) {
                            if (mLinearPhone.getVisibility() == View.GONE) {
                                mLinearPhone.setVisibility(View.VISIBLE);
                            }

                            mTvPhone.setText(phone);
                            mTvCity.setText(response.body().getResult().getCity());
                            mTvCityCode.setText(response.body().getResult().getCityCode());
                            mTvOperator.setText(response.body().getResult().getOperator());
                            mTvProvide.setText(response.body().getResult().getProvince());
                            mTvZipCode.setText(response.body().getResult().getZipCode());
                        } else {
                            if (response.body() != null) {
                                ToastUtil.showToast(response.body().getMsg());
                            } else {
                                ToastUtil.showToast("数据错误!");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<IPhoneMode> call, Throwable t) {
                        dismissProgress();
                        ToastUtil.showToast("数据错误!" + t.getMessage());
                    }
                });
            }
        }
    }

}
