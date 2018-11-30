package com.trio.standard.module.image;

import com.trio.standard.api.bean.FileBean;
import com.trio.standard.module.base.BaseView;

import java.io.File;
import java.util.List;

/**
 * Created by lixia on 2018/11/29.
 */

public interface FileView extends BaseView {

    void uploadFilesDone(List<FileBean> data);

    void downloadFileDone(File file);

    void onProgress(int requestCode, int progress);
}
