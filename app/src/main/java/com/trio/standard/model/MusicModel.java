package com.trio.standard.model;

import com.trio.standard.module.base.OnResponseListener;

/**
 * Created by lixia on 2018/11/30.
 */

public interface MusicModel {

    void searchMusic(String keyword, int index, OnResponseListener listener);
}
