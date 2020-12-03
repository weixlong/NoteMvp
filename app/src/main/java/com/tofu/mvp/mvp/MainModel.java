package com.tofu.mvp.mvp;

import com.tofu.mvp.ApiService;
import com.tofu.mvp.ApiService1;
import com.tofu.mvp.MainActivity;
import com.tofu.mvp.contract.MainContract;
import com.tofu.mvp.gain.Callback;
import com.tofu.mvp.gain.Gain;
import com.tofu.mvp.note.GainApi;
import com.tofu.mvp.note.GainLifecycle;

/**
 * Created by wxl on 2020/5/9.
 */
@GainLifecycle(lifeKey = MainActivity.class)
public class MainModel implements MainContract.Model {

    @GainApi
    ApiService api;

    @GainApi
    ApiService1 api1;

    @Override
    public void getValue(Callback<String> callback) {
        Gain.load(api.getUser(), callback);
    }

    @Override
    public void getValue1(Callback<String> callback) {
        Gain.load(api1.getUser("Home.getConfig"), callback);

    }


}
