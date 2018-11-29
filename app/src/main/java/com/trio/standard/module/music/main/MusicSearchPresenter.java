package com.trio.standard.module.music.main;

import com.trio.standard.api.MusicApi;
import com.trio.standard.api.bean.MusicBean;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.module.base.BasePresenter;
import com.trio.standard.module.base.BaseResp;
import com.trio.standard.net.RetrofitFactory;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lixia on 2018/11/26.
 */

class MusicSearchPresenter implements BasePresenter {

    private MusicSearchView mView;
    private List<MusicBean> mCurrentData = new ArrayList<>();

    MusicSearchPresenter(MusicSearchView view) {
        mView = view;
    }

    void loadDataByKeyword(String keyword, int index) {
        RetrofitFactory.create(MusicApi.class)
                .searchMusicByKeyword(keyword, HttpConstant.default_page_size, index)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mView.bindToLife())
                .subscribe(new Observer<BaseResp<List<MusicBean>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mView.onError(HttpConstant.searchMusicCode, HttpConstant.error_unknown, e.getMessage());
                    }

                    @Override
                    public void onNext(BaseResp<List<MusicBean>> listBaseResp) {
                        if (listBaseResp.getRet() == 200) {
                            if (index == 0)
                                mCurrentData.clear();
                            mCurrentData.addAll(listBaseResp.getData());
                            mView.showData(mCurrentData);
                        } else
                            mView.onError(HttpConstant.searchMusicCode, listBaseResp.getRet(), listBaseResp.getMsg());
                    }
                });
    }

}
