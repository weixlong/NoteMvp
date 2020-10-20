package com.tofu.mvp.smart;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.tofu.mvp.R;
import com.tofu.mvp.gain.Gain;
import com.tofu.mvp.gain.exception.ExceptionHandler;
import com.tofu.mvp.gain.exception.UnKnowException;
import com.tofu.mvp.gain.trust.SSLSocketClient;
import com.tofu.mvp.util.CollectionUtils;
import com.tofu.mvp.util.Print;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by wxl on 2019/7/9.
 */

public class SmartView<T> extends SmartRefreshLayout {

    private static Option option = new Option();

    private Class<T> clazz;

    private SmartCallback<T> callback;

    private boolean isPost = true;

    private String url;

    private HashMap<String, String> params = new HashMap<>();

    private HttpHeaders headers = new HttpHeaders();

    private String pageKey = "pageNo";

    private int start_page_index = 0;

    private int page = 0;

    private boolean isRefresh = true;

    private boolean isLastPage = false;

    private String beanName;

    public SmartView(Context context) {
        this(context, null);
    }

    public SmartView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
        paresAttr(context, attrs);
    }

    public SmartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paresAttr(context, attrs);
    }

    private void paresAttr(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SmartView);
            beanName = typedArray.getString(R.styleable.SmartView_bean);
            pageKey = typedArray.getString(R.styleable.SmartView_pageKey);
            url = typedArray.getString(R.styleable.SmartView_url);
            start_page_index = typedArray.getIndex(R.styleable.SmartView_start_page);
            page = start_page_index;
            typedArray.recycle();
        }
    }

    /**
     * 设置参数
     *
     * @param key
     * @param value
     * @param
     * @return
     */
    public SmartView<T> put(String key, Object value) {
        if (value == null) {
            value = "null";
        }
        params.put(key, value.toString());
        return this;
    }


    /**
     * 设置参数
     *
     * @param key
     * @param value
     * @param
     * @return
     */
    public SmartView<T> add(String key, Object value, boolean isAdd) {
        if (value == null) {
            value = "null";
        }
        if (isAdd) {
            params.put(key, value.toString());
        } else {
            remove(key);
        }
        return this;
    }


    /**
     * 移除参数
     * @param key
     * @return
     */
    public SmartView<T> remove(String key){
        boolean b = params.containsKey(key);
        if (b) {
            params.remove(key);
        }
        return this;
    }

    /**
     * 设置参数
     *
     * @param param
     * @return
     */
    public SmartView<T> put(Map<String, Object> param) {
        if (CollectionUtils.isNotEmpty(param)) {
            Iterator<Map.Entry<String, Object>> iterator = param.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                Object value = next.getValue();
                if (value == null) {
                    value = "null";
                }
                params.put(next.getKey(), value.toString());
            }
        }
        return this;
    }


    /**
     * 设置请求头
     *
     * @param key
     * @param value
     * @return
     */
    public SmartView<T> header(String key, String value) {
        headers.put(key, value);
        return this;
    }


    /**
     * 设置页数
     *
     * @param key
     * @param startIndex
     * @return
     */
    public SmartView<T> setPage(String key, int startIndex) {
        this.pageKey = key;
        this.start_page_index = startIndex;
        this.page = startIndex;
        return this;
    }


    /**
     * 不分页
     *
     * @return
     */
    public SmartView<T> noPage() {
        this.pageKey = null;
        return this;
    }



    /**
     * 获取当前页
     *
     * @return
     */
    public int getCurrentPageNo() {
        return page;
    }


    /**
     * post请求
     *
     * @return
     */
    public void post(SmartCallback<T> callback) {
        this.callback = callback;
        isPost = true;
        setOnRefreshLoadMoreListener();
    }

    /**
     * get请求
     */
    public void get(SmartCallback<T> callback) {
        this.callback = callback;
        isPost = false;
        setOnRefreshLoadMoreListener();
    }

    /**
     * 清除所有参数
     */
    public SmartView<T> clear() {
        params.clear();
        return this;
    }

    /**
     * 清除所有请求头
     */
    public SmartView<T> clearHeaders() {
        headers.clear();
        return this;
    }

    /**
     * 设置监听
     */
    private void setOnRefreshLoadMoreListener() {
        setOnRefreshLoadMoreListener(onRefreshLoadMoreListener);
    }


    /**
     * 刷新加载监听
     */
    private OnRefreshLoadMoreListener onRefreshLoadMoreListener = new OnRefreshLoadMoreListener() {
        @Override
        public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            loadSmart(false);
        }

        @Override
        public void onRefresh(@NonNull RefreshLayout refreshLayout) {
            loadSmart(true);
        }
    };


    /**
     * 加载
     */
    private void loadSmart(boolean isRefresh) {
        this.isRefresh = isRefresh;
        if (!TextUtils.isEmpty(pageKey)) {
            if (isRefresh) {
                page = start_page_index;
            } else {
                ++page;
            }
            params.put(pageKey, page + "");
        }
        if (isPost) {
            onPostRequest();
        } else {
            onGetRequest();
        }
    }


    /**
     * post请求
     */
    private void onPostRequest() {
        OkGo.post(url)
                .tag(url)
                .writeTimeOut(Gain.Option.getHttpConnectOutTime() * 1000)
                .readTimeOut(Gain.Option.getHttpConnectOutTime() * 1000)
                .connTimeOut(Gain.Option.getHttpConnectOutTime() * 1000)
                .upJson(JSONObject.toJSONString(params))
                .setHostnameVerifier(SSLSocketClient.getHostnameVerifier())
                //.addInterceptor(new AccessTokenInterceptor())
                .headers(headers)
                //.params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        finishRefresh();
                        finishLoadMore();
                        if (response.isSuccessful()) {
                            onSuccessCallback(s);
                        } else {
                            UnKnowException e = new UnKnowException(response.message(), response.code());
                            onErrorCallback(e);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        finishRefresh(false);
                        finishLoadMore(false);
                        onErrorCallback(e);
                    }
                });

    }


    /**
     * get请求
     */
    private void onGetRequest() {
        HttpParams httpParams = new HttpParams();
        httpParams.put(params);
        OkGo.get(url)
                .tag(url)
                .writeTimeOut(Gain.Option.getHttpConnectOutTime() * 1000)
                .readTimeOut(Gain.Option.getHttpConnectOutTime() * 1000)
                .connTimeOut(Gain.Option.getHttpConnectOutTime() * 1000)
                .setHostnameVerifier(SSLSocketClient.getHostnameVerifier())
                //.addInterceptor(new AccessTokenInterceptor())
                .headers(headers)
                .params(httpParams)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        finishRefresh();
                        finishLoadMore();
                        if (response.isSuccessful()) {
                            onSuccessCallback(s);
                        } else {
                            UnKnowException e = new UnKnowException(response.message(), response.code());
                            onErrorCallback(e);
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        finishRefresh();
                        finishLoadMore();
                        onErrorCallback(e);
                    }
                });
    }


    /**
     * 错误回调
     *
     * @param e
     */
    private void onErrorCallback(Exception e) {
        onErrorSmartPageNoChanged();
        if (callback != null) {
            callback.isDrop = isRefresh;
            callback.onFailed(e.getMessage(), isRefresh);
        }
        ExceptionHandler.OnExceptionCallback exceptionCallback = Gain.option().getExceptionCallback();
        if (exceptionCallback != null) {
            exceptionCallback.handlerException(e);
        }
    }

    /**
     * 失败时改变当前请求的页数
     */
    private void onErrorSmartPageNoChanged() {
        if (!TextUtils.isEmpty(pageKey)) {
            if (!isRefresh) {
                if (page > start_page_index) {
                    --page;
                }
            }
            params.put(pageKey, page + "");
        }
    }


    /**
     * 成功回调
     *
     * @param s
     */
    private void onSuccessCallback(String s) {
        if (!TextUtils.isEmpty(s)) {
            if (callback != null) {
                if (clazz == null) clazz = findProClass();
                if (clazz == null || clazz == String.class) {
                    isLastPage = callback.onSuccess((T) s, isRefresh);
                } else {
                    try {
                        T t = JSONObject.parseObject(s, clazz);
                        if (t != null) {
                            callback.isDrop = isRefresh;
                            isLastPage = callback.onSuccess(t, isRefresh);
                        } else {
                            Print.e( " json parse object error is null format json : \n" + s);
                            UnKnowException e = new UnKnowException("转换异常，请检查类型与数据格式", -101);
                            onErrorCallback(e);
                        }
                    } catch (Exception e) {
                        Print.e( " json parse object error is null format json : \n" + s);
                        onErrorCallback(e);
                    }
                }
                notifySmartViewLoadMoreFinishChanged(isLastPage);
            }
        }
    }


    /**
     * 如果是最后一页，上拉显示已没有更多
     *
     * @param isLastPage
     */
    private void notifySmartViewLoadMoreFinishChanged(boolean isLastPage) {
        setNoMoreData(isLastPage);
        if (isLastPage) {
            finishSmartLoadMoreWithNoMoreData();
        }
    }

    /**
     * 找到pro
     *
     * @return
     */
    private Class<T> findProClass() {
        if (TextUtils.isEmpty(beanName)) {
            return null;
        }
        try {
            return (Class<T>) Class.forName(beanName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 请求链接
     *
     * @param url
     * @return
     */
    public SmartView<T> url(String url) {
        this.url = url;
        return this;
    }


    public SmartView<T> clazz(Class<T> clazz) {
        this.clazz = clazz;
        return this;
    }

    /**
     * 已没有更多数据
     */
    public void finishSmartLoadMoreWithNoMoreData() {
        finishLoadMoreWithNoMoreData();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        onDestroy();
    }

    /**
     * 销毁
     */
    private void onDestroy() {
        OkGo.getInstance().cancelTag(url);
        start_page_index = 0;
        page = 0;
        isRefresh = true;
        isPost = true;
        pageKey = "pageNo";
        clazz = null;
        callback = null;
        params.clear();
        headers.clear();
    }


    /**
     * 配置
     */
    public static class Option {

        private OkHttpClient.Builder builder;

        private Option() {
            builder = OkGo.getInstance().getOkHttpClientBuilder();
            builder.addInterceptor(new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY))
                    .retryOnConnectionFailure(true)
                    .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                    .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
                    .connectTimeout(Gain.Option.getHttpConnectOutTime(), TimeUnit.SECONDS)
                    .writeTimeout(Gain.Option.getHttpConnectOutTime(), TimeUnit.SECONDS)
                    .readTimeout(Gain.Option.getHttpConnectOutTime(), TimeUnit.SECONDS);
        }

        /**
         * 添加拦截器<p>
         * ● 不需要担心中间过程的响应,如重定向和重试.<p>
         * ● 总是只调用一次,即使HTTP响应是从缓存中获取.<p>
         * ● 观察应用程序的初衷. 不关心OkHttp注入的头信息如: If-None-Match.<p>
         * ● 允许短路而不调用 Chain.proceed(),即中止调用.<p>
         * ● 允许重试,使 Chain.proceed()调用多次.<p>
         * ● 已与Gain同步添加拦截器
         *
         * @param interceptor
         * @return
         */
        public Option addInterceptor(Interceptor interceptor) {
            builder.addInterceptor(interceptor);
            return this;
        }


        /**
         * 添加网络拦截器<p>
         * ● 能够操作中间过程的响应,如重定向和重试.<p>
         * ● 当网络短路而返回缓存响应时不被调用.<p>
         * ● 只观察在网络上传输的数据.<p>
         * ● 携带请求来访问连接.<p>
         * ● 已与Gain同步添加拦截器
         *
         * @param interceptor
         * @return
         */
        public Option addNetworkInterceptor(Interceptor interceptor) {
            builder.addNetworkInterceptor(interceptor);
            return this;
        }

    }

    /**
     * 配置
     *
     * @return
     */
    public static Option option() {
        return option;
    }
}
