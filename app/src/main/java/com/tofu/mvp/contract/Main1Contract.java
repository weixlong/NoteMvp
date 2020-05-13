package com.tofu.mvp.contract;

import com.tofu.mvp.gain.Callback;

/**
 * Created by wxl on 2020/5/12.
 */
public interface Main1Contract  {

    interface Presenter {
        void print1();
        void print1(String s);
    }

    interface Model{
        void getValue1(Callback<String> callback);
    }
}
