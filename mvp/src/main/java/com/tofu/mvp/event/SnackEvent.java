package com.tofu.mvp.event;

import lombok.Data;

@Data
public class SnackEvent {
    private boolean isLong;
    private String message;

    public SnackEvent(boolean isLong, String message) {
        this.isLong = isLong;
        this.message = message;
    }

    public SnackEvent(String message) {
        this.message = message;
    }
}
