package com.trio.standard.model.impl;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.trio.standard.api.ShowApi;
import com.trio.standard.api.bean.Category;
import com.trio.standard.api.bean.ShowInfo;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.model.ShowListModel;
import com.trio.standard.module.base.OnResponseListener;
import com.trio.standard.net.BaseObserver;
import com.trio.standard.net.RetrofitFactory;
import com.trio.standard.utils.CacheUtil;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lixia on 2018/11/30.
 */

public class ShowListModelImpl implements ShowListModel {

    @Override
    public void queryShowInfoList(Context context, String keyword, Long category, Long index,
                                  OnResponseListener listener) {
        int requestCode = HttpConstant.queryShowInfoListCode;
        RetrofitFactory.create(ShowApi.class)
                .queryShowList(keyword, category, HttpConstant.default_page_size, index)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<ShowInfo>>() {
                    @Override
                    protected void onSuccessResponse(List<ShowInfo> result) {
                        if (index == 0) {
                            JSONArray array = JSONArray.parseArray(JSON.toJSONString(result));
                            CacheUtil.getInstance(context).putString(HttpConstant.queryShowInfoList,
                                    array.toString());
                        }
                        listener.onSuccess(requestCode, result);
                    }

                    @Override
                    protected void onErrorResponse(int ret, String msg) {
                        listener.onError(requestCode, ret, msg);
                    }
                });
    }

    @Override
    public void queryCategotyAll(OnResponseListener listener) {
        int requestCode = HttpConstant.queryCategoryListCode;
        RetrofitFactory.create(ShowApi.class)
                .queryCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<Category>>() {
                    @Override
                    protected void onSuccessResponse(List<Category> result) {
                        Category category = new Category();
                        category.setCategoryId(null);
                        category.setName("All");
                        result.add(0, category);
                        listener.onSuccess(requestCode, result);
                    }

                    @Override
                    protected void onErrorResponse(int ret, String msg) {
                        listener.onError(requestCode, ret, msg);
                    }
                });
    }
}
