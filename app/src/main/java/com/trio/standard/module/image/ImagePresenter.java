package com.trio.standard.module.image;

import com.blankj.utilcode.util.LogUtils;
import com.trio.standard.api.FileApi;
import com.trio.standard.api.bean.FileBean;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.module.base.BasePresenter;
import com.trio.standard.module.base.BaseResp;
import com.trio.standard.module.base.BaseView;
import com.trio.standard.net.ProgressRequestBody;
import com.trio.standard.net.RetrofitFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lixia on 2018/11/28.
 */

class ImagePresenter implements BasePresenter {

    private ImageMainView mView;

    ImagePresenter(ImageMainView view) {
        mView = view;
    }

    void uploadFiles(List<String> paths) {
        ProgressRequestBody requestBody = new ProgressRequestBody(filesToMultipartBody(paths),
                (current, total, progress, done) -> {
                    mView.onProgress(HttpConstant.uploadFileBatchCode, progress);
                });
        RetrofitFactory.create(FileApi.class).uploadFileWithRequestBody(requestBody)
                .enqueue(new Callback<BaseResp<List<FileBean>>>() {
                    @Override
                    public void onResponse(Call<BaseResp<List<FileBean>>> call, Response<BaseResp<List<FileBean>>>
                            response) {
                        BaseResp<List<FileBean>> body = response.body();
                        if (body.getRet() == HttpConstant.success)
                            mView.loadingDone(body.getData());
                        else
                            mView.onError(HttpConstant.uploadFileBatchCode, body.getRet(), body.getMsg());
                        LogUtils.i(body.getRet() + " upload: " + body.getData().toString());
                    }

                    @Override
                    public void onFailure(Call<BaseResp<List<FileBean>>> call, Throwable t) {
                        LogUtils.i("onFailure" + t.getMessage());
                        mView.onError(HttpConstant.uploadFileBatchCode, HttpConstant.error_unknown, t.getMessage());
                    }
                });
    }

    private static MultipartBody filesToMultipartBody(List<String> paths) {
        List<File> files = new ArrayList<>();
        for (String path : paths)
            files.add(new File(path));
        MediaType MutilPart_Form_Data = MediaType.parse("multipart/form-data; charset=utf-8");
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        //循环添加文件
        for (int i = 0; i < files.size(); i++)
            requestBodyBuilder.addFormDataPart(HttpConstant.file,
                    files.get(i).getName(), RequestBody.create(MutilPart_Form_Data, files.get(i)));
        return requestBodyBuilder.build();
    }
}
