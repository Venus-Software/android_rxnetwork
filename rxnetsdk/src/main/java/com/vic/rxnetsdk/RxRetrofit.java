package com.vic.rxnetsdk;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.text.TextUtils;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
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
    private static final CookieHandler cookieHandler = new CookieManager();
    private volatile static RxRetrofit INSTANCE;
    private static Retrofit RETROFIT;
    private static OkHttpClient okhttpClient;
    private static String baseUrl;

    private RxRetrofit() {
    }

    public static RxRetrofit initInstance() {
        if (INSTANCE == null) {
            synchronized (RxRetrofit.class) {
                if (INSTANCE == null) {
                    INSTANCE = new RxRetrofit();
                }
            }
        }
        return INSTANCE;
    }

    public static RxRetrofit getInstance() {
        if (INSTANCE == null) {
            throw new NullPointerException("please invoke initInstance to initialize RxRetrofit，perfectly in application");
        }
        return INSTANCE;
    }

    /**
     * 初始化Retrofit
     */
    private static void initRetrofit() {
        RETROFIT = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okhttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public RxRetrofit baseUrl(@NonNull String baseUrl) {
        if (TextUtils.isEmpty(baseUrl)) {
            throw new IllegalArgumentException("baseUrl cannot be empty");
        }
        setBaseUrl(baseUrl);
        return INSTANCE;
    }

    /**
     * 是否在Chrome浏览器中抓取请求信息
     * @param flag
     * @return
     */
    public RxRetrofit showRequestInChrome(Context context, boolean flag) {
        if(flag){
            Stetho.initializeWithDefaults(context);
        }
        return INSTANCE;
    }

    public void initialize() {
        if (TextUtils.isEmpty(baseUrl)) {
            throw new IllegalArgumentException("please invoke initialize baseUrl before invoke initialize");
        }
        if (okhttpClient == null) {
            throw new NullPointerException("please invoke initOkhttpClient before invoke initialize");
        }
        initRetrofit();
    }

    /**
     * 初始化okhttpClient
     * @return
     */
    public RxRetrofit initOkhttpClient() {
        okhttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .cookieJar(new JavaNetCookieJar(cookieHandler)) //自动管理cookie
                .build();
        return INSTANCE;
    }

    /**
     * 请求再处理
     * @param interceptor
     * @return
     */
    public RxRetrofit addReprocessRequestInterceptor(@NonNull Interceptor interceptor) {
        if (okhttpClient == null) {
            throw new NullPointerException("please invoke initOkhttpClient before invoke addReprocessRequestInterceptor");
        }
        okhttpClient.newBuilder().addInterceptor(interceptor).build();
        return INSTANCE;
    }

    /**
     * 设置超时时间
     * @param read
     * @param write
     * @param connect
     * @return
     */
    public RxRetrofit timeOut(int read,int write,int connect) {
        if (okhttpClient == null) {
            throw new NullPointerException("please invoke initOkhttpClient before invoke timeOut");
        }
        okhttpClient.newBuilder()
                .readTimeout(read, TimeUnit.SECONDS)
                .writeTimeout(write,TimeUnit.SECONDS)
                .connectTimeout(connect,TimeUnit.SECONDS)
                .build();
        return INSTANCE;
    }

    /**
     * 创建接口api
     * @param service
     * @param <T>
     * @return
     */
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
