package com.tofu.mvp;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.tofu.mvp.base.BaseActivity;
import com.tofu.mvp.contract.Main1Contract;
import com.tofu.mvp.mvp.Main1Model;
import com.tofu.mvp.mvp.Main1Presenter;
import com.tofu.mvp.note.PMTarget;
import com.tofu.mvp.note.Presenter;

/**
 * create file time : 2020/12/3
 * create user : wxl
 * subscribe :
 */
@PMTarget(p = Main1Presenter.class,m = Main1Model.class)
public class MainActivity1 extends BaseActivity  {

    @Presenter
    Main1Contract.Presenter presenter;

    @Override
    protected int onCreateBindViewLayoutId(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main_layout1;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        MvpNote.bind(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onCreateBindViewChanged(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void onDestroy() {
        MvpNote.unBind(getClass());
        super.onDestroy();
    }

    public void onPost(View view) {
        presenter.print1();
    }
}
