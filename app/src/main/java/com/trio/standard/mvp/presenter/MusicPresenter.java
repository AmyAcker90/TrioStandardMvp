package com.trio.standard.mvp.presenter;

import com.trio.standard.bean.MusicBean;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.module.base.BasePresenter;
import com.trio.standard.mvp.contract.MusicContract;
import com.trio.standard.mvp.model.MusicModel;
import com.trio.standard.net.BaseObserver;
import com.trio.standard.net.RxTransformer;

import java.util.List;

/**
 * Created by lixia on 2018/11/30.
 */

public class MusicPresenter extends BasePresenter<MusicContract.MusicView, MusicContract.MusicModel> {

    public MusicPresenter(MusicContract.MusicView view) {
        super(new MusicModel(), view);
    }

    public void searchMusic(String keyword, int index) {
        getView().setRefreshing(true);
        mModel.searchMusic(keyword, HttpConstant.default_page_size, index)
                .compose(RxTransformer.transformWithLoading(getView()))
                .subscribe(new BaseObserver<List<MusicBean>>() {
                    @Override
                    protected void onSuccessResponse(List<MusicBean> result) {
                        getView().updateMusic(result);
                        getView().onBottom(result.size() < HttpConstant.default_page_size);
                        getView().setRefreshing(false);
                    }

                    @Override
                    protected void onErrorResponse(int ret, String msg) {
                        getView().onError(HttpConstant.searchMusicCode, ret, msg);
                        getView().setRefreshing(false);
                        if (ret == HttpConstant.error_no_data)
                            getView().onBottom(true);
                    }
                });
    }
}
