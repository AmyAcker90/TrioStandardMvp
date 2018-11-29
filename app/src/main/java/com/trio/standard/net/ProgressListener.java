package com.trio.standard.net;

/**
 * Created by lixia on 2018/11/28.
 */

public interface ProgressListener {
    /**
     * @param current 已经读取的字节数
     * @param total   响应总长度
     * @param done    是否读取完毕
     */
    void onProgress(long current, long total, int progress, boolean done);
}
