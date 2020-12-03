package com.tofu.mvp.base;

/**
 * create file time : 2020/12/3
 * create user : wxl
 * subscribe :
 */
public interface BaseGameMvpLifecycle {


    /**
     * 绑定Mvp后调用,Mvp框架自主调用，切勿在此处做过多耗时操作
     */
    void onMvpAttach();


    /**
     * 显示，与生命周期同步
     */
    void onResume();


    /**
     * 暂停，与生命周期同步
     */
    void onPause();

    /**
     * 停止，与生命周期同步
     */
    void onStop();


    /**
     * 销毁Mvp，与释放Mvp同步
     */
    void onMvpDetach();
}
