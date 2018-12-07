package com.trio.standard.module.image;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.trio.standard.R;
import com.trio.standard.module.base.BaseMvpActivity;
import com.trio.standard.module.base.BasePresenter;
import com.trio.standard.utils.ImageUtil;
import com.trio.standard.widgets.FileSelectorDialog;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

public class FilePickActivity extends BaseMvpActivity {

    @Bind(R.id.bt_file_pick)
    Button mBtFilePick;
    @Bind(R.id.rb_all)
    RadioButton mRbAll;
    @Bind(R.id.rb_jpg)
    RadioButton mRbJpg;
    @Bind(R.id.rb_txt)
    RadioButton mRbTxt;
    @Bind(R.id.rb_mp3)
    RadioButton mRbMp3;
    @Bind(R.id.radiogroup)
    RadioGroup mRadiogroup;
    @Bind(R.id.tv_file)
    TextView mTvFile;
    @Bind(R.id.image1)
    ImageView mImage1;
    @Bind(R.id.image2)
    ImageView mImage2;
    @Bind(R.id.image3)
    ImageView mImage3;
    @Bind(R.id.image4)
    ImageView mImage4;

    private FileSelectorDialog fileDialog;
    private int filterMode = FileSelectorDialog.FILTER_MODE.FILTER_ALL.ordinal();
    private String imageUrl = "http://pic22.nipic.com/20120621/1628220_155636709122_2.jpg";

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_file;
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected void init() {
        fileDialog = new FileSelectorDialog();
        fileDialog.setFilterMode(filterMode);
        fileDialog.setOnFileSelectorDialogListener(new FileSelectorDialog.OnFileSelectorDialogListener() {
            @SuppressLint("StringFormatMatches")
            @Override
            public void onFileSelectFinish(String path) {
                LogUtils.i("FileSelectorDialog: " + path);
                File file = new File(path);
                if (file.exists()) {
                    mTvFile.setText(String.format(getString(R.string.file_select_path), path));
                }
            }

            @Override
            public void onFileSelectCancel() {

            }
        });

        mRadiogroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_all:
                    filterMode = FileSelectorDialog.FILTER_MODE.FILTER_ALL.ordinal();
                    break;
                case R.id.rb_jpg:
                    filterMode = FileSelectorDialog.FILTER_MODE.FILTER_IMAGE.ordinal();
                    break;
                case R.id.rb_txt:
                    filterMode = FileSelectorDialog.FILTER_MODE.FILTER_TXT.ordinal();
                    break;
                case R.id.rb_mp3:
                    filterMode = FileSelectorDialog.FILTER_MODE.FILTER_AUDIO.ordinal();
                    break;
            }
        });

        ImageUtil.getInstance(mContext).load(imageUrl, mImage1);
        ImageUtil.getInstance(mContext).loadCenterCrop(imageUrl, mImage2);
        ImageUtil.getInstance(mContext).loadRound(imageUrl, mImage3);
        ImageUtil.getInstance(mContext).loadRadius(imageUrl, mImage4, 30);
    }

    @OnClick({R.id.bt_file_pick})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_file_pick:
                fileDialog.setFilterMode(filterMode);
                fileDialog.show(getFragmentManager(), getString(R.string.file_selector_file));
                break;
        }
    }

}
