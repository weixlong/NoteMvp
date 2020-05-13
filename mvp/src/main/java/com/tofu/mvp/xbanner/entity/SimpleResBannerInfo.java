package com.tofu.mvp.xbanner.entity;

/**
 * Created by wxl on 2020/3/9.
 */
public class SimpleResBannerInfo extends SimpleBannerInfo{
    private int resId;

    public SimpleResBannerInfo(int resId) {
        this.resId = resId;
    }

    @Override
    public Object getXBannerUrl() {
        return resId;
    }

    @Override
    public String getXBannerTitle() {
        return null;
    }
}
