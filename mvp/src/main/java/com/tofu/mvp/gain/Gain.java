package com.tofu.mvp.gain;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.tofu.mvp.gain.convert.FastJsonConverterFactory;
import com.tofu.mvp.gain.convert.ToStringConverterFactory;
import com.tofu.mvp.gain.exception.ExceptionHandler;
import com.tofu.mvp.gain.trust.SSLSocketClient;
import com.tofu.mvp.pool.Mvp;
import com.tofu.mvp.smart.SmartView;
import com.tofu.mvp.util.ActivityManager;
import com.tofu.mvp.util.Print;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.trello.rxlifecycle2.components.support.RxFragmentActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by wxl on 2019/6/26.
 * https://github.com/QMUI/QMUI_Android
 */

public class Gain {


    /**
     * 静态内部类保证全局唯一
     */
    private static class Instance {
        private static Option option = new Option();
        private static Gain request = new Gain();
    }


    /**
     * 请求接口
     *
     * @return Api
     */
    public static <Api> Api api() {
        return (Api) Instance.option.api;
    }


    /**
     * 加载Gain生命周期跟随
     * @param key 当前实现生命周期的实体class
     * @param attach 依赖的其他MVP里使用Gain的地方跟随key指向的生命周期
     */
    public static <V> void loadAttachLifecycle(Class<V> key,Class... attach){
        Mvp.addLifeAttach(key,attach);
    }


    /**
     * 默认执行
     *
     * @param observable
     * @param callback
     */
    public static <T> void exe(Observable<HttpResult<T>> observable, Callback<T> callback) {
        Class key = getCallClass();
        LifecycleProvider lifecycleProvider = Mvp.getAnnotationLife(key);
        if (lifecycleProvider != null) {
            exe(lifecycleProvider, observable, callback);
        } else {
            observable.compose(Gain.get().defaultSchedulers())
                    .subscribe(Gain.get().getDefaultSubscriber(callback));
        }
    }


    /**
     * 默认执行
     *
     * @param observable
     * @param callback
     */
    public static <T> void exe(LifecycleProvider lifecycleProvider, Observable<HttpResult<T>> observable, Callback<T> callback) {
        if (lifecycleProvider != null) {
            observable.compose(Gain.get().defaultSchedulers(lifecycleProvider))
                    .subscribe(Gain.get().getDefaultSubscriber(callback));
        } else {
            observable.compose(Gain.get().defaultSchedulers())
                    .subscribe(Gain.get().getDefaultSubscriber(callback));
        }
    }


    /**
     * 默认执行,带对话框
     * ActivityManager 里要设置当前的Activity
     * @param observable
     * @param callback
     */
    public static <T> void load(Observable<HttpResult<T>> observable, Callback<T> callback) {
        Class key = getCallClass();
        LifecycleProvider lifecycleProvider = Mvp.getAnnotationLife(key);
        Print.d(lifecycleProvider);
        if (lifecycleProvider != null) {
            load(lifecycleProvider, observable, callback);
        } else {
            observable.compose(Gain.get().defaultDialogSchedulers())
                    .subscribe(Gain.get().getDefaultSubscriber(callback));
        }
    }


    /**
     * 默认执行,带对话框
     * ActivityManager 里要设置当前的Activity
     * @param observable
     * @param callback
     */
    public static <T> void load(LifecycleProvider lifecycleProvider, Observable<HttpResult<T>> observable, Callback<T> callback) {
        if (lifecycleProvider != null) {
            observable.compose(Gain.get().defaultDialogSchedulers(lifecycleProvider))
                    .subscribe(Gain.get().getDefaultSubscriber(callback));
        } else {
            observable.compose(Gain.get().defaultDialogSchedulers())
                    .subscribe(Gain.get().getDefaultSubscriber(callback));
        }
    }

    /**
     * 默认描述者
     *
     * @param callback
     * @return DefaultSubscriber
     */
    public <T> DefaultSubscriber<T> getDefaultSubscriber(Callback<T> callback) {
        return DefaultSubscriber.newInstance(callback);
    }

    public static class Option<Api> {

        private static int HTTP_CONNECT_OUT_TIME = 10 * 1000;

        private static String BASE_URL = "";

        private static int SUCCESS_CODE = 0;

        private OkHttpClient.Builder builder;

        private Api api;

        private Class<Api> apiClass;

        private ExceptionHandler.OnExceptionCallback exceptionCallback;

        private LoadingPopupView popupView;

        private Option() {
            if (builder == null) {
                builder = new OkHttpClient.Builder()
                        .addInterceptor(new HttpLoggingInterceptor()
                                .setLevel(HttpLoggingInterceptor.Level.BODY))
                        .retryOnConnectionFailure(true)
                        .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                        .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                        .connectTimeout(HTTP_CONNECT_OUT_TIME, TimeUnit.SECONDS)
                        .writeTimeout(HTTP_CONNECT_OUT_TIME, TimeUnit.SECONDS)
                        .readTimeout(HTTP_CONNECT_OUT_TIME, TimeUnit.SECONDS);
            }
        }


        /**
         * 添加拦截器<p>
         * ● 不需要担心中间过程的响应,如重定向和重试.<p>
         * ● 总是只调用一次,即使HTTP响应是从缓存中获取.<p>
         * ● 观察应用程序的初衷. 不关心OkHttp注入的头信息如: If-None-Match.<p>
         * ● 允许短路而不调用 Chain.proceed(),即中止调用.<p>
         * ● 允许重试,使 Chain.proceed()调用多次.<p>
         *
         * @param interceptor
         * @return Option
         */
        public Option addInterceptor(Interceptor interceptor) {
            builder.addInterceptor(interceptor);
            SmartView.option().addInterceptor(interceptor);
            return this;
        }


        /**
         * 添加网络拦截器<p>
         * ● 能够操作中间过程的响应,如重定向和重试.<p>
         * ● 当网络短路而返回缓存响应时不被调用.<p>
         * ● 只观察在网络上传输的数据.<p>
         * ● 携带请求来访问连接.<p>
         *
         * @param interceptor
         * @return Option
         */
        public Option addNetworkInterceptor(Interceptor interceptor) {
            builder.addNetworkInterceptor(interceptor);
            SmartView.option().addNetworkInterceptor(interceptor);
            return this;
        }


        /**
         * 错误回调
         *
         * @param exceptionCallback
         * @return Option
         */
        public Option setExceptionInterceptor(ExceptionHandler.OnExceptionCallback exceptionCallback) {
            this.exceptionCallback = exceptionCallback;
            return this;
        }

        /**
         * 设置接口类
         *  可在MVP层中GainApi注解使用该api
         *  也可Gain.api() 调用
         * @param apiClass
         * @return Option
         */
        public Option api(Class<Api> apiClass) {
            this.apiClass = apiClass;
            return this;
        }

        /**
         * 创建retrofit
         *
         * @param clazz
         * @param baseUrl
         * @param <T>
         * @return T
         */
        private <T> T createApi(Class<T> clazz, String baseUrl) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(builder.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(ToStringConverterFactory.create())
                    .addConverterFactory(FastJsonConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())//添加Gson支持，然后Retrofit就会使用Gson将响应体（api接口的Take）转换我们想要的类型。
                    .build();
            return retrofit.create(clazz);
        }


        /**
         * 构建Retrofit
         */
        public void build() {
            api = createApi(apiClass, BASE_URL);
            ExceptionHandler.exceptionCallback(exceptionCallback);
        }


        /**
         * 显示加载中
         */
        private void showLoading() {
            Activity activity = ActivityManager.getInstance().peekActivity();
            if (popupView == null && activity != null) {
                XPopup.Builder builder = new XPopup.Builder(activity).hasShadowBg(false);
                popupView = builder.asLoading("加载中");
            }
            if (popupView != null && !popupView.isShow()) {
                popupView.show();
            }
        }

        /**
         * 关闭加载中
         */
        private void dismissLoading() {
            if (popupView != null && popupView.isShow()) {
                popupView.dismiss();
            }
        }


        /**
         * 成功码 默认为0
         *
         * @param code
         * @return Option
         */
        public Option okCode(int code) {
            SUCCESS_CODE = code;
            return this;
        }

        /**
         * 超时时间
         *
         * @param outTime
         * @return
         */
        public Option setConnectOutTime(int outTime) {
            Option.HTTP_CONNECT_OUT_TIME = outTime;
            return this;
        }


        /**
         * 基本路径
         *
         * @param baseUrl
         * @return Option
         */
        public Option baseUrl(String baseUrl) {
            Option.BASE_URL = baseUrl;
            return this;
        }


        /**
         * 获取超时时间
         *
         * @return ConnectOutTime
         */
        public static int getHttpConnectOutTime() {
            return HTTP_CONNECT_OUT_TIME;
        }


        /**
         * 获取错误回调
         *
         * @return OnExceptionCallback
         */
        public ExceptionHandler.OnExceptionCallback getExceptionCallback() {
            return exceptionCallback;
        }


        /**
         * 获取成功码
         *
         * @return SuccessCode
         */
        public static int getSuccessCode() {
            return SUCCESS_CODE;
        }
    }


    /**
     * 请求
     *
     * @return Gain
     */
    private static Gain get() {
        return Instance.request;
    }


    /**
     * 配置
     *
     * @return Option
     */
    public static Option option() {
        return Instance.option;
    }


    /**
     * 获得默认Observable
     *
     * @return ObservableTransformer
     */
    public <T> ObservableTransformer<T, T> defaultSchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }


    /**
     * 获得默认Observable
     *
     * @param lifecycleProvider
     * @return ObservableTransformer
     */
    public <T> ObservableTransformer<T, T> defaultSchedulers(@NonNull LifecycleProvider lifecycleProvider) {
        if (lifecycleProvider instanceof RxFragmentActivity) {
            return observable -> observable
                    .subscribeOn(Schedulers.newThread())
                    .compose(lifecycleProvider.bindUntilEvent(ActivityEvent.DESTROY))
                    .observeOn(AndroidSchedulers.mainThread());
        } else if (lifecycleProvider instanceof RxFragment) {
            return observable -> observable
                    .subscribeOn(Schedulers.newThread())
                    .compose(lifecycleProvider.bindUntilEvent(FragmentEvent.DESTROY))
                    .observeOn(AndroidSchedulers.mainThread());
        }
        return defaultSchedulers();
    }

    /**
     * 获得默认Dialog Observable
     *
     * @return ObservableTransformer
     */
    public <T> ObservableTransformer<T, T> defaultDialogSchedulers() {
        return observable -> observable
                .subscribeOn(Schedulers.newThread())
                .doOnSubscribe(disposable -> {
                    option().showLoading();
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally((Action) () -> {
                    option().dismissLoading();
                });
    }


    /**
     * 获得默认Dialog Observable
     *
     * @param lifecycleProvider
     * @return ObservableTransformer
     */
    public <T> ObservableTransformer<T, T> defaultDialogSchedulers(@NonNull final LifecycleProvider lifecycleProvider) {
        if (lifecycleProvider instanceof RxFragmentActivity) {
            return observable -> observable
                    .subscribeOn(Schedulers.newThread())
                    .compose(lifecycleProvider.bindUntilEvent(ActivityEvent.DESTROY))
                    .doOnSubscribe((Consumer) disposable -> option().showLoading())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally((Action) () -> {
                        option().dismissLoading();
                    });
        } else if (lifecycleProvider instanceof RxFragment) {
            return observable -> observable
                    .subscribeOn(Schedulers.newThread())
                    .compose(lifecycleProvider.bindUntilEvent(FragmentEvent.DESTROY))
                    .doOnSubscribe(disposable -> {
                        option().showLoading();
                    })
                    .observeOn(AndroidSchedulers.mainThread())
                    .doFinally((Action) () -> {
                        option().dismissLoading();
                    });
        }
        return defaultDialogSchedulers();
    }


    /**
     * 获得model层的类名，即默认调用请求的层级
     *
     * @return Class
     */
    public static Class getCallClass() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        String className = traceElement.getClassName();
        try {
            Print.d(" use gain class = " + className);
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
