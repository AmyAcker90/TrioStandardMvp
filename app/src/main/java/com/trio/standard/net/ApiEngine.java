package com.trio.standard.net;

import com.trio.standard.constant.HttpConstant;
import com.trio.standard.net.FastJson.FastJsonConverterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by lixia on 2018/11/22.
 */

public class ApiEngine {

    private Retrofit retrofit;
    private static Interceptor mInterceptor;
    private volatile static ApiEngine apiEngine;
    private static final long DEFAULT_TIMEOUT = 5;

    private ApiEngine(ProgressListener listener) {
        if (listener != null) {
            mInterceptor = new DownloadProgressInterceptor(listener);
        } else {
            mInterceptor = chain -> {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("charset", "UTF-8")
                        .build();
                return chain.proceed(request);
            };
        }
        retrofit = new Retrofit.Builder()
                .baseUrl(HttpConstant.url)
                .addConverterFactory(FastJsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(initClient())
                .build();

    }

    private OkHttpClient initClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(mInterceptor)
                .addInterceptor(initLogInterceptor())
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    private HttpLoggingInterceptor initLogInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    public static ApiEngine getInstance(ProgressListener listener) {
        if (apiEngine == null) {
            synchronized (ApiEngine.class) {
                if (apiEngine == null) {
                    apiEngine = new ApiEngine(listener);
                }
            }
        }
        return apiEngine;
    }

    public ApiService getApiService() {
        return retrofit.create(ApiService.class);
    }

}
