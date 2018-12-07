package com.trio.standard.module.base;

import android.app.Activity;
import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.trello.rxlifecycle2.RxLifecycle;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by lixia on 2018/11/30.
 */
public abstract class BasePresenter<V extends BaseView, M extends BaseModel> implements IPresenter<V>{

    public Reference<V> mView;
    public M mModel;
    protected CompositeDisposable mCompositeDisposable;

    protected V getView() {
        return mView.get();
    }

    @Override
    public void attach(V view) {
        this.mView = new WeakReference<>(view);
    }

    public BasePresenter(M model, V view) {
        attach(view);
        mModel = model;
    }

    @Override
    public void detach() {
        this.mView = null;
        unDispose();//备用方案
    }

    /**
     * 将 {@link Disposable} 添加到 {@link CompositeDisposable} 中统一管理
     * 可在 {@link Activity#onDestroy()} 中使用 {@link #unDispose()} 停止正在执行的 RxJava 任务,避免内存泄漏
     * 目前已使用 {@link RxLifecycle} 避免内存泄漏,此方法作为备用方案
     *
     * @param disposable
     */
    protected void addDisposabel(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        //将所有 Disposable 放入集中处理
        mCompositeDisposable.add(disposable);
        Log.d("订阅", mCompositeDisposable.toString() + "个数" + mCompositeDisposable.size());
    }

    public void unDispose() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.clear();//保证 Activity 结束时取消所有正在执行的订阅
        }
    }

}
