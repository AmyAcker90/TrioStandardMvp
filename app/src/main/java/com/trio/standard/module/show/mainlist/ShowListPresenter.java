package com.trio.standard.module.show.mainlist;

import android.content.Context;

import com.trio.standard.api.bean.Category;
import com.trio.standard.api.bean.ShowInfo;
import com.trio.standard.constant.HttpConstant;
import com.trio.standard.model.ShowListModel;
import com.trio.standard.model.impl.ShowListModelImpl;
import com.trio.standard.module.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixia on 2018/11/26.
 */

class ShowListPresenter extends BasePresenter<ShowListView> {

    private ShowListModel mModel;
    private Context mContext;

    ShowListPresenter(Context context, ShowListView view) {
        mContext = context;
        mView = view;
        mModel = new ShowListModelImpl();
    }

    void queryShowInfoList(String keyword, Long category, Long index) {
        mView.setRefreshing(true);
        mModel.queryShowInfoList(mContext, keyword, category, index, this);
    }

    void queryCategotyAll() {
        mModel.queryCategotyAll(this);
    }

    @Override
    public void onSuccess(int requestCode, Object data) {
        super.onSuccess(requestCode, data);
        switch (requestCode) {
            case HttpConstant.queryShowInfoListCode:
                List<ShowInfo> list = (List<ShowInfo>) data;
                mView.updateShows(list);
                mView.onBottom(list.size() < HttpConstant.default_page_size);
                mView.setRefreshing(false);
                break;
            case HttpConstant.queryCategoryListCode:
                mView.updateCategorys((List<Category>) data);
                break;
        }
    }

    @Override
    public void onError(int requestCode, int ret, String msg) {
        super.onError(requestCode, ret, msg);
        mView.showError(ret,msg);
        if (requestCode == HttpConstant.queryShowInfoListCode) {
            mView.setRefreshing(false);
            if (ret == HttpConstant.error_no_data)
                mView.onBottom(true);
        }
    }

}
