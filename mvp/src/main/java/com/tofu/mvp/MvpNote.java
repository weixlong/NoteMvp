package com.tofu.mvp;

import com.tofu.mvp.pool.Mvp;
import com.trello.rxlifecycle2.LifecycleProvider;

/**
 * Created by wxl on 2020/5/9.
 */
public class MvpNote {

    /**
     * 绑定V层
     * @param v
     * @param <V>
     */
    public static <V> void bind(V v) {
        Mvp.bind(v);
    }


    /**
     * 绑定V层和生命周期
     * @param v
     * @param lifeKey
     * @param life
     * @param <V>
     */
    public static <V> void bind(V v, Class lifeKey, LifecycleProvider life){
        Mvp.bind(v,lifeKey,life);
    }


    /**
     * 只绑定生命周期，响应GainLifecycle注解
     * @param lifeKey
     * @param life
     */
    public static void bindLife(Class lifeKey, LifecycleProvider life){
        Mvp.addLife(lifeKey,null,life);
    }





    /**
     * 解绑生命周期，使用默认时vClass为V层实现的class
     * @param vClass
     * @param <V>
     */
    public static <V> void unBindLife(Class<V> vClass){
        Mvp.unBindLife(vClass);
    }

    /**
     * 解绑生命周期跟随
     * @param key
     */
    public static void unBindAttach(Class key){
        Mvp.unAttachLife(key);
    }


    /**
     *  解绑，如使用默认绑定生命周期时，也会一同解绑
     * @param vClass
     * @param <V>
     */
    public static <V> void unBind(Class<V> vClass){
        Mvp.unBind(vClass);
    }
}
