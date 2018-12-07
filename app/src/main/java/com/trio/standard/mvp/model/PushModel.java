package com.trio.standard.mvp.model;

import com.trio.standard.module.base.BaseResp;
import com.trio.standard.mvp.contract.PushContract;

import io.reactivex.Observable;

/**
 * Created by lixia on 2018/12/7.
 */

public class PushModel implements PushContract.PushModel {

    @Override
    public Observable<BaseResp<String>> pushOne(String clientId, String title, String content) {
        return mApiService.pushOne(clientId, title, content);
    }
}
