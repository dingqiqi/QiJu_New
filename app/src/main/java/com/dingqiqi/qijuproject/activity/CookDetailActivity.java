package com.dingqiqi.qijuproject.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.manager.GlideManager;
import com.dingqiqi.qijuproject.mode.MenuDetailMode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 菜谱详情
 * Created by 丁奇奇 on 2017/7/25.
 */
public class CookDetailActivity extends BaseActivity {

    private MenuDetailMode mMenuMode;
    //标题
    private TextView mTvTitle;
    //简介
    private TextView mTvDetail;
    private TextView mTvDetailTitle;
    //材料
    private TextView mTvMaterial;
    private TextView mTvMaterialTitle;
    //步奏
    private TextView mTvMethod;
    //成品
    private ImageView mIvImg;

    private GlideManager mGlideManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cook_detail_layout);

        mMenuMode = getIntent().getParcelableExtra("data");

        mGlideManager = new GlideManager(this);

        initView();
        setData();
    }

    private void initView() {
        mTvTitle = (TextView) findViewById(R.id.tv_cook_detail_title);
        mTvDetail = (TextView) findViewById(R.id.tv_cook_detail_detail);
        mTvMaterial = (TextView) findViewById(R.id.tv_cook_detail_material);
        mTvDetailTitle = (TextView) findViewById(R.id.tv_cook_detail_detail_title);
        mTvMaterialTitle = (TextView) findViewById(R.id.tv_cook_detail_material_title);
        mTvMethod = (TextView) findViewById(R.id.tv_cook_detail_method);
        mIvImg = (ImageView) findViewById(R.id.iv_cook_detail_img);

    }

    private void setData() {
        if (mMenuMode != null) {
            setTitle(mMenuMode.getTitle());

            //存在图片为空的情况
            String imgUrl = TextUtils.isEmpty(mMenuMode.getImg()) ? getIntent().getStringExtra("img") : mMenuMode.getImg();
            mGlideManager.LoadImage(imgUrl, mIvImg);

            mTvTitle.setText(mMenuMode.getTitle());

            String detail = mMenuMode.getSumary();
            if (TextUtils.isEmpty(detail)) {
                mTvDetail.setVisibility(View.GONE);
                mTvDetailTitle.setVisibility(View.GONE);
            } else {
                mTvDetail.setText(detail);
            }

            String material = mMenuMode.getIngredients();
            if (TextUtils.isEmpty(material)) {
                mTvMaterial.setVisibility(View.GONE);
                mTvMaterialTitle.setVisibility(View.GONE);
            } else {
                mTvMaterial.setText(material);
            }

            String method = mMenuMode.getMethod();

            if (!TextUtils.isEmpty(method)) {
                StringBuilder mBuffer = new StringBuilder();
                try {
                    JSONArray jsonArray = new JSONArray(method);
                    int size = jsonArray.length();

                    for (int i = 0; i < size; i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String step = jsonObject.optString("step");
                        mBuffer.append(step);
                        mBuffer.append("\n");
                        mBuffer.append("\n");
                    }

                    method = mBuffer.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    mTvMethod.setText(method);
                }
            }
        }
    }

}
