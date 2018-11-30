package com.trio.standard.module.base;

/**
 * Created by lixia on 2018/11/30.
 */

public interface OnResponseListener {

    void onSuccess(int requestCode, Object data);

    void onError(int requestCode, int ret, String msg);

    void onProgress(int requestCode, int progress);
}
