package com.tofu.mvp.base;

/**
 * create file time : 2020/12/3
 * create user : wxl
 * subscribe :
 */
public interface BaseGainMvpFragmentLifecycle extends BaseGameMvpLifecycle {


    /**
     * 销毁view，与生命周期同步
     */
    void onDestroyView();


    /**
     * 解绑，与生命周期同步
     */
    void onDetach();
}
