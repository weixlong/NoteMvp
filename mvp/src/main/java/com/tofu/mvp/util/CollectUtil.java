package com.tofu.mvp.util;

import java.util.Collection;
import java.util.Map;

/**
 * Created by wxl on 2019/5/23.
 */

public class CollectUtil {
    public static boolean isEmpty(Collection l) {
        if (null == l || l.isEmpty()) return true;
        return false;
    }

    public static boolean isEmpty(Map map) {
        if (null == map || map.isEmpty()) return true;
        return false;
    }

    public static boolean isNotEmpty(Collection l) {
        return !isEmpty(l);
    }


    public static boolean isNotEmpty(Map map) {
        return !isEmpty(map);
    }
}
