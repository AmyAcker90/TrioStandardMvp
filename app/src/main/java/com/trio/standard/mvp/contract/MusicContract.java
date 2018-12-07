package com.trio.standard.mvp.contract;

import com.trio.standard.bean.MusicBean;
import com.trio.standard.module.base.BaseModel;
import com.trio.standard.module.base.BaseResp;
import com.trio.standard.module.base.BaseView;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by lixia on 2018/12/7.
 */

public interface MusicContract {
    interface MusicView extends BaseView {

        void updateMusic(List<MusicBean> data);
    }

    interface MusicModel extends BaseModel {

        Observable<BaseResp<List<MusicBean>>> searchMusic(String keyword, int pageSize, int index);
    }
}
