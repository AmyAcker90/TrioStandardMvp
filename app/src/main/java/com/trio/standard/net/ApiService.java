package com.trio.standard.net;

import com.trio.standard.bean.Category;
import com.trio.standard.bean.FileBean;
import com.trio.standard.bean.MusicBean;
import com.trio.standard.bean.ShowInfo;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.module.base.BaseResp;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

/**
 * Created by lixia on 2018/12/6.
 */

public interface ApiService {

    @GET(HttpConstant.queryShowInfoList)
    Observable<BaseResp<List<ShowInfo>>> queryShowList(@Query(HttpConstant.keyword) String keyword,
                                                       @Query(HttpConstant.category) Long category,
                                                       @Query(HttpConstant.pageSize) int pageSize,
                                                       @Query(HttpConstant.index) long index);

    @GET(HttpConstant.queryCategoryList)
    Observable<BaseResp<List<Category>>> queryCategory();

    @POST(HttpConstant.pushOne)
    Observable<BaseResp<String>> pushOne(@Query(HttpConstant.clientId) String clientId,
                                         @Query(HttpConstant.title) String title,
                                         @Query(HttpConstant.content) String content);

    @GET(HttpConstant.searchMusic)
    Observable<BaseResp<List<MusicBean>>> searchMusicByKeyword(@Query(HttpConstant.keyword) String keyword,
                                                               @Query(HttpConstant.pageSize) int pageSize,
                                                               @Query(HttpConstant.index) int index);


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
