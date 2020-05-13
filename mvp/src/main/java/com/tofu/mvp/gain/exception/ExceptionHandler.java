package com.tofu.mvp.gain.exception;


/**
 * Created by wxl on 2019/5/23.
 */

public class ExceptionHandler {


    private static OnExceptionCallback onExceptionCallback;


    /**
     * 错误回调
     * @param onExceptionCallback
     */
    public static void exceptionCallback(OnExceptionCallback onExceptionCallback){
        ExceptionHandler.onExceptionCallback = onExceptionCallback;
    }

    /**
     * 错误处理
     *
     * @param e
     */
    public static boolean handlerException(Throwable e) {
        //CrashReport.postCatchedException(e);  //throwable上报
        if(onExceptionCallback != null){
           return onExceptionCallback.handlerException(e);
        }
        return false;
//        //加载完成
//        if (e instanceof HttpException) {
//            HttpException httpException = (HttpException) e;
//            if (httpException.code() == 401 || httpException.code() == -10002 || httpException.code() == -10000) {//token过期
//                Log.d("HttpException", "EventBus.getDefault().post(new ExitLoginEvent())");
//                EventBus.getDefault().post(new ExitLoginEvent());
//            }
//            return;
//        }
//
//        if (e instanceof UnKnowException) {
//            UnKnowException unKnowException = (UnKnowException) e;
//            if (unKnowException.getCode() == 401 || unKnowException.getCode() == -10002 || unKnowException.getCode() == -10000) {//token过期
//                Log.d("UnKnowException", "EventBus.getDefault().post(new ExitLoginEvent())");
//                EventBus.getDefault().post(new ExitLoginEvent());
//            }
//            return;
//        }

//        if (e instanceof ConnectException) {
//            //这里java.net.ConnectException: Failed to connect to 错误与503错误一起处理
//
//            return;
//        }
//        if (e instanceof SocketTimeoutException) {
//            //这里java.net.SocketTimeoutException: failed to connect to 错误与503错误一起处理
//            return;
//        }
//        if (e instanceof JsonSyntaxException) {
//            //json解析错误，一般是后台返回的实体类改变
//            return;
//        }
//        if (e instanceof UnknownHostException) {
//            //无法解析主机,一般出现服务区域名错误或者用户未联网
//            return;
//        }
    }


    public interface OnExceptionCallback {
        boolean handlerException(Throwable e);
    }
}
