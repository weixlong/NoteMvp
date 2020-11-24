package com.tofu.mvp.gain;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.lxj.xpopup.util.navbar.NavigationBarObserver;
import com.tofu.mvp.MvpNote;
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

import java.util.HashMap;
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
        private static HashMap<Class, Object> api = new HashMap<>();
    }


    /**
     * 请求接口
     *
     * @return Api
     */
    public static <Api> Api api(Class<Api> apiClass) {
        return (Api) Instance.api.get(apiClass);
    }


    /**
     * 加载Gain生命周期跟随,
     * 当key为受管理的V层时，会跟随unbind一起释放，
     * 如key不受管理时，请手动释放Gain.unAttachLifecycle.
     *
     * @param key    当前实现生命周期的实体class
     * @param attach 依赖的其他MVP里使用Gain的地方跟随key指向的生命周期
     */
    public static <V> void loadAttachLifecycle(Class<V> key, Class... attach) {
        Mvp.addLifeAttach(key, attach);
    }

    /**
     * 解绑生命周期跟随
     *
     * @param key
     * @param <V>
     */
    public static <V> void unAttachLifecycle(Class<V> key) {
        MvpNote.unBindAttach(key);
    }


    /**
     * 生命周期的接管，替换@GainLifecycle 里注解的生命周期key值
     *
     * @param takeOverWho 被接管的key
     * @param newKey      新的key
     * @param <V>
     * @param <K>
     */
    public static <V, K> void takeOverLifecycleToNewKey(Class<V> takeOverWho, Class<K> newKey) {
        Mvp.addTakeOverLifeKeyToNewKey(takeOverWho, newKey);
    }


    /**
     * 取消生命周期接管，把接管的key 交还给@GainLifecycle 里的key
     *
     * @param key 目前接管的key
     * @param <K>
     */
    public static <K> void cancelTakeOverLifecycle(Class<K> key) {
        Mvp.removeTakeOverLifeKey(key);
    }


    /**
     * 默认执行
     *
     * @param observable
     * @param callback
     */
    public static <T> void exe(Observable<T> observable, Callback<T> callback) {
        Class key = getCallClass();
        LifecycleProvider lifecycleProvider = Mvp.getAnnotationLife(key);
        if (lifecycleProvider != null) {
            exe(lifecycleProvider, observable, callback);
        } else {
            observable.compose(Gain.get().defaultSchedulers())
                    .subscribe(Gain.get().getSimpleSubscriber(callback));
        }
    }


    /**
     * 默认执行
     *
     * @param observable
     * @param callback
     */
    public static <T> void exe(Observable<T> observable, Callback<T> callback, ActivityEvent event) {
        Class key = getCallClass();
        LifecycleProvider lifecycleProvider = Mvp.getAnnotationLife(key);
        if (lifecycleProvider != null) {
            exe(lifecycleProvider, observable, callback, event);
        } else {
            observable.compose(Gain.get().defaultSchedulers())
                    .subscribe(Gain.get().getSimpleSubscriber(callback));
        }
    }


    /**
     * 默认执行
     *
     * @param observable
     * @param callback
     */
    public static <T> void exe(LifecycleProvider lifecycleProvider, Observable<T> observable, Callback<T> callback) {
        if (lifecycleProvider != null) {
            observable.compose(Gain.get().defaultSchedulers(lifecycleProvider))
                    .subscribe(Gain.get().getSimpleSubscriber(callback));
        } else {
            observable.compose(Gain.get().defaultSchedulers())
                    .subscribe(Gain.get().getSimpleSubscriber(callback));
        }
    }


    /**
     * 默认执行
     *
     * @param observable
     * @param callback
     */
    public static <T> void exe(LifecycleProvider lifecycleProvider, Observable<T> observable, Callback<T> callback, ActivityEvent event) {
        if (lifecycleProvider != null) {
            observable.compose(Gain.get().defaultSchedulers(lifecycleProvider, event))
                    .subscribe(Gain.get().getSimpleSubscriber(callback));
        } else {
            observable.compose(Gain.get().defaultSchedulers())
                    .subscribe(Gain.get().getSimpleSubscriber(callback));
        }
    }


    /**
     * 默认执行,带对话框
     * ActivityManager 里要设置当前的Activity
     *
     * @param observable
     * @param callback
     */
    public static <T> void load(Observable<T> observable, Callback<T> callback) {
        Class key = getCallClass();
        LifecycleProvider lifecycleProvider = Mvp.getAnnotationLife(key);
        if (lifecycleProvider != null) {
            load(lifecycleProvider, observable, callback);
        } else {
            observable.compose(Gain.get().defaultDialogSchedulers())
                    .subscribe(Gain.get().getSimpleSubscriber(callback));
        }
    }


    /**
     * 默认执行,带对话框
     * ActivityManager 里要设置当前的Activity
     *
     * @param observable
     * @param callback
     */
    public static <T> void load(Observable<T> observable, Callback<T> callback, ActivityEvent event) {
        Class key = getCallClass();
        LifecycleProvider lifecycleProvider = Mvp.getAnnotationLife(key);
        if (lifecycleProvider != null) {
            load(lifecycleProvider, observable, callback, event);
        } else {
            observable.compose(Gain.get().defaultDialogSchedulers())
                    .subscribe(Gain.get().getSimpleSubscriber(callback));
        }
    }


    /**
     * 默认执行,带对话框
     * ActivityManager 里要设置当前的Activity
     *
     * @param observable
     * @param callback
     */
    public static <T> void load(LifecycleProvider lifecycleProvider, Observable<T> observable, Callback<T> callback) {
        if (lifecycleProvider != null) {
            observable.compose(Gain.get().defaultDialogSchedulers(lifecycleProvider))
                    .subscribe(Gain.get().getSimpleSubscriber(callback));
        } else {
            observable.compose(Gain.get().defaultDialogSchedulers())
                    .subscribe(Gain.get().getSimpleSubscriber(callback));
        }
    }


    /**
     * 默认执行,带对话框
     * ActivityManager 里要设置当前的Activity
     *
     * @param observable
     * @param callback
     */
    public static <T> void load(LifecycleProvider lifecycleProvider, Observable<T> observable, Callback<T> callback, ActivityEvent event) {
        if (lifecycleProvider != null) {
            observable.compose(Gain.get().defaultDialogSchedulers(lifecycleProvider, event))
                    .subscribe(Gain.get().getSimpleSubscriber(callback));
        } else {
            observable.compose(Gain.get().defaultDialogSchedulers())
                    .subscribe(Gain.get().getSimpleSubscriber(callback));
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

    /**
     * 返回简单描述者
     *
     * @param callback
     * @param <T>
     * @return
     */
    public <T> SimpleSubscriber<T> getSimpleSubscriber(Callback<T> callback) {
        return SimpleSubscriber.newInstance(callback);
    }

    public static class Option<Api> {

        private static int HTTP_CONNECT_OUT_TIME = 10;

        private static String BASE_URL = "";

        private static int SUCCESS_CODE = 0;

        private OkHttpClient.Builder builder;

        private Class<Api> apiClass;

        private ExceptionHandler.OnExceptionCallback exceptionCallback;

        private LoadingPopupView popupView;

        private ActivityEvent event = ActivityEvent.DESTROY;

        private FragmentEvent fragmentEvent = FragmentEvent.DESTROY;

        private Option() {
            if (builder == null) {
                builder = new OkHttpClient.Builder()
                        .retryOnConnectionFailure(true)
                        .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                        .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                        .connectTimeout(HTTP_CONNECT_OUT_TIME, TimeUnit.SECONDS)
                        .writeTimeout(HTTP_CONNECT_OUT_TIME, TimeUnit.SECONDS)
                        .readTimeout(HTTP_CONNECT_OUT_TIME, TimeUnit.SECONDS);
                if (MvpNote.debug) {
                    builder.addInterceptor(new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY));
                }
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
         * 设置取消请求的生命周期
         */
        public Option setClientCancelEvent(ActivityEvent event) {
            this.event = event;
            return this;
        }

        /**
         * 设置取消请求的生命周期
         *
         * @param fragmentEvent
         * @return
         */
        public Option setClientCancelEvent(FragmentEvent fragmentEvent) {
            this.fragmentEvent = fragmentEvent;
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
         * 可在MVP层中GainApi注解使用该api
         * 也可Gain.api() 调用
         *
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
            Instance.api.put(apiClass, createApi(apiClass, BASE_URL));
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
            if (popupView != null) {
                popupView.dismiss();
                NavigationBarObserver.getInstance().removeOnNavigationBarListener(popupView);
                popupView = null;
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
         * @param outSecondTime
         * @return
         */
        public Option setConnectOutTime(int outSecondTime) {
            Option.HTTP_CONNECT_OUT_TIME = outSecondTime;
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
    public <T> ObservableTransformer<T, T> defaultSchedulers(@NonNull LifecycleProvider lifecycleProvider, ActivityEvent event) {
        if (lifecycleProvider instanceof RxFragmentActivity) {
            return observable -> observable
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(lifecycleProvider.bindUntilEvent(event));
        } else if (lifecycleProvider instanceof RxFragment) {
            return observable -> observable
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(lifecycleProvider.bindUntilEvent(event));
        }
        return defaultSchedulers();
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
                    .compose(lifecycleProvider.bindUntilEvent(Instance.option.event))
                    .observeOn(AndroidSchedulers.mainThread());
        } else if (lifecycleProvider instanceof RxFragment) {
            return observable -> observable
                    .subscribeOn(Schedulers.newThread())
                    .compose(lifecycleProvider.bindUntilEvent(Instance.option.fragmentEvent))
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
    public <T> ObservableTransformer<T, T> defaultDialogSchedulers(@NonNull final LifecycleProvider lifecycleProvider, ActivityEvent event) {
        if (lifecycleProvider instanceof RxFragmentActivity) {
            return observable -> observable
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(lifecycleProvider.bindUntilEvent(event))
                    .doOnSubscribe((Consumer) disposable -> option().showLoading())
                    .doFinally((Action) () -> {
                        option().dismissLoading();
                    });
        } else if (lifecycleProvider instanceof RxFragment) {
            return observable -> observable
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(lifecycleProvider.bindUntilEvent(event))
                    .doOnSubscribe(disposable -> {
                        option().showLoading();
                    })
                    .doFinally((Action) () -> {
                        option().dismissLoading();
                    });
        }
        return defaultDialogSchedulers();
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
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(lifecycleProvider.bindUntilEvent(Instance.option.event))
                    .doOnSubscribe((Consumer) disposable -> option().showLoading())
                    .doFinally((Action) () -> {
                        option().dismissLoading();
                    });
        } else if (lifecycleProvider instanceof RxFragment) {
            return observable -> observable
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .compose(lifecycleProvider.bindUntilEvent(Instance.option.fragmentEvent))
                    .doOnSubscribe(disposable -> {
                        option().showLoading();
                    })
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
