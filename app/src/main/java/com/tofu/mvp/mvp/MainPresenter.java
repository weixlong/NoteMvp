package com.tofu.mvp.mvp;

import com.tofu.mvp.MainActivity;
import com.tofu.mvp.contract.Main1Contract;
import com.tofu.mvp.contract.MainContract;
import com.tofu.mvp.gain.Gain;
import com.tofu.mvp.note.Model;
import com.tofu.mvp.note.Presenter;
import com.tofu.mvp.note.View;

/**
 * Created by wxl on 2020/5/9.
 */

public class MainPresenter implements MainContract.Presenter {

    @View
    MainContract.View view;

    @Model
    MainContract.Model model;

    @Presenter
    Main1Contract.Presenter presenter;

    @Override
    public void print() {
        Gain.loadAttachLifecycle(MainActivity.class,Main1Model.class);
        presenter.print1();
    }

    @Override
    public void print(String s) {
        presenter.print1(s);
    }
}
