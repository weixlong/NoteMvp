package com.tofu.mvp.base;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;


/**
 * Created by wxl on 2019/6/28.
 * https://blog.csdn.net/luzhenyuxfcy/article/details/87696429 开源UI框架
 */

public class App extends Application {


    static {//static 代码段可以防止内存泄露
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {

            @NonNull
            @Override
            public RefreshHeader createRefreshHeader(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.setPrimaryColorsId(android.R.color.transparent, android.R.color.white);//全局设置主题颜色
                layout.setEnableHeaderTranslationContent(false);
                return new MaterialHeader(context).setShowBezierWave(true);//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });

        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context)
                        .setPrimaryColor(ContextCompat.getColor(context, android.R.color.white))
                        .setAccentColor(ContextCompat.getColor(context, android.R.color.darker_gray));
            }
        });
    }



    @Override
    public void onCreate() {
        super.onCreate();
//        Tu.initialize(this);
//        XmlDB.initialize(this);
//        Gain.option()
//                .baseUrl("")
//                .context(this)
//                .setConnectOutTime(20000)
//                .okCode(200)
//                .setExceptionInterceptor(e -> false)
//                .addInterceptor(new Interceptor() {
//                    @Override
//                    public Response intercept(Chain chain) throws IOException {
//                        // 当本地有用户信息的时候就添加公共参数
//
//                        return chain;
//                    }
//                })
//                .build();



    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
