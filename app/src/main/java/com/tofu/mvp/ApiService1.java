package com.tofu.mvp;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by wxl on 2020/5/11.
 */
public interface ApiService1 {

    @GET("api/anchor/push-stream")
    Observable<String> getUser();
}
