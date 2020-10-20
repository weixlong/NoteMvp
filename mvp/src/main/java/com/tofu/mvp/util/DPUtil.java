package com.tofu.mvp.util;

import android.content.res.Resources;

public class DPUtil {

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    public static int dip2px(int dp) {
        float density = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }

    /**
     * px转换dip
     */
    public static int px2dip(int px) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    /**
     * px转换sp
     */
    public static int px2sp(int pxValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * sp转换px
     */
    public static int sp2px(int spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

}
