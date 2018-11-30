package com.trio.standard.model;

import android.content.Context;

import com.trio.standard.module.base.OnResponseListener;

/**
 * Created by lixia on 2018/11/30.
 */

public interface ShowListModel {

    void queryShowInfoList(Context context, String keyword, Long category, Long index, OnResponseListener listener);

    void queryCategotyAll(OnResponseListener listener);

}
