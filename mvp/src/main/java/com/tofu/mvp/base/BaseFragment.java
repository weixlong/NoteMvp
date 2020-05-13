package com.tofu.mvp.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.trello.rxlifecycle2.components.support.RxFragment;


/**
 * Created by wxl on 2019/7/24.
 *
 */

public abstract class BaseFragment extends RxFragment {



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        onCreateBindViewBefore(savedInstanceState);
        View view = onCreateBindViewUpdate(onCreateBindViewLayoutId(savedInstanceState), savedInstanceState);
        onCreateBindViewChanged(savedInstanceState);
        return view;
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
        return LayoutInflater.from(getContext()).inflate(layoutId, null, false);
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
        return ContextCompat.getColor(getContext(),id);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
