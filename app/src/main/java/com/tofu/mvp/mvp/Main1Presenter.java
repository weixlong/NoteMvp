package com.tofu.mvp.mvp;

import com.tofu.mvp.contract.Main1Contract;
import com.tofu.mvp.contract.MainContract;
import com.tofu.mvp.gain.Callback;
import com.tofu.mvp.note.Model;
import com.tofu.mvp.note.View;

/**
 * Created by wxl on 2020/5/12.
 */
public class Main1Presenter implements Main1Contract.Presenter {

    @View
    MainContract.View view;

    @Model
    Main1Contract.Model model;

    @Override
    public void print1() {
        model.getValue1(new Callback<String>() {
            @Override
            public void onSuccess(String s) {
                print1(s);
            }

            @Override
            public void onFailed(String error) {
                print1(error);
                model.getValue2(new Callback<String>() {
                    @Override
                    public void onSuccess(String s) {
                        print1(s);
                    }

                    @Override
                    public void onFailed(String error) {
                        print1("with api1 "+error);
                    }
                });
            }
        });
    }

    @Override
    public void print1(String s) {
        view.print("from 1 "+s);
    }
}
