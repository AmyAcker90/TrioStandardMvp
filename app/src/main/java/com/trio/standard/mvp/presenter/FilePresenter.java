package com.trio.standard.mvp.presenter;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.trio.standard.bean.FileBean;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.module.base.BasePresenter;
import com.trio.standard.module.base.BaseResp;
import com.trio.standard.mvp.contract.FileContract;
import com.trio.standard.mvp.model.FileModel;
import com.trio.standard.net.ApiEngine;
import com.trio.standard.net.ProgressRequestBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lixia on 2018/11/28.
 */

public class FilePresenter extends BasePresenter<FileContract.FileView, FileContract.FileModel> {
    private Thread mThread;

    public FilePresenter(FileContract.FileView view) {
        super(new FileModel(), view);
    }

    public void uploadFiles(List<String> paths) {
        int requestCode = HttpConstant.uploadFileBatchCode;
        getView().onProgress(requestCode, 0);
        ProgressRequestBody requestBody = new ProgressRequestBody(filesToMultipartBody(paths),
                (current, total, progress, done) -> {
                    getView().onProgress(requestCode, progress);
                });
        ApiEngine.getInstance(null).getApiService().uploadFileWithRequestBody(requestBody)
                .enqueue(new Callback<BaseResp<List<FileBean>>>() {
                    @Override
                    public void onResponse(Call<BaseResp<List<FileBean>>> call,
                                           Response<BaseResp<List<FileBean>>> response) {
                        BaseResp<List<FileBean>> body = response.body();
                        if (body.getRet() == HttpConstant.success)
                            getView().uploadFilesDone(body.getData());
                        else
                            getView().onError(requestCode, body.getRet(), body.getMsg());
                    }

                    @Override
                    public void onFailure(Call<BaseResp<List<FileBean>>> call, Throwable t) {
                        getView().onError(requestCode, HttpConstant.error_unknown, t.getMessage());
                    }
                });
    }

    public void downloadFile(Long fileId, String filePath) {
        int requestCode = HttpConstant.downloadFileCode;
        getView().onProgress(requestCode, 0);
        ApiEngine.getInstance((current, total, progress, done) -> {
            getView().onProgress(requestCode, progress);
        }).getApiService().downloadFile(fileId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //下载文件放在子线程
                    mThread = new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            //保存到本地
                            OutputStream os = null;
                            if (response.body() == null) {
                                getView().onError(requestCode, HttpConstant.error_unknown, response.message());
                                return;
                            }
                            LogUtils.i(response.headers().toString());
                            InputStream is = response.body().byteStream();
                            File file = new File(filePath + getHeaderFileName(response));
                            if (file.exists())
                                file.delete();
                            try {
                                os = new FileOutputStream(file);
                                int len;
                                byte[] buff = new byte[1024];
                                while ((len = is.read(buff)) != -1) {
                                    os.write(buff, 0, len);
                                }
                                getView().downloadFileDone(file);
                            } catch (IOException e) {
                                e.printStackTrace();
                                getView().onError(requestCode, HttpConstant.error_unknown, e.getMessage());
                            } finally {
                                if (os != null) {
                                    try {
                                        os.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        getView().onError(requestCode, HttpConstant.error_unknown, e.getMessage());
                                    }
                                }
                                if (is != null) {
                                    try {
                                        is.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        getView().onError(requestCode, HttpConstant.error_unknown, e.getMessage());
                                    }
                                }
                            }
                        }
                    };
                    mThread.start();
                } else
                    getView().onError(requestCode, HttpConstant.error_unknown, response.message());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogUtils.i("onFailure" + t.getMessage());
                getView().onError(requestCode, HttpConstant.error_unknown, t.getMessage());
            }
        });
    }

    /**
     * 解析文件头
     */
    private static String getHeaderFileName(Response response) {
        String disposition = response.headers().get("Content-Disposition");
        if (!TextUtils.isEmpty(disposition)) {
            disposition = disposition.replace("attachment;filename=", "");
            return disposition;
        }
        return "";
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
