package com.trio.standard.module.show.mainlist;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.blankj.utilcode.util.NetworkUtils;
import com.trio.standard.api.ShowApi;
import com.trio.standard.api.bean.Category;
import com.trio.standard.api.bean.ShowInfo;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.net.BaseObserver;
import com.trio.standard.module.base.BasePresenter;
import com.trio.standard.module.base.BaseResp;
import com.trio.standard.net.RetrofitFactory;
import com.trio.standard.utils.CacheUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by lixia on 2018/11/26.
 */

class ShowListPresenter implements BasePresenter {

    private ShowListView mView;
    private List<ShowInfo> mCurrentData = new ArrayList<>();
    private boolean isQuery = false;
    private Context mContext;

    ShowListPresenter(Context context, ShowListView view) {
        mContext = context;
        mView = view;
    }

    void queryShowInfoList(String keyword, Long category, Long index) {
        if (NetworkUtils.isAvailableByPing()) {
            if (!isQuery) {
                mView.setRefreshing(true);
                isQuery = true;
                RetrofitFactory.create(ShowApi.class)
                        .queryShowList(keyword, category, HttpConstant.default_page_size, index)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(mView.bindToLife())
                        .subscribe(new BaseObserver<List<ShowInfo>>() {
                            @Override
                            protected void onSuccessResponse(List<ShowInfo> result) {
                                mView.setRefreshing(false);
                                if (index == 0) {
                                    mCurrentData.clear();
                                    JSONArray array = JSONArray.parseArray(JSON.toJSONString(result));
                                    CacheUtil.getInstance(mContext).putString(HttpConstant.queryShowInfoList,
                                            array.toString());
                                }
                                mCurrentData.addAll(result);
                                mView.showListData(mCurrentData);
                                mView.onBottom(result.size() < HttpConstant.default_page_size);
                                isQuery = false;
                            }

                            @Override
                            protected void onErrorResponse(int ret, String msg) {
                                mView.setRefreshing(false);
                                mView.onError(HttpConstant.queryShowInfoListCode, ret, msg);
                                mView.onBottom(ret == HttpConstant.error_no_data);
                                isQuery = false;
                            }
                        });
            } else
                mView.onError(0, HttpConstant.error_repetitive_operation, null);
        } else {
            mView.setRefreshing(false);
            mView.onError(0, HttpConstant.error_no_internet, null);
        }
    }

    void queryCategotyAll() {
        RetrofitFactory.create(ShowApi.class)
                .queryCategory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(mView.bindToLife())
                .subscribe(new Observer<BaseResp<List<Category>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BaseResp<List<Category>> listBaseResp) {
                        if (listBaseResp.getRet() == 200) {
                            List<Category> list = listBaseResp.getData();
                            Category category = new Category();
                            category.setCategoryId(null);
                            category.setName("All");
                            list.add(0, category);
                            mView.categoryData(list);
                        }
                    }
                });
    }
}
