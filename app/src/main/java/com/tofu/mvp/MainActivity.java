package com.tofu.mvp;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.tofu.mvp.base.BaseActivity;
import com.tofu.mvp.contract.MainContract;
import com.tofu.mvp.mvp.Main1Model;
import com.tofu.mvp.mvp.Main1Presenter;
import com.tofu.mvp.mvp.MainModel;
import com.tofu.mvp.mvp.MainPresenter;
import com.tofu.mvp.note.PMTarget;
import com.tofu.mvp.note.Presenter;
import com.tofu.mvp.permissions.OnPermissionResultCallback;
import com.tofu.mvp.permissions.PermissionReq;
import com.tofu.mvp.util.Print;


@PMTarget(p = {MainPresenter.class, Main1Presenter.class},m = {MainModel.class, Main1Model.class})
public class MainActivity extends BaseActivity implements MainContract.View, OnPermissionResultCallback {

    @Presenter
    MainContract.Presenter presenter;

    private TextView view;


    @Override
    protected int onCreateBindViewLayoutId(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main_layout;
    }

    @Override
    protected void onCreateBindViewChanged(@Nullable Bundle savedInstanceState) {
        MvpNote.bind(this);

    }

    @Override
    protected void onDestroy() {
        MvpNote.unBind(getClass());
        super.onDestroy();
    }

    @Override
    public void print(String s) {
       // view.setText(s);
    }

    public void onPrintClick(View view) {
        this.view = (TextView) view;
        presenter.print();
    }

    public void onReqPermission(View v){
        PermissionReq.instance(this)
                .permissions(Manifest.permission.CALL_PHONE)
                .requestCode(123)
                .callback(this)
                .req();
    }

    @Override
    public void onRequestPermissionSuccess(int requestCode) {
        Print.e("onRequestPermissionSuccess");
        startActivity(new Intent(this,MainActivity1.class));
    }

    @Override
    public void onRequestPermissionFailed(int requestCode) {
        Print.e("onRequestPermissionFailed");
    }
}
