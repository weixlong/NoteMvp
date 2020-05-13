package com.tofu.mvp.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;
import com.tofu.mvp.event.SnackEvent;
import com.tofu.mvp.util.ActivityManager;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


/**
 * Created by wxl on 2019/6/21.
 *
 */

public abstract class BaseActivity extends RxAppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onCreateBindViewBefore(savedInstanceState);
        View view = onCreateBindViewUpdate(onCreateBindViewLayoutId(savedInstanceState), savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(view);
        ActivityManager.getInstance().pushActivity(this);
        EventBus.getDefault().register(this);
        onCreateBindViewChanged(savedInstanceState);
    }


    /**
     * 设置view之前
     *
     * @param savedInstanceState
     */
    protected void onCreateBindViewBefore(@Nullable Bundle savedInstanceState) {
        // todo child do something
    }

    /**
     * 布局id
     *
     * @return
     */
    protected abstract @LayoutRes
    int onCreateBindViewLayoutId(@Nullable Bundle savedInstanceState);


    /**
     * 设置view 之后
     */
    protected abstract void onCreateBindViewChanged(@Nullable Bundle savedInstanceState);


    /**
     * 默认获取布局的方式
     *
     * @return
     */
    private View generateLayoutView(@LayoutRes int layoutId) {
        return LayoutInflater.from(this).inflate(layoutId, null, false);
    }


    /**
     * 复写修改布局，改变最终设置的布局
     * @param layoutId
     * @return
     */
    protected View onCreateBindViewUpdate(@LayoutRes int layoutId,@Nullable Bundle savedInstanceState){
        return generateLayoutView(layoutId);
    }




    @ColorInt
    public final int getResColor(@ColorRes int id) {
        return ContextCompat.getColor(this,id);
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }


    @Override
    public void startActivity(Intent intent, Bundle options) {
        super.startActivity(intent, options);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        ActivityManager.getInstance().popActivity(getClass());
    }


    /**
     * 使用snackbar显示内容
     *
     * @param
     * @param
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void showSnackbar(SnackEvent event) {
        View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
        Snackbar bar= Snackbar.make(view, event.getMessage(), event.isLong() ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT);
        bar.getView().setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_orange_dark));
        bar.show();
    }
}
