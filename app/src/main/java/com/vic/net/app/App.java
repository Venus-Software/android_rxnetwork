package com.vic.net.app;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.vic.rxnetsdk.RxRetrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liu song on 2017/5/3.
 */

public class App extends Application {
    private static App instance;

    public static App getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        RxRetrofit.initInstance()
                .showRequestInChrome(this,true)
                .baseUrl("http://x")
                .initOkhttpClient()
                .timeOut(60,60,10)
                //请求再处理
                .addReprocessRequestInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request();
                        return chain.proceed(request);
                    }
                })
                .initialize();


    }
}
