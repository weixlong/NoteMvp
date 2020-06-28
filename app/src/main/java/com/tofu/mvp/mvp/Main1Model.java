package com.tofu.mvp.mvp;

import com.tofu.mvp.ApiService;
import com.tofu.mvp.ApiService1;
import com.tofu.mvp.contract.Main1Contract;
import com.tofu.mvp.gain.Callback;
import com.tofu.mvp.gain.Gain;
import com.tofu.mvp.note.GainApi;

/**
 * Created by wxl on 2020/5/12.
 */
public class Main1Model implements Main1Contract.Model {

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
        Gain.load(api1.getUser(), callback);
    }
}
