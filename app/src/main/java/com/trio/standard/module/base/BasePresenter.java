package com.trio.standard.module.base;

/**
 * Created by lixia on 2018/11/30.
 */
public abstract class BasePresenter<V extends BaseView> implements OnResponseListener {

    public V mView;

    public void attach(V view) {
        this.mView = view;
    }

    public void detach() {
        this.mView = null;
    }

    @Override
    public void onSuccess(int requestCode, Object data) {

    }

    @Override
    public void onError(int requestCode, int ret, String msg) {

    }

    @Override
    public void onProgress(int requestCode, int progress) {

    }

}
