package com.vic.rxnetsdk;

import android.net.ParseException;
import android.util.Log;

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

    private static final String TAG = CommonSubscriber.class.getSimpleName();

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
        Log.i(TAG, "onError()");
        if (e instanceof HttpException) {
            /*HTTP错误*/
        } else if (e instanceof ConnectException || e instanceof SocketTimeoutException) {
            /*链接异常*/
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            /*解析错误*/
        } else if (e instanceof UnknownHostException) {
            /*解析该域名异常*/
        } else {
            /*未知异常*/
        }
    }

    @Override
    public void onNext(T t) {
        Log.i(TAG, "onNext()");
        callBack(t);
    }

    //成功回调
    protected abstract void callBack(T t);
}
