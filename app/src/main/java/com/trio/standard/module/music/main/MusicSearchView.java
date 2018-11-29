package com.trio.standard.module.music.main;

import com.trio.standard.api.bean.MusicBean;
import com.trio.standard.module.base.BaseView;

import java.util.List;

/**
 * Created by lixia on 2018/11/26.
 */

public interface MusicSearchView extends BaseView {

    void showData(List<MusicBean> data);

}
