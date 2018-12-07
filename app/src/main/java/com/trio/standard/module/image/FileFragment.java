package com.trio.standard.module.image;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.blankj.utilcode.util.LogUtils;
import com.trio.standard.R;
import com.trio.standard.bean.FileBean;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.module.base.BaseMvpFragment;
import com.trio.standard.mvp.contract.FileContract;
import com.trio.standard.mvp.presenter.FilePresenter;
import com.trio.standard.widgets.ImagePicker;
import com.trio.standard.widgets.ProgressButton;
import com.zhihu.matisse.Matisse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;
import static com.trio.standard.widgets.ImagePicker.REQUEST_CODE_CHOOSE;

/**
 * Created by lixia on 2018/11/28.
 */

public class FileFragment extends BaseMvpFragment<FilePresenter>
        implements FileContract.FileView {

    @Bind(R.id.rb_one)
    RadioButton mRbOne;
    @Bind(R.id.rb_nine)
    RadioButton mRbNine;
    @Bind(R.id.radiogroup)
    RadioGroup mRadiogroup;
    @Bind(R.id.imagepicker)
    ImagePicker mImagepicker;
    @Bind(R.id.pb_upload)
    ProgressButton mProgressBtUpload;
    @Bind(R.id.btn_file)
    Button mBtnFile;
    @Bind(R.id.pb_download)
    ProgressButton mProgressBtDownload;

    @Override
    protected int attachLayoutRes() {
        return R.layout.fragment_image;
    }

    @Override
    protected FilePresenter createPresenter() {
        return new FilePresenter(this);
    }

    @Override
    protected void init() {
        mImagepicker.setNumColumns(3);
        //图片选择器最多可以选择的图片数量
        mImagepicker.setMaxSelectable(1);

        mRadiogroup.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_one:
                    mImagepicker.setMaxSelectable(1);
                    mImagepicker.setPaths(new ArrayList<String>());
                    break;
                case R.id.rb_nine:
                    mImagepicker.setMaxSelectable(9);
                    mImagepicker.setPaths(new ArrayList<String>());
                    break;
            }
        });

        mPresenter = new FilePresenter(this);
        mProgressBtUpload.setState(ProgressButton.STATE.NORMAL, getString(R.string.toast_upload_file));
        mProgressBtDownload.setState(ProgressButton.STATE.NORMAL, getString(R.string.toast_download_file));
    }

    public void onProgress(int requestCode, int progress) {
        LogUtils.i(requestCode + " progress: " + progress);
        switch (requestCode) {
            case HttpConstant.uploadFileBatchCode:
                mProgressBtUpload.setState(ProgressButton.STATE.PERCENT, null);
                mProgressBtUpload.setProgress(progress);
                break;
            case HttpConstant.downloadFileCode:
                mProgressBtDownload.setState(ProgressButton.STATE.PERCENT, null);
                mProgressBtDownload.setProgress(progress);
                break;
        }
    }

    @Override
    public void uploadFilesDone(List<FileBean> data) {
        mProgressBtUpload.setState(ProgressButton.STATE.NORMAL, getString(R.string.toast_upload_finish));
    }

    @Override
    public void downloadFileDone(File file) {
        mProgressBtDownload.setState(ProgressButton.STATE.NORMAL, getString(R.string.toast_download_finish));
    }

    @OnClick({R.id.pb_upload, R.id.pb_download, R.id.btn_file})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pb_upload:
                List<String> result = mImagepicker.getPaths();
                if (result.size() > 0) {
                    mPresenter.uploadFiles(result);
                } else
                    showToast(getString(R.string.error_image_num));
                break;
            case R.id.pb_download:
                mPresenter.downloadFile(155L, HttpConstant.app_file_download);
                break;
            case R.id.btn_file:
                Intent intent = new Intent(mContext, FilePickActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE_CHOOSE) {
                List<Uri> mSelected = Matisse.obtainResult(data);
                List<String> paths = Matisse.obtainPathResult(data);
                mImagepicker.setPaths(paths);
                for (String s : paths) {
                    //更新图库资源
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    File f = new File(s);
                    Uri contentUri = Uri.fromFile(f);
                    mediaScanIntent.setData(contentUri);
                    getActivity().sendBroadcast(mediaScanIntent);
                }
            }
        }
    }
}
