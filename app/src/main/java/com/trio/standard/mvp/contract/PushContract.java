package com.trio.standard.mvp.contract;

import com.trio.standard.module.base.BaseModel;
import com.trio.standard.module.base.BaseResp;
import com.trio.standard.module.base.BaseView;

import io.reactivex.Observable;

/**
 * Created by lixia on 2018/12/7.
 */

public interface PushContract {
    interface PushView extends BaseView {
        void getOnePush();
    }

    interface PushModel extends BaseModel {

        Observable<BaseResp<String>> pushOne(String clientId, String title, String content);
    }
}
