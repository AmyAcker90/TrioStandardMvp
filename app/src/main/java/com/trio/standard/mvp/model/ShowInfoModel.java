package com.trio.standard.mvp.model;

import android.content.Context;

import com.trio.standard.bean.Category;
import com.trio.standard.bean.ShowInfo;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.module.base.BaseResp;
import com.trio.standard.mvp.contract.ShowContract;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by lixia on 2018/11/30.
 */

public class ShowInfoModel implements ShowContract.ShowListModel {

    @Override
    public Observable<BaseResp<List<ShowInfo>>> queryShowInfoList(Context context, String keyword, Long category,
                                                                  int pageSize, Long index) {
        return mApiService.queryShowList(keyword, category, pageSize, index);
    }

    @Override
    public Observable<BaseResp<List<Category>>> queryCategotyAll() {
        return mApiService.queryCategory();
    }
}
