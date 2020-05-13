package com.tofu.mvp.gain.exception;

/**
 * Created by wxl on 2019/5/23.
 */

public class UnKnowException extends Exception {

    private int code;

    public UnKnowException() {
    }

    public UnKnowException(String message, int code) {
        super(message);
        this.code = code;
    }

    public UnKnowException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnKnowException(Throwable cause) {
        super(cause);
    }

    public int getCode() {
        return code;
    }
}
