package com.vic.rxnetsdk;

import android.support.annotation.Nullable;
import android.util.Log;

import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by Jiang Chen on 16/3/23.
 */
public class CommonSubscriber<T> extends Subscriber<T> {

    private static final String TAG = CommonSubscriber.class.getSimpleName();

    private final Action1<? super T> onNext;

    public CommonSubscriber() {
        this(null);
    }

    public CommonSubscriber(@Nullable Action1<? super T> onNext) {
        this.onNext = onNext;
    }

    @Override
    public void onCompleted() {
        Log.i("PING", "ping");
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onNext(T response) {
        if (onNext != null) {
            onNext.call(response);
        }
    }
}
