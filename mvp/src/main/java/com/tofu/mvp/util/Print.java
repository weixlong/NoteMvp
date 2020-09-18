package com.tofu.mvp.util;

import android.util.Log;

public class Print {

    private static String TAG = Print.class.getSimpleName();

    public static boolean debug = true;

    private static int LENGTH = 2000;

    public static void d(String msg){
        if(debug) {
            if (msg.length() > LENGTH) {
                for (int i = 0; i < msg.length(); i += LENGTH) {
                    if (i + LENGTH < msg.length()) {
                        Log.i(TAG, msg.substring(i, i + LENGTH));
                    } else {
                        Log.i(TAG, msg.substring(i, msg.length()));
                    }
                }
            } else {
                Log.i(TAG, msg);
            }
        }
    }

    public static void d(Object o){
        if(debug){
            if(o == null){
                Log.d(TAG, "null");
            } else {
                Log.d(TAG, o.toString());
            }
        }
    }

    public static void e(String msg){
        if(debug) {
            if (msg.length() > LENGTH) {
                for (int i = 0; i < msg.length(); i += LENGTH) {
                    if (i + LENGTH < msg.length()) {
                        Log.e(TAG, msg.substring(i, i + LENGTH));
                    } else {
                        Log.e(TAG, msg.substring(i, msg.length()));
                    }
                }
            } else {
                Log.e(TAG, msg);
            }
        }
    }
}
