package com.trio.standard.mvp.contract;

import android.content.Context;

import com.trio.standard.bean.Category;
import com.trio.standard.bean.ShowInfo;
import com.trio.standard.module.base.BaseModel;
import com.trio.standard.module.base.BaseResp;
import com.trio.standard.module.base.BaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by lixia on 2018/12/6.
 */

public interface ShowContract {
    interface ShowListView extends BaseView {

        void updateShows(List<ShowInfo> data);

        void updateCategorys(List<Category> data);
    }

    interface ShowListModel extends BaseModel {

        Observable<BaseResp<List<ShowInfo>>> queryShowInfoList(Context context, String keyword,
                                                               Long category,int pageSize, Long index);

        Observable<BaseResp<List<Category>>> queryCategotyAll();

    }
}
