package com.trio.standard.net;

/**
 * Created by lixia on 2018/11/28.
 */

public interface XLoadingCallback {

    void onProgress(int progress);

    void onSuccess(Object result);

    void onFail(String result);
}
