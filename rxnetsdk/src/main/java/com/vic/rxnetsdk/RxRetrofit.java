package com.vic.rxnetsdk;

import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.Map;

import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.schedulers.Schedulers;

/**
 * Created by liu song on 2017/5/3.
 */

public class RxRetrofit {

    private static final Map<Class<?>, Object> SERVICE_MAP = new ArrayMap<>();
    private volatile static RxRetrofit INSTANCE;
    private static Retrofit RETROFIT;

    private RxRetrofit() {
        initRetrofit();
    }

    public static RxRetrofit getInstance() {
        if (INSTANCE == null) {
            synchronized (RxRetrofit.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RxRetrofit();
                }
            }
        }
        return INSTANCE;
    }

    private static void initRetrofit() {
        CookieHandler cookieHandler = new CookieManager();
        OkHttpClient okhttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .cookieJar(new JavaNetCookieJar(cookieHandler))
                .build();
        RETROFIT = new Retrofit.Builder()
                .baseUrl("http://www.baidu.com/")
                .client(okhttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

    public <T> T create(@NonNull Class<T> service) {
        Object o = SERVICE_MAP.get(service);
        if (o != null) {
            return (T) o;
        } else {
            T t = RETROFIT.create(service);
            SERVICE_MAP.put(service, t);
            return t;
        }
    }
}
