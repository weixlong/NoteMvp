package com.tofu.mvp.gain;


import com.tofu.mvp.gain.exception.ExceptionHandler;
import com.tofu.mvp.gain.exception.UnKnowException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by wxl on 2019/5/20.
 */

public class DefaultSubscriber<T> implements Observer<HttpResult<T>> {

    private Callback<T> callback;


    private DefaultSubscriber(Callback<T> callback) {
        this.callback = callback;
    }


    public static <T> DefaultSubscriber<T> newInstance(Callback<T> callback){
        return new DefaultSubscriber<T>(callback);
    }

    @Override
    public void onSubscribe(Disposable d) {
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

    @Override
    public void onNext(HttpResult<T> result) {
        if (result.getCode() == Gain.option().getSuccessCode()) {
            if (callback != null) {
                callback.onSuccess(result.getData());
            }
            return;
        }
        if (callback != null) {
            callback.onFailed(result.getMessage());
        }
        ExceptionHandler.handlerException(new UnKnowException(result.getMessage(), result.getCode()));
    }
}
