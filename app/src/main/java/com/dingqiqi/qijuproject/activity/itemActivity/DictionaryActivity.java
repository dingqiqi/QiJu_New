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
import com.dingqiqi.qijuproject.http.IDictionaryApi;
import com.dingqiqi.qijuproject.http.IIdiomApi;
import com.dingqiqi.qijuproject.manager.RetrofitManager;
import com.dingqiqi.qijuproject.mode.DictionaryMode;
import com.dingqiqi.qijuproject.mode.IdiomMode;
import com.dingqiqi.qijuproject.util.CommonUtil;
import com.dingqiqi.qijuproject.util.InputMethodUtil;
import com.dingqiqi.qijuproject.util.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 新华字典
 * Created by 丁奇奇 on 2017/6/23.
 */
public class DictionaryActivity extends BaseActivity {

    private TextView mTvName;
    private TextView mTvPinYin;
    private TextView mTvWuBi;
    private TextView mTvBuShou;
    private TextView mTvBiHua;
    private TextView mTvQuBiHua;
    private TextView mTvBrief;
    private TextView mTvDetail;
    private LinearLayout mLinearDictionary;
    private EditText mEtName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_layout);

        initView();
        setTitle("新华字典");
    }

    private void initView() {
        mTvName = (TextView) findViewById(R.id.tv_dictionary_name);
        mTvPinYin = (TextView) findViewById(R.id.tv_dictionary_pinyin);
        mTvWuBi = (TextView) findViewById(R.id.tv_dictionary_wubi);
        mTvBuShou = (TextView) findViewById(R.id.tv_dictionary_bushou);
        mTvBiHua = (TextView) findViewById(R.id.tv_dictionary_bihuashu);
        mTvQuBiHua = (TextView) findViewById(R.id.tv_dictionary_qu_bihuashu);
        mTvBrief = (TextView) findViewById(R.id.tv_dictionary_brief);
        mTvDetail = (TextView) findViewById(R.id.tv_dictionary_detail);
        mEtName = (EditText) findViewById(R.id.et_dictionary_name);
        mLinearDictionary = (LinearLayout) findViewById(R.id.linear_dictionary);

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
                IDictionaryApi dictionaryApi = RetrofitManager.getInstance(this).getRetrofit().create(IDictionaryApi.class);
                Call<DictionaryMode> call = dictionaryApi.getDictionaryData(CommonUtil.getKey(), name);

                call.enqueue(new Callback<DictionaryMode>() {
                    @SuppressWarnings("ConstantConditions")
                    @Override
                    public void onResponse(Call<DictionaryMode> call, Response<DictionaryMode> response) {
                        dismissProgress();
                        if (response.body() != null && response.body().getResult() != null) {
                            if (mLinearDictionary.getVisibility() == View.GONE) {
                                mLinearDictionary.setVisibility(View.VISIBLE);
                            }

                            mTvName.setText(response.body().getResult().getName());
                            mTvPinYin.setText(response.body().getResult().getPinyin());
                            mTvWuBi.setText(response.body().getResult().getWubi());
                            mTvBuShou.setText(response.body().getResult().getBushou());
                            mTvBiHua.setText(response.body().getResult().getBihua());
                            mTvQuBiHua.setText(response.body().getResult().getBihuaWithBushou());
                            mTvBrief.setText(response.body().getResult().getBrief());
                            mTvDetail.setText(response.body().getResult().getDetail());
                        } else {
                            if (response.body() != null) {
                                ToastUtil.showToast(response.body().getMsg());
                            } else {
                                ToastUtil.showToast("数据错误!");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<DictionaryMode> call, Throwable t) {
                        dismissProgress();
                        ToastUtil.showToast("数据错误!" + t.getMessage());
                    }
                });
            }
        }
    }

}
