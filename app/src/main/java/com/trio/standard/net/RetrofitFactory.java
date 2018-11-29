package com.trio.standard.net;

import com.trio.standard.constant.HttpConstant;
import com.trio.standard.net.FastJson.FastJsonConverterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by lixia on 2018/11/22.
 */

public class RetrofitFactory {
    private static Retrofit retrofit;
    private static Interceptor mInterceptor;

    private static void init(ProgressListener listener) {
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
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(initClient())
                .build();
    }

    private static OkHttpClient initClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(mInterceptor)
                .addInterceptor(initLogInterceptor())
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(3, TimeUnit.SECONDS)
                .build();
    }

    private static HttpLoggingInterceptor initLogInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }


    public static <T> T create(Class<T> service) {
        init(null);
        return retrofit.create(service);
    }

    public static <T> T create(Class<T> service,ProgressListener listener) {
        init(listener);
        return retrofit.create(service);
    }

}
