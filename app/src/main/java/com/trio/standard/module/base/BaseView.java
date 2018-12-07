package com.trio.standard.module.base;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * Created by lixia on 2018/11/30.
 */

public interface BaseView {

    void showLoading();

    void hideLoading();

    void onError(int requestCode, int errorCode, String msg);

    void setRefreshing(boolean isRefreshing);

    void onBottom(boolean isBottom);

    <T> LifecycleTransformer<T> bindToLifecycle();
}
