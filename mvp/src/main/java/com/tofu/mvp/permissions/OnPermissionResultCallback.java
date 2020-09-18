package com.tofu.mvp.permissions;

public interface OnPermissionResultCallback {
    void onRequestPermissionSuccess(int requestCode);
    void onRequestPermissionFailed(int requestCode);
}
