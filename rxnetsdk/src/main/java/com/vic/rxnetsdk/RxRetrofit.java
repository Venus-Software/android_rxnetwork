/*
 * Copyright (C) 2017 Venus Software(VIC), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.schedulers.Schedulers;

/**
 * RxRetrofit
 * 提供对Retrofit, RxJava, OkHttp的封装
 * Example:
 * <pre class="prettyprint">
 *      RxRetrofit.initInstance()
 *           .showRequestInChrome(this, true)
 *           .baseUrl("http://www.baidu.com/")
 *           .initOkhttpClient()
 *           .timeOut(60, 60, 10)
 *           .addReprocessRequestInterceptor(new Interceptor() {
 *               @Override public Response intercept(Chain chain) throws IOException {
 *                  Request request = chain.request();
 *                  return chain.proceed(request);
 *               }
 *           })
 *           .addConverterFactory(GsonConverterFactory.create())
 *           .initialize();
 * </pre>
 */
public class RxRetrofit {
    private static final Map<Class<?>, Object> SERVICE_MAP = new ArrayMap<>();
    private static final CookieHandler cookieHandler = new CookieManager();
    private volatile static RxRetrofit INSTANCE;
    private static Retrofit RETROFIT;
    private static OkHttpClient okhttpClient;
    private static String baseUrl;
    private static Converter.Factory converterFactory;

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
                .addConverterFactory(converterFactory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();
    }

    /**
     * 添加数据转换工厂
     * GsonConverterFactory.create()->Gson转bean对象
     * ScalarsConverterFactory.create()->返回String
     * 必传项
     *
     * @param factory
     */
    public RxRetrofit addConverterFactory(Converter.Factory factory) {
        this.converterFactory = factory;
        return this;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * 设置baseUrl
     * 必须。需在initialize之前完成
     */
    public RxRetrofit baseUrl(@NonNull String baseUrl) {
        if (TextUtils.isEmpty(baseUrl)) {
            throw new IllegalArgumentException("baseUrl cannot be empty");
        }
        setBaseUrl(baseUrl);
        return this;
    }

    /**
     * 是否在Chrome浏览器中抓取请求信息
     * 非必须。可以随时调用
     *
     * @param flag
     * @return
     */
    public RxRetrofit showRequestInChrome(Context context, boolean flag) {
        if (flag) {
            Stetho.initializeWithDefaults(context);
        }
        return this;
    }

    public void initialize() {
        if (TextUtils.isEmpty(baseUrl)) {
            throw new IllegalArgumentException("please invoke initialize baseUrl before invoke initialize");
        }
        if (okhttpClient == null) {
            throw new NullPointerException("please invoke initOkhttpClient before invoke initialize");
        }
        if (converterFactory == null) {
            throw new NullPointerException("please add a ConverterFactory for retrofit before invoke initialize");
        }
        initRetrofit();
    }

    /**
     * 初始化okhttpClient
     * 必传项
     *
     * @return
     */
    public RxRetrofit initOkhttpClient() {
        okhttpClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .cookieJar(new JavaNetCookieJar(cookieHandler)) //自动管理cookie
                .build();
        return this;
    }

    /**
     * 请求再处理
     * 提供添加签名等机制
     * 非必须。需在initialize之前完成
     *
     * @param interceptor
     * @return
     */
    public RxRetrofit addReprocessRequestInterceptor(@NonNull Interceptor interceptor) {
        if (okhttpClient == null) {
            throw new NullPointerException("please invoke initOkhttpClient before invoke addReprocessRequestInterceptor");
        }
        okhttpClient.newBuilder().addInterceptor(interceptor).build();
        return this;
    }

    /**
     * 设置超时时间
     * 非必须。需在initialize之前完成
     *
     * @param read
     * @param write
     * @param connect
     * @return
     */
    public RxRetrofit timeOut(int read, int write, int connect) {
        if (okhttpClient == null) {
            throw new NullPointerException("please invoke initOkhttpClient before invoke timeOut");
        }
        okhttpClient.newBuilder()
                .readTimeout(read, TimeUnit.SECONDS)
                .writeTimeout(write, TimeUnit.SECONDS)
                .connectTimeout(connect, TimeUnit.SECONDS)
                .build();
        return this;
    }

    /**
     * 创建接口api
     *
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

    /**
     * 创建接口api, 你可以在这里临时指定别的baseURL
     *
     * @param service
     * @param <T>
     * @return
     */
    public <T> T create(String baseUrl, @NonNull Class<T> service) {
        if (this.baseUrl.equals(baseUrl)) {
            return create(service);
        } else {
            Object o = SERVICE_MAP.get(service);
            if (o != null) {
                return (T) o;
            } else {
                Retrofit.Builder builder = RETROFIT.newBuilder();
                builder.baseUrl(baseUrl);
                T t = builder.build().create(service);
                SERVICE_MAP.put(service, t);
                return t;
            }
        }
    }

}
