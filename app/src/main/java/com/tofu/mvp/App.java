package com.tofu.mvp;
import android.app.Application;

import com.tofu.mvp.gain.Gain;

/**
 * Created by wxl on 2020/5/11.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Gain.option().api(ApiService.class)
                .baseUrl("http://192.168.1.8:8084/user/")
                .okCode(0)
                .setConnectOutTime(20000)
                .build();
    }

//    public static ApiService api(){
//        return Gain.api();
//    }
}
