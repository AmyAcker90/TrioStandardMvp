package com.trio.standard.mvp.model;

import com.trio.standard.bean.MusicBean;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.module.base.BaseResp;
import com.trio.standard.mvp.contract.MusicContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by lixia on 2018/12/7.
 */

public class MusicModel implements MusicContract.MusicModel {

    @Override
    public Observable<BaseResp<List<MusicBean>>> searchMusic(String keyword, int pageSize, int index) {
        return mApiService.searchMusicByKeyword(keyword, pageSize, index);
    }
}
