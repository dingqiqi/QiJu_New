package com.dingqiqi.qijuproject.activity.itemActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dingqiqi.qijuproject.R;
import com.dingqiqi.qijuproject.activity.BaseActivity;
import com.dingqiqi.qijuproject.dialog.SelectImageDialog;
import com.dingqiqi.qijuproject.dialog.SelectLevelDialog;
import com.dingqiqi.qijuproject.dialog.ShowImageDialog;
import com.dingqiqi.qijuproject.util.FileUtil;
import com.dingqiqi.qijuproject.util.ImageUtil;
import com.dingqiqi.qijuproject.view.MoveView;

import java.io.File;

public class PuzzleActivity extends BaseActivity {
    /**
     * 点击次数
     */
    private TextView mTvCount;
    /**
     * 消耗时间
     */
    private TextView mTvTime;
    /**
     * 拼图控件
     */
    private MoveView mMoveView;
    /**
     * 游戏难度控件
     */
    private TextView mTvUpdateLevel;
    /**
     * 难度等级
     */
    private int mNum = 3;
    /**
     * 选择游戏图片dialog
     */
    private SelectImageDialog mImageDialog;
    /**
     * 选择游戏难度dialog
     */
    private SelectLevelDialog mLevelDialog;
    /**
     * 显示图片dialog
     */
    private ShowImageDialog mShowDialog;
    /**
     * 当前照片文件
     */
    private File mCurPhotoFile;
    /**
     * 是否进行裁剪
     */
    public static boolean mIsZoom = false;
    /**
     * 退出记录时间
     */
    private long mExitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzle_layout);

        mTvUpdateLevel = (TextView) findViewById(R.id.tv_update_level);
        mTvCount = (TextView) findViewById(R.id.tv_game_count);
        mTvTime = (TextView) findViewById(R.id.tv_game_time);
        mMoveView = (MoveView) findViewById(R.id.move_view);

        mImageDialog = new SelectImageDialog();
        mLevelDialog = new SelectLevelDialog();
        mShowDialog = new ShowImageDialog();

        initView();
    }

    private void initView() {
        mMoveView.setOnCallBack(new MoveView.onCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(PuzzleActivity.this, "拼图成功!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTime(int time) {
                String textTime = "用时: " + getTextTime(time);
                mTvTime.setText(textTime);
            }

            @Override
            public void onCount(int count) {
                String textCount = "次数: " + count;
                mTvCount.setText(textCount);
            }
        });

        mImageDialog.setListener(this, new SelectImageDialog.onClickCallBack() {
            @Override
            public void onClick(File file) {
                mCurPhotoFile = file;
                if (mImageDialog != null) {
                    mImageDialog.dismiss();
                }
            }

            @Override
            public void onDefault() {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_default);
                mMoveView.setBitmapBg(bitmap);

                if (mImageDialog != null) {
                    mImageDialog.dismiss();
                }
            }
        });

        mLevelDialog.setListener(new SelectLevelDialog.onClickCallBack() {
            @Override
            public void onClick(int num) {
                mNum = num;
                String text = "难度: " + num + " * " + num;
                mTvUpdateLevel.setText(text);
                mMoveView.startGame(mNum);
                if (mLevelDialog != null) {
                    mLevelDialog.dismiss();
                }
            }
        });
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_update_image:
                if (mImageDialog != null) {
                    mImageDialog.show(getFragmentManager(), "select");
                }
                break;
            case R.id.tv_update_level:
                if (mLevelDialog != null) {
                    mLevelDialog.show(getFragmentManager(), "level");
                }
                break;
            case R.id.tv_query_image:
                if (mShowDialog != null) {
                    mShowDialog.show(getFragmentManager(), "show");
                    mShowDialog.setBackground(mMoveView.getBitmapBg());
                }
                break;
            case R.id.tv_start_game:
                startGame(mNum);
                break;
        }
    }

    /**
     * 初始化游戏
     */
    public void startGame(int num) {
        mMoveView.startGame(num);

        String level = "难度: " + num + " * " + num;
        mTvUpdateLevel.setText(level);

        String textTime = "用时: " + getTextTime(0);
        mTvTime.setText(textTime);
        String textCount = "次数: " + 0;
        mTvCount.setText(textCount);
    }

    /**
     * 格式化时间
     *
     * @param time 秒
     * @return 显示的时间格式
     */
    private String getTextTime(int time) {
        int min = time / 60;
        int second = time % 60;

        String timeText;
        if (min < 10) {
            timeText = "0" + min;
        } else {
            timeText = String.valueOf(min);
        }

        if (second < 10) {
            timeText = timeText + ":" + "0" + second;
        } else {
            timeText = timeText + ":" + String.valueOf(second);
        }

        return timeText;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SelectImageDialog.SELECT_IMAGE) {
                Uri mImageUri = data.getData();
                if (mImageUri == null) {
                    return;
                }
                //是否裁剪
                if (mIsZoom) {
                    mImageDialog.startPhotoZoom(mImageUri);
                } else {
                    // 拿到图片路径
                    String path = FileUtil.getImagePath(PuzzleActivity.this, mImageUri);
                    Bitmap bitmap = ImageUtil.compressImageFromFile(path);
                    setGameBg(bitmap);
                }
            } else if (requestCode == SelectImageDialog.SELECT_PHOTO) {
                if (mCurPhotoFile.exists()) {
                    //是否裁剪
                    if (mIsZoom) {
                        Uri mImageUri = FileProvider.getUriForFile(PuzzleActivity.this, getPackageName() + ".FileProvide", mCurPhotoFile);
                        mImageDialog.startPhotoZoom(mImageUri);
                    } else {
                        String path = mCurPhotoFile.getPath();
                        Bitmap bitmap = ImageUtil.compressImageFromFile(path);
                        setGameBg(bitmap);
                    }
                }
            } else if (requestCode == SelectImageDialog.SELECT_ZOOM) {
                // 拿到剪切数据
                Bitmap bitmap = FileUtil.getImageBitmap(PuzzleActivity.this, data);
                if (bitmap != null) {
                    setGameBg(bitmap);
                } else {
                    Toast.makeText(PuzzleActivity.this, "图片获取失败!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    /**
     * 设置游戏背景
     *
     * @param bitmap 背景
     */
    private void setGameBg(Bitmap bitmap) {
        mMoveView.setBitmapBg(bitmap);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - mExitTime > 2000) {
            mExitTime = System.currentTimeMillis();
            Toast.makeText(this, "再按一次退出游戏!", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }
}
