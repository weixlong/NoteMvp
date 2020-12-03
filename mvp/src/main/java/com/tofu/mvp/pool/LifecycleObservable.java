package com.tofu.mvp.pool;

import com.tofu.mvp.base.BaseGainMvpFragmentLifecycle;
import com.tofu.mvp.base.BaseGameMvpLifecycle;
import com.tofu.mvp.util.CollectionUtils;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * create file time : 2020/12/3
 * create user : wxl
 * subscribe :
 */
public class LifecycleObservable {

    private static class LO {
       public static LifecycleObservable mLifecycleObservable = new LifecycleObservable();
    }

    private LifecycleObservable() {
    }

    public static LifecycleObservable get(){
        return LO.mLifecycleObservable;
    }

    /**
     * 生命周期回调
     */
    private HashMap<Class, List<BaseGameMvpLifecycle>> lifecycles = new HashMap<>();

    /**
     * 生命周期添加标记
     */
    private List<String> lifecycleTags = new ArrayList<>();

    /**
     * 生命周期Disposable
     */
    private static HashMap<Class, Disposable> lifecycleDisposables = new HashMap<>();

    /**
     * 回调对应的生命周期
     * @param observable
     * @param lifecycle
     */
    public synchronized void observableActivityEvent(Class key,Observable<ActivityEvent> observable, BaseGameMvpLifecycle lifecycle){
        if(lifecycleTags.contains(lifecycle.getClass().getName())){
            return;
        }
        synchronized (this){
            if(lifecycleTags.contains(lifecycle.getClass().getName())){
                return;
            }
        }
        lifecycle.onMvpAttach();
        addLifecycle(key,lifecycle);
        lifecycleDisposables.put(key,observable.subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ActivityEvent>() {
                    @Override
                    public void accept(ActivityEvent activityEvent) throws Exception {
                        if(lifecycle != null) {
                            switch (activityEvent) {
                                case RESUME:
                                    lifecycle.onResume();
                                    break;
                                case PAUSE:
                                    lifecycle.onPause();
                                    break;
                                case STOP:
                                    lifecycle.onStop();
                                    break;
                            }
                        }
                    }
                }));
    }


    /**
     * 回调对应的生命周期
     * @param observable
     * @param lifecycle
     */
    public synchronized void observableFragmentEvent(Class key,Observable<FragmentEvent> observable, BaseGainMvpFragmentLifecycle lifecycle){
        if(lifecycleTags.contains(lifecycle.getClass().getName())){
            return;
        }
        synchronized (this){
            if(lifecycleTags.contains(lifecycle.getClass().getName())){
                return;
            }
        }
        lifecycle.onMvpAttach();
        addLifecycle(key,lifecycle);
        lifecycleDisposables.put(key, observable.subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<FragmentEvent>() {
                    @Override
                    public void accept(FragmentEvent fragmentEvent) throws Exception {
                        if(lifecycle != null){
                            switch (fragmentEvent) {
                                case RESUME:
                                    lifecycle.onResume();
                                    break;
                                case PAUSE:
                                    lifecycle.onPause();
                                    break;
                                case STOP:
                                    lifecycle.onStop();
                                    break;
                                case DESTROY_VIEW:
                                    lifecycle.onDestroyView();
                                    break;
                                case DETACH:
                                    lifecycle.onDetach();
                                    break;
                            }
                        }
                    }
                }));
    }



    private synchronized void addLifecycle(Class key, BaseGameMvpLifecycle lifecycle){
        lifecycleTags.add(lifecycle.getClass().getName());
        if(lifecycles.containsKey(key)){
            List<BaseGameMvpLifecycle> baseLifecycles = lifecycles.get(key);
            if(CollectionUtils.isNull(baseLifecycles)){
                baseLifecycles = new ArrayList<>();
            }
            baseLifecycles.add(lifecycle);
        } else {
            List<BaseGameMvpLifecycle> baseLifecycles = new ArrayList<>();
            baseLifecycles.add(lifecycle);
            lifecycles.put(key,baseLifecycles);
        }
    }

    public void unAttachLifecycle(Class key){
        if(lifecycles.containsKey(key)){
            List<BaseGameMvpLifecycle> baseLifecycles = lifecycles.remove(key);
            if(CollectionUtils.isNotEmpty(baseLifecycles)){
                for (BaseGameMvpLifecycle lifecycle : baseLifecycles) {
                    if(lifecycleTags.contains(lifecycle.getClass().getName())){
                        lifecycleTags.remove(lifecycle.getClass().getName());
                    }
                    lifecycle.onMvpDetach();
                }
                baseLifecycles.clear();
            }
        }
        if(lifecycleDisposables.containsKey(key)){
            Disposable disposable = lifecycleDisposables.remove(key);
            if(disposable != null){
                disposable.dispose();
            }
        }
    }


}
