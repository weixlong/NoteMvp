package com.tofu.mvp.note;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


/**
 * Created by wxl on 2020/5/9.
 */
@Retention(RUNTIME)
@Target({ANNOTATION_TYPE,TYPE})
public @interface PMTarget {

    /**
     *<p>在View实现里指定一个P层实现<p/>
     * <p>P无参构造方法保留public访问权限<p/>
     * <p>框架将会通过无参构造函数生产P层<p/>
     * @return
     */
    Class<?>[] p();


    /**
     * <p>在View实现里指定一个M层实现<p/>
     * <p>M无参构造方法保留public访问权限<p/>
     * <p>框架将会通过无参构造函数生产M层<p/>
     * @return
     */
    Class<?>[] m();


    /**
     * <p>在View实现里指定需要依赖的其他V层<p/>
     * <p>如果该V层已被释放则将无法被依赖成功<p/>
     * @return
     */
    Class<?>[] v() default PMTarget.class;
}
