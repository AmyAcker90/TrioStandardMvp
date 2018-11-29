package com.trio.standard.module.image;

import com.trio.standard.api.bean.FileBean;
import com.trio.standard.module.base.BaseView;

import java.util.List;

/**
 * Created by lixia on 2018/11/29.
 */

public interface ImageMainView extends BaseView {

    void loadingDone(List<FileBean> data);
}
