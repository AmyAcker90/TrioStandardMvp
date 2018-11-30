package com.trio.standard.module.image;

import com.trio.standard.api.bean.FileBean;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.model.FileModel;
import com.trio.standard.model.impl.FileModelImpl;
import com.trio.standard.module.base.BasePresenter;

import java.io.File;
import java.util.List;

/**
 * Created by lixia on 2018/11/28.
 */

class FilePresenter extends BasePresenter<FileView> {

    private FileModel mModel;

    FilePresenter(FileView view) {
        mView = view;
        mModel = new FileModelImpl();
    }

    void uploadFiles(List<String> paths) {
        mModel.uploadFiles(paths, this);
    }

    void downloadFile(Long fileId, String filePath) {
        mModel.downLoadFile(fileId, filePath, this);
    }

    @Override
    public void onSuccess(int requestCode, Object data) {
        super.onSuccess(requestCode, data);
        if (requestCode == HttpConstant.uploadFileBatchCode)
            mView.uploadFilesDone((List<FileBean>) data);
        else if (requestCode == HttpConstant.downloadFileCode)
            mView.downloadFileDone((File) data);
    }

    @Override
    public void onProgress(int requestCode, int progress) {
        super.onProgress(requestCode, progress);
        mView.onProgress(requestCode, progress);
    }
}
