package com.trio.standard.module.file;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.trio.standard.R;
import com.trio.standard.adapter.FileSelectAdapter;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.module.base.BaseActivity;
import com.trio.standard.widgets.FileSelectorDialog;
import com.trio.standard.widgets.ProgressButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class FileActivity extends BaseActivity<FilePresenter> implements FileView {

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
    @Bind(R.id.progress_bt_download)
    ProgressButton mProgressBtDownload;

    private FileSelectorDialog fileDialog;
    private int filterMode = FileSelectorDialog.FILTER_MODE.FILTER_ALL.ordinal();

    @Override
    protected int attachLayoutRes() {
        return R.layout.activity_file;
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
        mPresenter = new FilePresenter(this);
        mProgressBtDownload.setState(ProgressButton.STATE.NORMAL, getString(R.string.toast_download_file));
        mProgressBtDownload.setOnClickListener(v ->
                mPresenter.downloadFile(145L, HttpConstant.app_file_download));
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
