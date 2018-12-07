package com.trio.standard.mvp.model;

import com.trio.standard.bean.FileBean;
import com.trio.standard.module.base.BaseResp;
import com.trio.standard.mvp.contract.FileContract;
import com.trio.standard.net.ProgressRequestBody;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by lixia on 2018/12/7.
 */

public class FileModel implements FileContract.FileModel {

    @Override
    public Call<BaseResp<List<FileBean>>> uploadFiles(ProgressRequestBody requestBody) {
        return mApiService.uploadFileWithRequestBody(requestBody);
    }

    @Override
    public Call<ResponseBody> downLoadFile(Long fileId, String filePath) {
        return mApiService.downloadFile(fileId);
    }

}
