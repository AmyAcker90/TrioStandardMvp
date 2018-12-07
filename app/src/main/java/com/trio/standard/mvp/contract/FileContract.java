package com.trio.standard.mvp.contract;

import com.trio.standard.bean.FileBean;
import com.trio.standard.module.base.BaseModel;
import com.trio.standard.module.base.BaseResp;
import com.trio.standard.module.base.BaseView;
import com.trio.standard.net.ProgressRequestBody;

import java.io.File;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by lixia on 2018/12/7.
 */

public interface FileContract {

    interface FileView extends BaseView {

        void uploadFilesDone(List<FileBean> data);

        void downloadFileDone(File file);

        void onProgress(int requestCode, int progress);
    }

    interface FileModel extends BaseModel {

        Call<BaseResp<List<FileBean>>> uploadFiles(ProgressRequestBody requestBody);

        Call<ResponseBody> downLoadFile(Long fileId, String filePath);

    }
}
