package com.tofu.mvp.note;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by wxl on 2020/5/11.
 */
@Retention(RUNTIME)
@Target({ANNOTATION_TYPE,TYPE})
public @interface GainLifecycle {

    /**
     * 如果View层就是BaseFragment 或者 BaseActivity,
     * 可以直接指向View层实现
     *
     * 如果不是但是又需要注解来绑定生命周期，则需要调用MvpNote.bind(V v, Class lifeKey, LifecycleProvider life)
     * 来注册使用
     *
     * 使用于Gain网络请求
     * @return
     */
    Class<?> lifeKey();

}
