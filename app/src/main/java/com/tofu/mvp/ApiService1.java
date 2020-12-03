package com.tofu.mvp;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by wxl on 2020/5/11.
 */
public interface ApiService1 {

    @FormUrlEncoded
    @POST("/")
    Observable<String> getUser(@Field("service") String service);
}
