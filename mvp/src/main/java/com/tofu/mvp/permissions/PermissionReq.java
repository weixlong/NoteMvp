package com.tofu.mvp.permissions;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.HashMap;

public class PermissionReq {

    private static HashMap<String,Builder> cacheBuilder = new HashMap<>();

    public static synchronized Builder instance(FragmentActivity activity){
        synchronized (PermissionReq.class) {
            if (!cacheBuilder.containsKey(activity.getClass().getName())) {
                synchronized (PermissionReq.class) {
                    if (!cacheBuilder.containsKey(activity.getClass().getName())) {
                        cacheBuilder.put(activity.getClass().getName(), new Builder(activity));
                    }
                }
            }
            return cacheBuilder.get(activity.getClass().getName());
        }
    }


    /**
     * 检查是否具有多个权限
     *
     * @param permissions
     * @return true 有权限 false无权限
     */
    public static boolean checkPermissions(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    public static class Builder {
        private FragmentActivity activity;
        private String[] pers;
        private int reqCode;
        private OnPermissionResultCallback onPermissionResultCallback;
        private PermissionsFragment permissionsFragment;

        private Builder(FragmentActivity activity) {
            this.activity = activity;
        }


        protected String[] getPers() {
            return pers;
        }

        protected int getReqCode() {
            return reqCode;
        }

        protected OnPermissionResultCallback getOnPermissionResultCallback() {
            return onPermissionResultCallback;
        }

        public Builder permissions(String... pers) {
            this.pers = pers;
            return this;
        }

        public Builder requestCode(int code) {
            this.reqCode = code;
            return this;
        }

        public Builder callback(OnPermissionResultCallback callback) {
            this.onPermissionResultCallback = callback;
            return this;
        }

        public void req() {
            if (pers == null || pers.length == 0 || activity == null) {
                if(onPermissionResultCallback != null){
                    onPermissionResultCallback.onRequestPermissionSuccess(reqCode);
                }
            } else {
                if(permissionsFragment == null) {
                    permissionsFragment = new PermissionsFragment();
                    permissionsFragment.setBuilder(this);
                    activity.getSupportFragmentManager().beginTransaction().attach(permissionsFragment).show(permissionsFragment).commitAllowingStateLoss();
                } else {
                    permissionsFragment.requestPermissions(activity,pers,reqCode,onPermissionResultCallback);
                }
            }
        }

        public void release(){
            if(activity != null){
                activity.getSupportFragmentManager().beginTransaction().detach(permissionsFragment).commitAllowingStateLoss();
            }
            activity = null;
            permissionsFragment = null;
            pers = null;
            onPermissionResultCallback = null;
        }
    }

}
