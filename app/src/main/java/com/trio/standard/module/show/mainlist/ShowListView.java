package com.trio.standard.module.show.mainlist;

import com.trio.standard.api.bean.Category;
import com.trio.standard.api.bean.ShowInfo;
import com.trio.standard.module.base.BaseView;

import java.util.List;

/**
 * Created by lixia on 2018/11/26.
 */

public interface ShowListView extends BaseView {

    void showListData(List<ShowInfo> data);

    void categoryData(List<Category> data);
}
