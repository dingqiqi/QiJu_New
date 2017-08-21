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
import com.dingqiqi.qijuproject.http.IIdiomApi;
import com.dingqiqi.qijuproject.manager.RetrofitManager;
import com.dingqiqi.qijuproject.mode.IdiomMode;
import com.dingqiqi.qijuproject.util.CommonUtil;
import com.dingqiqi.qijuproject.util.InputMethodUtil;
import com.dingqiqi.qijuproject.util.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 成语大全
 * Created by 丁奇奇 on 2017/6/23.
 */
public class IdiomActivity extends BaseActivity {

    private TextView mTvName;
    private TextView mTvPinYin;
    private TextView mTvPretation;
    private TextView mTvSample;
    private TextView mTvSampleFrom;
    private TextView mTvSource;
    private LinearLayout mLinearIdiom;
    private EditText mEtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idiom_layout);

        initView();
        setTitle("成语大全");
    }

    private void initView() {
        mTvName = (TextView) findViewById(R.id.tv_idiom_name);
        mTvPinYin = (TextView) findViewById(R.id.tv_idiom_pinyin);
        mTvPretation = (TextView) findViewById(R.id.tv_idiom_pretation);
        mTvSample = (TextView) findViewById(R.id.tv_idiom_sample);
        mTvSampleFrom = (TextView) findViewById(R.id.tv_idiom_sample_from);
        mTvSource = (TextView) findViewById(R.id.tv_idiom_source);
        mEtName = (EditText) findViewById(R.id.et_idiom_name);
        mLinearIdiom = (LinearLayout) findViewById(R.id.linear_idiom);

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
            String name = mEtName.getText().toString().trim();
            if (TextUtils.isEmpty(name)) {
                ToastUtil.showToast("请输入成语！");
            } else {
                showProgress();
                IIdiomApi iIdiomApi = RetrofitManager.getInstance(this).getRetrofit().create(IIdiomApi.class);
                Call<IdiomMode> call = iIdiomApi.getIdiomData(CommonUtil.getKey(), name);

                call.enqueue(new Callback<IdiomMode>() {
                    @SuppressWarnings("ConstantConditions")
                    @Override
                    public void onResponse(Call<IdiomMode> call, Response<IdiomMode> response) {
                        dismissProgress();
                        if (response.body() != null && response.body().getResult() != null) {
                            if (mLinearIdiom.getVisibility() == View.GONE) {
                                mLinearIdiom.setVisibility(View.VISIBLE);
                            }

                            mTvName.setText(response.body().getResult().getName());
                            mTvPinYin.setText(response.body().getResult().getPinyin());
                            mTvPretation.setText(response.body().getResult().getPretation());
                            mTvSample.setText(response.body().getResult().getSample());

                            if (TextUtils.isEmpty(response.body().getResult().getSampleFrom())) {
                                mTvSampleFrom.setVisibility(View.GONE);
                            } else {
                                mTvSampleFrom.setVisibility(View.VISIBLE);
                                mTvSampleFrom.setText(response.body().getResult().getSampleFrom());
                            }
                            mTvSource.setText(response.body().getResult().getSource());
                        } else {
                            if (response.body() != null) {
                                ToastUtil.showToast(response.body().getMsg());
                            } else {
                                ToastUtil.showToast("数据错误!");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<IdiomMode> call, Throwable t) {
                        dismissProgress();
                        ToastUtil.showToast("数据错误!" + t.getMessage());
                    }
                });
            }
        }
    }

}
