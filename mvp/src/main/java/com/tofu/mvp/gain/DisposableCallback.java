package com.tofu.mvp.gain;

import io.reactivex.disposables.Disposable;

/**
 * Created by wxl on 2019/6/28.
 *
 */

public interface DisposableCallback<T> {

    void onDisposable(Disposable disposable, Class<T> clazz);
}
