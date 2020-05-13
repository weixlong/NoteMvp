package com.tofu.mvp.util;

import android.app.Activity;

import com.tofu.mvp.base.BaseActivity;

import java.util.Iterator;
import java.util.Stack;

public class ActivityManager {

    private static Stack<Activity> mActivityStack;    //Activity栈

    private static class I {
        private static ActivityManager m = new ActivityManager();
    }

    private ActivityManager() {
        mActivityStack = new Stack<>();
    }

    public static ActivityManager getInstance(){
        return I.m;
    }

    /**
     * 添加一个activity到栈顶.
     *
     * @param activity 添加的activity
     */
    public void pushActivity(Activity activity) {
        mActivityStack.push(activity);
    }

    /**
     * 获取栈顶的Activity.
     *
     * @return 如果栈存在, 返回栈顶的activity
     */
    public Activity peekActivity() {
        if (mActivityStack != null && !mActivityStack.isEmpty()) {
            return mActivityStack.peek();
        } else {
            return null;
        }
    }

    /**
     * 结束当前的activity,在activity的onDestroy中调用.
     */
    public void popActivity() {
        if (mActivityStack != null && !mActivityStack.isEmpty()) {
            mActivityStack.pop();
        }
    }

    /**
     * 结束最接近栈顶的匹配类名的activity.
     * 遍历到的不一定是被结束的,遍历是从栈底开始查找,为了确定栈中有这个activity,并获得一个引用
     * 删除是从栈顶查找,结束查找到的第一个
     * 在activity外结束activity时调用
     *
     * @param klass 类名
     */
    public void popActivity(Class<? extends Activity> klass) {
        Iterator<Activity> iterator = mActivityStack.iterator();
        while(iterator.hasNext()){
            Activity activity = iterator.next();
            if (activity != null && activity.getClass().equals(klass)) {
                iterator.remove();   //注意这个地方
                activity.finish();
            }
        }

    }


    public Activity findActivity(Class<? extends Activity> klass) {
        Iterator<Activity> iterator = mActivityStack.iterator();
        while(iterator.hasNext()){
            Activity activity = iterator.next();
            if (activity != null && activity.getClass().equals(klass)) {
                return activity;
            }
        }
        return null;
    }


    public boolean cantains(Class<? extends Activity> klass){
        for (Activity activity : mActivityStack) {
            if (activity != null && activity.getClass().equals(klass)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 结束最接近栈顶的匹配类名的activity.
     * 遍历到的不一定是被结束的,遍历是从栈底开始查找,为了确定栈中有这个activity,并获得一个引用
     * 删除是从栈顶查找,结束查找到的第一个
     * 在activity外结束activity时调用
     *
     * @param klass 类名
     */
    public void popActivitys(Class<? extends BaseActivity> ... klass) {
        for (Class<? extends BaseActivity> aClass : klass) {
            for (Activity activity : mActivityStack) {
                if (activity != null && activity.getClass().equals(aClass)) {
                    mActivityStack.remove(activity);
                    activity.finish();
                    break;              //只结束一个
                }
            }
        }
    }

    //移除所有的Activity
    public void removeAll(){
        while (!mActivityStack.empty()){
            Activity activity = mActivityStack.pop();
            if (activity != null) {
                activity.finish();
            }
        }
    }


    public void exit(){
        removeAll();
        System.exit(0);
    }

}
