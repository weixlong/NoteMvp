package com.tofu.mvp.mvp;

import com.tofu.mvp.MainActivity1;
import com.tofu.mvp.base.BaseGameMvpLifecycle;
import com.tofu.mvp.contract.Main1Contract;
import com.tofu.mvp.contract.MainContract;
import com.tofu.mvp.gain.Callback;
import com.tofu.mvp.note.GainLifecycle;
import com.tofu.mvp.note.Model;
import com.tofu.mvp.note.View;
import com.tofu.mvp.util.Print;

/**
 * Created by wxl on 2020/5/12.
 */
@GainLifecycle(lifeKey = MainActivity1.class)
public class Main1Presenter implements Main1Contract.Presenter, BaseGameMvpLifecycle {

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


    @Override
    public void onMvpAttach() {
        Print.e("Main1Presenter onMvpCreate");
    }

    @Override
    public void onResume() {
        Print.e("Main1Presenter onResume");
    }

    @Override
    public void onPause() {
        Print.e("Main1Presenter onPause");
    }

    @Override
    public void onStop() {
        Print.e("Main1Presenter onStop");
    }

    @Override
    public void onMvpDetach() {
        Print.e("Main1Presenter onDestroy");
    }
}
