package com.trio.standard.model.impl;

import com.trio.standard.api.MusicApi;
import com.trio.standard.api.bean.MusicBean;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.model.MusicModel;
import com.trio.standard.module.base.BaseResp;
import com.trio.standard.module.base.OnResponseListener;
import com.trio.standard.net.BaseObserver;
import com.trio.standard.net.RetrofitFactory;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lixia on 2018/11/30.
 */

public class MusicModelImpl implements MusicModel {

    @Override
    public void searchMusic(String keyword, int index, OnResponseListener listener) {
        int requestCode = HttpConstant.searchMusicCode;
        RetrofitFactory.create(MusicApi.class)
                .searchMusicByKeyword(keyword, HttpConstant.default_page_size, index)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<MusicBean>>() {
                    @Override
                    protected void onSuccessResponse(List<MusicBean> result) {
                        listener.onSuccess(requestCode, result);
                    }

                    @Override
                    protected void onErrorResponse(int ret, String msg) {
                        listener.onError(requestCode, ret, msg);
                    }
                });
    }
}
