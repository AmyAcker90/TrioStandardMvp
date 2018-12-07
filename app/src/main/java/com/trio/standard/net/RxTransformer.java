package com.trio.standard.net;

import com.trio.standard.module.base.BaseView;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lixia on 2018/12/7.
 */

public class RxTransformer {
    /**
     * 界面请求，需要加载和隐藏loading时调用,使用RxLifeCycle
     * 传入view接口，Activity，Fragment等实现了view接口，Activity，Fragment继承于{@link com.trello.rxlifecycle2.components.support.RxAppCompatActivity}
     * 也就实现了bindToLifecycle方法
     * @param view View
     * @param <T> 泛型
     * @return
     */
    public static <T> ObservableTransformer<T, T> transformWithLoading(final BaseView view) {
        //隐藏进度条
        return observable -> observable.subscribeOn(Schedulers.io())
                .doOnSubscribe(disposable -> {
                    view.showLoading();//显示进度条
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally(view::hideLoading).compose(view.bindToLifecycle());
    }

}
