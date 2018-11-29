package com.trio.standard.api;

import com.trio.standard.constant.HttpConstant;
import com.trio.standard.module.base.BaseResp;

import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by lixia on 2018/11/28.
 */

public interface PushApi {

    @POST(HttpConstant.pushOne)
    Observable<BaseResp<String>> pushOne(@Query(HttpConstant.clientId) String clientId,
                                         @Query(HttpConstant.title) String title,
                                         @Query(HttpConstant.content) String content);

}
