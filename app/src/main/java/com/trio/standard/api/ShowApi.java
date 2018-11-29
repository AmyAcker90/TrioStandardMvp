package com.trio.standard.api;

import com.trio.standard.api.bean.Category;
import com.trio.standard.api.bean.ShowInfo;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.module.base.BaseResp;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by lixia on 2018/11/27.
 */

public interface ShowApi {

    @GET(HttpConstant.queryShowInfoList)
    Observable<BaseResp<List<ShowInfo>>> queryShowList(@Query(HttpConstant.keyword) String keyword,
                                                       @Query(HttpConstant.category) Long category,
                                                       @Query(HttpConstant.pageSize) int pageSize,
                                                       @Query(HttpConstant.index) long index);

    @GET(HttpConstant.queryCategoryList)
    Observable<BaseResp<List<Category>>> queryCategory();

}
