package com.trio.standard.mvp.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.trio.standard.bean.Category;
import com.trio.standard.bean.ShowInfo;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.module.base.BasePresenter;
import com.trio.standard.mvp.contract.ShowContract;
import com.trio.standard.mvp.model.ShowInfoModel;
import com.trio.standard.net.BaseObserver;
import com.trio.standard.net.RxTransformer;
import com.trio.standard.utils.CacheUtil;

import java.util.List;

/**
 * Created by lixia on 2018/11/26.
 */

public class ShowListPresenter extends BasePresenter<ShowContract.ShowListView, ShowContract.ShowListModel> {

    private Context mContext;

    public ShowListPresenter(Context context, ShowContract.ShowListView view) {
        super(new ShowInfoModel(), view);
        mContext = context;
    }

    public void queryShowInfoList(String keyword, Long category, Long index) {
        getView().setRefreshing(true);
        mModel.queryShowInfoList(mContext, keyword, category, HttpConstant.default_page_size, index)
                .compose(RxTransformer.transformWithLoading(getView()))
                .subscribe(new BaseObserver<List<ShowInfo>>() {
                    @Override
                    protected void onSuccessResponse(List<ShowInfo> result) {
                        if (index == 0 && TextUtils.isEmpty(keyword)) {
                            JSONArray array = JSONArray.parseArray(JSON.toJSONString(result));
                            CacheUtil.getInstance(mContext).putString(HttpConstant.queryShowInfoList,
                                    array.toString());
                        }
                        getView().updateShows(result);
                        getView().onBottom(result.size() < HttpConstant.default_page_size);
                        getView().setRefreshing(false);
                    }

                    @Override
                    protected void onErrorResponse(int ret, String msg) {
                        getView().onError(HttpConstant.queryShowInfoListCode, ret, msg);
                        getView().setRefreshing(false);
                        if (ret == HttpConstant.error_no_data)
                            getView().onBottom(true);
                    }
                });
    }

    public void queryCategotyAll() {
        mModel.queryCategotyAll()
                .compose(RxTransformer.transformWithLoading(getView()))
                .subscribe(new BaseObserver<List<Category>>() {
                    @Override
                    protected void onSuccessResponse(List<Category> result) {
                        Category category = new Category();
                        category.setCategoryId(null);
                        category.setName("All");
                        result.add(0, category);
                        getView().updateCategorys(result);
                    }

                    @Override
                    protected void onErrorResponse(int ret, String msg) {

                    }
                });
    }

}
