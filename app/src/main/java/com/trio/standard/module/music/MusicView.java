package com.trio.standard.module.music;

import com.trio.standard.api.bean.MusicBean;
import com.trio.standard.module.base.BaseView;

import java.util.List;

/**
 * Created by lixia on 2018/11/30.
 */

public interface MusicView extends BaseView {

    void updateMusic(List<MusicBean> data);
}
