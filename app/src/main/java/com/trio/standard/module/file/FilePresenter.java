package com.trio.standard.module.file;

import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.trio.standard.api.FileApi;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.module.base.BasePresenter;
import com.trio.standard.net.RetrofitFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lixia on 2018/11/29.
 */

class FilePresenter implements BasePresenter<FileView> {

    private FileView mView;
    private Thread mThread;

    FilePresenter(FileView view) {
        mView = view;
    }

    void downloadFile(Long fileId, String filepath) {
        RetrofitFactory.create(FileApi.class, (current, total, progress, done) -> {
            LogUtils.i("download current: " + current + "  total: " + total);
        }).downloadFile(fileId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    //下载文件放在子线程
                    mThread = new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            //保存到本地
                            writeFile2Disk(response, filepath);
                        }
                    };
                    mThread.start();
                } else
                    mView.onError(HttpConstant.downloadFileCode, HttpConstant.error_unknown, response.message());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                LogUtils.i("onFailure" + t.getMessage());
                mView.onError(HttpConstant.downloadFileCode, HttpConstant.error_unknown, t.getMessage());
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

    private void writeFile2Disk(Response<ResponseBody> response, String filepath) {
        OutputStream os = null;
        if (response.body() == null) {
            return;
        }
        LogUtils.i(response.headers().toString());
        InputStream is = response.body().byteStream();
        File file = new File(filepath + getHeaderFileName(response));
        if (file.exists())
            file.delete();
        try {
            os = new FileOutputStream(file);
            int len;
            byte[] buff = new byte[1024];
            while ((len = is.read(buff)) != -1) {
                os.write(buff, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
