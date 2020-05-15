package com.tofu.mvp.gain;

import com.tofu.mvp.gain.exception.ExceptionHandler;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by wxl on 2020/5/15.
 */
public class SimpleSubscriber<T> implements Observer<T> {

    private Callback<T> callback;


    private SimpleSubscriber(Callback<T> callback) {
        this.callback = callback;
    }


    public static <T> SimpleSubscriber<T> newInstance(Callback<T> callback){
        return new SimpleSubscriber<T>(callback);
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T t) {
        if (callback != null) {
            callback.onSuccess(t);
        }
    }

    @Override
    public void onError(Throwable e) {
        if (callback != null) {
            callback.onFailed(e.getLocalizedMessage());
        }
        ExceptionHandler.handlerException(e);
    }

    @Override
    public void onComplete() {

    }
}
