package com.trio.standard.model;

import com.trio.standard.module.base.OnResponseListener;

import java.util.List;

/**
 * Created by lixia on 2018/11/30.
 */

public interface FileModel {

    void uploadFiles(List<String> paths, OnResponseListener listener);

    void downLoadFile(Long fileId, String filePath, OnResponseListener listener);
}
