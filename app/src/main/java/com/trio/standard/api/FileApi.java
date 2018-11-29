package com.trio.standard.api;

import com.trio.standard.api.bean.FileBean;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.module.base.BaseResp;
import com.trio.standard.net.ProgressRequestBody;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

/**
 * Created by lixia on 2018/11/28.
 */

public interface FileApi {

    /**
     * 通过 MultipartBody和@body作为参数来上传
     * 注意：对于大文件的操作一定要加@Streaming，否则会出现OOM
     *
     * @param requestBody MultipartBody包含多个Part
     * @return 状态信息
     */
    @Streaming
    @POST(HttpConstant.uploadFileBatch)
    Call<BaseResp<List<FileBean>>> uploadFileWithRequestBody(@Body ProgressRequestBody requestBody);

    @Streaming
    @GET(HttpConstant.downloadFile)
    Call<ResponseBody> downloadFile(@Query(HttpConstant.fileId) Long fileId);

}
