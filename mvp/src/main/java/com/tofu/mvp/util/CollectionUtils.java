package com.tofu.mvp.util;

import java.util.Collection;
import java.util.Map;

/**
 * Copyright：贵州玄机科技有限公司
 * Created by wxl on 2020/7/17 13:09
 * Description：
 * Modify time：
 * Modified by：
 */
public class CollectionUtils {

    public static boolean isEmpty(Collection l){
        if(l == null || l.isEmpty())return true;
        return false;
    }

    public static boolean isNotEmpty(Collection l){
        return !isEmpty(l);
    }

    public static boolean isNull(Collection l){
        return l == null;
    }

    public static boolean isNotNull(Collection l){
        return !isNull(l);
    }

    public static boolean isEmpty(Map m){
        if(m == null || m.isEmpty())return true;
        return false;
    }

    public static boolean isNotEmpty(Map m){
        return !isEmpty(m);
    }


    public static void safeClear(Collection l){
        if(l != null){
            l.clear();
        }
    }

    public static void safeClearNull(Collection l){
        if(l != null){
            l.clear();
            l = null;
        }
    }
}
