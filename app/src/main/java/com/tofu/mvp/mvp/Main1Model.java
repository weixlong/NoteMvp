package com.tofu.mvp.mvp;

import com.tofu.mvp.ApiService;
import com.tofu.mvp.ApiService1;
import com.tofu.mvp.MainActivity1;
import com.tofu.mvp.base.BaseGameMvpLifecycle;
import com.tofu.mvp.contract.Main1Contract;
import com.tofu.mvp.gain.Callback;
import com.tofu.mvp.gain.Gain;
import com.tofu.mvp.note.GainApi;
import com.tofu.mvp.note.GainLifecycle;
import com.tofu.mvp.util.Print;

/**
 * Created by wxl on 2020/5/12.
 */
@GainLifecycle(lifeKey = MainActivity1.class)
public class Main1Model implements Main1Contract.Model , BaseGameMvpLifecycle {

    @GainApi
    ApiService api;

    @GainApi
    ApiService1 api1;

    @Override
    public void getValue1(Callback<String> callback) {
        Gain.load(api.getUser(), callback);
    }

    @Override
    public void getValue2(Callback<String> callback) {
        Gain.load(api1.getUser("Home.getConfig"), callback);
    }

    @Override
    public void onMvpAttach() {
        Print.e("Main1Model onMvpCreate");
    }

    @Override
    public void onResume() {
        Print.e("Main1Model onResume");
    }

    @Override
    public void onPause() {
        Print.e("Main1Model onPause");
    }

    @Override
    public void onStop() {
        Print.e("Main1Model onStop");
    }

    @Override
    public void onMvpDetach() {
        Print.e("Main1Model onDestroy");
    }
}
