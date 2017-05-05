package com.vic.rxnetsdk;

import android.app.Application;
import android.net.ParseException;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;
import rx.Subscriber;

/**
 * Created by liu song on 2017/5/4.
 */

public abstract class RxSubscriber<T> extends Subscriber<T> {

    private static final String TAG = RxSubscriber.class.getSimpleName();

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onCompleted()");
    }

    @Override
    public void onCompleted() {
        Log.i(TAG, "onCompleted()");
    }

    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "onError()");
        if (e instanceof HttpException) {
            /*HTTP错误*/
            Log.e(TAG, "http错误码："+((HttpException) e).code());
        } else if (e instanceof ConnectException || e instanceof SocketTimeoutException) {
            /*链接异常*/
            Log.e(TAG, "链接异常!");
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            /*解析异常*/
            Log.e(TAG, "解析异常!");
        } else if (e instanceof UnknownHostException) {
            /*解析域名异常*/
            Log.e(TAG, "解析域名异常!");
        } else {
            /*未知异常*/
            Log.e(TAG, "未知异常!");
        }

        e.printStackTrace();
    }

    @Override
    public void onNext(T t) {
        Log.i(TAG, "onNext()");
        callBack(t);
    }

    //成功回调
    protected abstract void callBack(T t);
}
