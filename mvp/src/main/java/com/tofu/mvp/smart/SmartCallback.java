package com.tofu.mvp.smart;

import com.tofu.mvp.gain.Callback;

/**
 * Created by wxl on 2019/7/2.
 */

public abstract class SmartCallback<T> implements Callback<T> {

    protected boolean isDrop;

    @Override
    public void onSuccess(T t) {
        onSuccess( t,  isDrop);
    }

    @Override
    public void onFailed(String error) {
        onFailed( error, isDrop);
    }

    /**
     * 返回值 是否为最后一页
     * @param t
     * @param isDrop
     * @return
     */
    protected abstract boolean onSuccess(T t, boolean isDrop);

    public void onFailed(String error,boolean isDrop) {

    }
}
