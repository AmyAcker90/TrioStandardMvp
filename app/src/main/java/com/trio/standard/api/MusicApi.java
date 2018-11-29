package com.trio.standard.api;

import com.trio.standard.api.bean.MusicBean;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.module.base.BaseResp;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by lixia on 2018/11/26.
 */

public interface MusicApi {

    @GET(HttpConstant.searchMusic)
    Observable<BaseResp<List<MusicBean>>> searchMusicByKeyword(@Query(HttpConstant.keyword) String keyword,
                                                               @Query(HttpConstant.pageSize) int pageSize,
                                                               @Query(HttpConstant.index) int index);

}
