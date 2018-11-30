package com.trio.standard.module.music;

import com.trio.standard.api.bean.MusicBean;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.model.MusicModel;
import com.trio.standard.model.impl.MusicModelImpl;
import com.trio.standard.module.base.BasePresenter;

import java.util.List;

/**
 * Created by lixia on 2018/11/30.
 */

public class MusicPresenter extends BasePresenter<MusicView> {

    private MusicModel mModel;

    public MusicPresenter(MusicView view) {
        mView = view;
        mModel = new MusicModelImpl();
    }

    void searchMusic(String keyword, int index) {
        mView.setRefreshing(true);
        mModel.searchMusic(keyword, index, this);
    }

    @Override
    public void onSuccess(int requestCode, Object data) {
        super.onSuccess(requestCode, data);
        List<MusicBean> list = (List<MusicBean>) data;
        mView.updateMusic(list);
        mView.onBottom(list.size() < HttpConstant.default_page_size);
        mView.setRefreshing(false);
    }

    @Override
    public void onError(int requestCode, int ret, String msg) {
        super.onError(requestCode, ret, msg);
        mView.showError(ret, msg);
        mView.setRefreshing(false);
        if (ret == HttpConstant.error_no_data)
            mView.onBottom(true);
    }
}
