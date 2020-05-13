package com.tofu.mvp.note;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by wxl on 2020/5/11.
 */
@Retention(RUNTIME) @Target(FIELD)
public @interface GainApi {
}
