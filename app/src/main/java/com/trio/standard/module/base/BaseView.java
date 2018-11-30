package com.trio.standard.module.base;

/**
 * Created by lixia on 2018/11/30.
 */

public interface BaseView {

    void showLoading(String msg, boolean cancelable);

    void hideLoading();

    void showError(int errorCode, String msg);

    void setRefreshing(boolean isRefreshing);

    void onBottom(boolean isBottom);

}
