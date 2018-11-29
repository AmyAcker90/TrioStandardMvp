package com.trio.standard.net;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * Created by lixia on 2018/11/29.
 */

public class DownloadProgressInterceptor implements Interceptor {

    private ProgressListener mListener;

    public DownloadProgressInterceptor(ProgressListener listener) {
        mListener = listener;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        return originalResponse.newBuilder()
                .body(new ProgressResponseBody(originalResponse.body(), (current, total, progress, done) -> {
                    if (mListener != null)
                        mListener.onProgress(current, total, progress, done);
                })).build();
    }
}
