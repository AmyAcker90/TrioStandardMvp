package com.trio.standard.module.base;

import com.trio.standard.net.ApiService;
import com.trio.standard.net.ApiEngine;
import com.trio.standard.net.ProgressListener;

/**
 * Created by lixia on 2018/12/6.
 */

public interface BaseModel {
    ApiService mApiService = ApiEngine.getInstance(null).getApiService();
}
