package com.tofu.mvp.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.tofu.mvp.event.SnackEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by wxl on 2019/7/2.
 */

public class Tu {

    private static Tu tu;

    private static Context context;

    public static void initialize(Context context) {
        Tu.context = context.getApplicationContext();
    }


    public static Tu get() {
        synchronized (Tu.class) {
            if (tu == null) {
                tu = new Tu();
            }
        }
        return tu;
    }


    public void tu(String text) {
        try {
            if (!TextUtils.isEmpty(text)) {
                Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

        }
    }

    public void tu(int sid) {
        try {
            String string = context.getResources().getString(sid);
            if (!TextUtils.isEmpty(string)) {
                Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

        }
    }


    public void ta(String text) {
        try {
            if (!TextUtils.isEmpty(text)) {
                Toast.makeText(context, text, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {

        }
    }


    /**
     * 显示提示
     * @param msg
     */
    public void snack(String msg){
        EventBus.getDefault().post(new SnackEvent(msg));
    }

    /**
     * 显示提示
     *
     *
     * @param msg
     */
    public void snack(String msg,boolean isShot){
        EventBus.getDefault().post(new SnackEvent(!isShot,msg));
    }

//    /**
//     * 使用snackbar显示内容
//     *
//     * @param
//     * @param
//     */
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void showSnackbar(SnackEvent event) {
//        View view = this.getWindow().getDecorView().findViewById(android.R.id.content);
//        Snackbar bar= Snackbar.make(view, event.getMessage(), event.isLong() ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT);
//        bar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.goldenrodColor));
//        bar.show();
//    }
}
