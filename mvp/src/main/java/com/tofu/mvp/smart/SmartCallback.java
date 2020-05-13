package com.tofu.mvp.smart;

/**
 * Created by wxl on 2019/7/2.
 */

public abstract class SmartCallback<T> {

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
