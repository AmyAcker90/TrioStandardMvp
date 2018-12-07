package com.trio.standard.module.base;

public interface IPresenter<V extends BaseView> {

    void attach(V view);

    void detach();
}
