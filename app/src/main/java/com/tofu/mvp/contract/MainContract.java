package com.tofu.mvp.contract;

import com.tofu.mvp.gain.Callback;

/**
 * Created by wxl on 2020/5/9.
 */
public interface MainContract {

    interface View {
        void print(String s);
    }

    interface Presenter {
        void print();
        void print(String s);
    }

    interface Model {
        void getValue(Callback<String> callback);
        void getValue1(Callback<String> callback);
    }
}
