package com.trio.standard.net;

import com.trio.standard.constant.HttpConstant;
import com.trio.standard.module.base.BaseResp;

import rx.Observer;

/**
 * Created by lixia on 2018/11/27.
 */

public abstract class BaseObserver<T> implements Observer<BaseResp<T>> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        onErrorResponse(HttpConstant.error_unknown, e.getMessage());
    }

    @Override
    public void onNext(BaseResp<T> tBaseResp) {
        if (tBaseResp.getRet() == HttpConstant.success) {
            onSuccessResponse(tBaseResp.getData());
        } else {
            onErrorResponse(tBaseResp.getRet(), tBaseResp.getMsg());
        }
    }

    protected abstract void onSuccessResponse(T result);

    protected abstract void onErrorResponse(int ret, String msg);
}
