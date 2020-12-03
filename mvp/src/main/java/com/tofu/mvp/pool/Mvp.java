package com.tofu.mvp.pool;

import com.tofu.mvp.base.BaseGainMvpFragmentLifecycle;
import com.tofu.mvp.base.BaseGameMvpLifecycle;
import com.tofu.mvp.gain.Gain;
import com.tofu.mvp.note.GainApi;
import com.tofu.mvp.note.GainLifecycle;
import com.tofu.mvp.note.Model;
import com.tofu.mvp.note.PMTarget;
import com.tofu.mvp.note.Presenter;
import com.tofu.mvp.note.View;
import com.tofu.mvp.util.Print;
import com.trello.rxlifecycle2.LifecycleProvider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by wxl on 2020/5/9.
 */
public class Mvp {


    /**
     * 容器
     */
    private static HashMap<Class, PO> container = new HashMap<>();

    /**
     * 生命周期容器
     */
    private static HashMap<Class, LFP> lps = new HashMap<>();

    /**
     * 生命周期跟随容器
     */
    private static HashMap<Class, Class[]> attach = new HashMap<>();

    /**
     * 生命周期接管
     */
    private static HashMap<Class, Class> takeoverKeys = new HashMap<>();

    /**
     * 生命周期事件
     */
    private static HashMap<Class, Class> lifecycleKeys = new HashMap<>();




    /**
     * @param v 绑定一个实体View 实现 ，如：Activity,Fragment等。
     *          在View上使用注解@Presenter和@Model指定对应的接口和实现。
     */
    public static <V> void bind(V v) {
        if (v == null) return;
        Annotation[] annotations = v.getClass().getAnnotations();
        container.put(v.getClass(), new PO(v.getClass(), v));
        if (annotations != null || annotations.length >= 0) {
            for (int i = 0; i < annotations.length; i++) {
                Annotation annotation = annotations[i];
                if (annotation instanceof PMTarget) {
                    PMTarget mpTarget = (PMTarget) annotation;
                    Class<?>[] target_ps = mpTarget.p();
                    Class<?>[] target_ms = mpTarget.m();
                    Class<?>[] target_vs = mpTarget.v();
                    parseModelAnnotation(v, target_ms);
                    parsePresenterAnnotation(v, target_ps);
                    startParseBindAnnotation(v, target_vs, target_ps, target_ms);
                    return;
                }
            }
            Print.e("没有在" + v.getClass().getName() + "上找到PMTarget注解，将无法正常使用MVP。");
        }
    }


    /**
     * 解析Model层
     *
     * @param target_ms
     */
    private static <V> void parseModelAnnotation(V v, Class<?>[] target_ms) {
        for (int i = 0; i < target_ms.length; i++) {
            Class target_m = target_ms[i];
            if (target_m != null) {
                Object object = findObjectByClass(target_m);
                if (object == null) {
                    container.put(target_m, new PO(v.getClass(), newInstanceByClass(target_m)));
                }
            }
        }
    }

    /**
     * 解析P层
     *
     * @param v
     * @param target_ps
     * @param <V>
     */
    private static <V> void parsePresenterAnnotation(V v, Class<?>[] target_ps) {
        for (int i = 0; i < target_ps.length; i++) {
            Class target_p = target_ps[i];
            if (target_p != null) {
                Object object = findObjectByClass(target_p);
                if (object == null) {
                    container.put(target_p, new PO(v.getClass(), newInstanceByClass(target_p)));
                }
            }
        }
    }


    private static <V> void startParseBindAnnotation(V v, Class<?>[] target_vs, Class<?>[] target_ps, Class<?>[] target_ms) {
        List<Field> fields = scanField(v, v);

        if (v instanceof LifecycleProvider) {
            addLife(v.getClass(), null, (LifecycleProvider) v);
        }

        setAnnotation(v, v, fields, target_vs, target_ps, target_ms);

        addObservableEvent(v.getClass(), v);

        for (int i = 0; i < target_ps.length; i++) {
            PO po = container.get(target_ps[i]);
            List<Field> fieldps = scanField(v, po.object);
            setAnnotation(v, po.object, fieldps, target_vs, target_ps, target_ms);
            addObservableEvent(v.getClass(), po.object);
        }
        for (int i = 0; i < target_ms.length; i++) {
            PO po = container.get(target_ms[i]);
            List<Field> fieldms = scanField(v, po.object);
            setAnnotation(v, po.object, fieldms, target_vs, target_ps, target_ms);
            addObservableEvent(v.getClass(), po.object);
        }


    }


    /**
     * 添加生命周期事件监听
     *
     * @param key
     * @param
     * @param
     */
    private static void addObservableEvent(Class key, Object o) {
        LifecycleProvider life = getAnnotationLife(o.getClass());
        if (life != null) {
            if (o instanceof BaseGainMvpFragmentLifecycle) {
                BaseGainMvpFragmentLifecycle callback = (BaseGainMvpFragmentLifecycle) o;
                LifecycleObservable.get().observableFragmentEvent(key,life.lifecycle(), callback);
            } else if (o instanceof BaseGameMvpLifecycle) {
                BaseGameMvpLifecycle callback  = (BaseGameMvpLifecycle) o;
                LifecycleObservable.get().observableActivityEvent(key,life.lifecycle(), callback);
            }
        }
    }



    private static <V> void setAnnotation(V v, Object o, List<Field> fields, Class<?>[] target_vs, Class<?>[] target_ps, Class<?>[] target_ms) {
        for (Field field : fields) {

            try {
                setValueToField(field, o, v);
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int i = 0; i < target_vs.length; i++) {
                PO po = container.get(target_vs[i]);
                if (po != null) {
                    try {
                        setValueToField(field, o, po.object);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            for (int i = 0; i < target_ps.length; i++) {
                PO po = container.get(target_ps[i]);
                try {
                    setValueToField(field, o, po.object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < target_ms.length; i++) {
                PO po = container.get(target_ms[i]);
                try {
                    setValueToField(field, o, po.object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 扫描符合注解的字段
     *
     * @param o
     * @return
     */
    private static <V> List<Field> scanField(V v, Object o) {
        List<Field> afields = new ArrayList<>();
        Field[] fields = o.getClass().getDeclaredFields();
        scanLifecycle(o.getClass());
        for (int i = 0; i < fields.length; i++) {

            Field field = fields[i];

            View annotationView = field.getAnnotation(View.class);

            Presenter annotationPresenter = field.getAnnotation(Presenter.class);

            Model annotationModel = field.getAnnotation(Model.class);

            GainApi annotationApi = field.getAnnotation(GainApi.class);

            if (annotationView != null || annotationPresenter != null
                    || annotationModel != null || annotationApi != null) {
                afields.add(field);
            }

        }
        return afields;
    }


    private static void scanLifecycle(Class t) {
        Annotation[] annotations = t.getAnnotations();
        if (annotations != null || annotations.length >= 0) {
            for (int i = 0; i < annotations.length; i++) {
                Annotation annotation = annotations[i];
                if (annotation instanceof GainLifecycle) {
                    GainLifecycle lifecycle = (GainLifecycle) annotation;
                    Class key = lifecycle.lifeKey();
                    lifecycleKeys.put(t, key);
                    return;
                }
            }
        }
    }


    /**
     * 取出已经存在并且未销毁的对象
     *
     * @param key
     * @return
     */
    private static PO findObjectByClass(Class key) {
        return container.get(key);
    }


    /**
     * 绑定MVP生命周期
     *
     * @param v
     * @param lifeKey
     * @param life
     * @param <V>
     * @param
     */
    public static <V> void bind(V v, Class lifeKey, LifecycleProvider life) {
        bind(v);
        addLife(lifeKey, null, life);
    }


    /**
     * 获得MVP注解里的生命周期
     *
     * @param t
     * @return
     */
    public static LifecycleProvider getAnnotationLife(Class t) {
        boolean b = lifecycleKeys.containsKey(t);
        if (b) {
            Class key = lifecycleKeys.get(t);
            LifecycleProvider life = getLife(key);
            if (life != null) {
                return life;
            }
        }
        return findLifecycleProviderAttach(t);
    }


    /**
     * 添加生命周期跟随
     *
     * @param key
     * @param ats
     */
    public static void addLifeAttach(Class key, Class[] ats) {
        if (ats != null && ats.length > 0) {
            if (attach.containsKey(key)) {
                Class[] concat = concat(attach.get(key), ats);
                attach.put(key, concat);
            } else {
                attach.put(key, ats);
            }
        }
    }


    public static void addTakeOverLifeKeyToNewKey(Class k, Class k1) {
        takeoverKeys.put(k, k1);
    }


    public static void removeTakeOverLifeKey(Class k) {
        if (takeoverKeys.containsValue(k)) {
            Iterator<Map.Entry<Class, Class>> iterator = takeoverKeys.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Class, Class> next = iterator.next();
                if (next.getValue() == k) {
                    takeoverKeys.remove(next.getKey());
                    return;
                }
            }
        }
    }


    private static Class[] concat(Class[] first, Class[] second) {
        Class[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }


    private static LifecycleProvider findLifecycleProviderAttach(Class t) {
        Iterator<Map.Entry<Class, Class[]>> iterator = attach.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Class, Class[]> next = iterator.next();
            Class[] value = next.getValue();
            for (int i = 0; i < value.length; i++) {
                if (t == value[i]) {
                    if (lps.containsKey(next.getKey())) {
                        return lps.get(next.getKey()).life;
                    }
                }
            }
        }
        return null;
    }


    /**
     * 获得已注册的生命周期对象
     *
     * @param key
     * @return
     */
    public static LifecycleProvider getLife(Class key) {
        if (takeoverKeys.containsKey(key)) {
            Class newKey = takeoverKeys.get(key);
            if (newKey != null) {
                if (lps.containsKey(newKey)) {
                    return lps.get(newKey).life;
                }
            }
        }
        if (lps.containsKey(key)) {
            return lps.get(key).life;
        }
        return null;
    }


    /**
     * 移除生命周期
     *
     * @param key
     */
    public static void removeLife(Class key) {
        if (lps.containsKey(key)) {
            LFP lfp = lps.get(key);
            lfp.life = null;
            lfp.main = null;
            if (lfp.with != null) {
                for (int i = 0; i < lfp.with.length; i++) {
                    lfp.with[i] = null;
                }
            }
            lps.remove(key);
        }
    }


    /**
     * 添加生命周期
     *
     * @param life
     */
    public static void addLife(Class key, Class[] with, LifecycleProvider life) {
        if (!lps.containsKey(key)) {
            LFP lfp = new LFP(key, with, life);
            lps.put(key, lfp);
        }
    }


    /**
     * new object
     *
     * @param tClass
     * @param <T>
     * @return
     */
    private static <T> T newInstanceByClass(Class<T> tClass) {
        try {
            Constructor<T> constructor = tClass.getConstructor();
            constructor.setAccessible(true);
            return tClass.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 设置Gain api
     *
     * @param o
     * @param field
     */
    private static void setApi(Object o, Field field) {
        try {
            Object api = Gain.api(field.getType());
            if (api == null) {
                Print.e("Gain api is null, please init Gain and set api class.");
            } else {
                Print.d("user api " + field.getType());
                field.setAccessible(true);
                field.set(o, api);
                field.setAccessible(false);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Print.e(e.getMessage());
        }
    }


    /**
     * 为字段赋mvp值
     *
     * @param field
     * @param o
     * @param v
     * @throws IllegalAccessException
     */
    private static void setValueToField(Field field, Object o, Object v) throws Exception {
        if (v == null) return;
        if (field.isAnnotationPresent(GainApi.class)) {
            setApi(o, field);
        } else {
            Type genericType = field.getGenericType();
            Type[] interfaces = v.getClass().getGenericInterfaces();
            if (interfaces != null && interfaces.length > 0) {
                for (int i1 = 0; i1 < interfaces.length; i1++) {
                    Type mpt = interfaces[i1];
                    if (mpt == genericType) {
                        field.setAccessible(true);
                        field.set(o, v);
                        field.setAccessible(false);
                    }
                }
            }
        }
    }


    /**
     * 解绑
     *
     * @param v
     * @param <V>
     */
    public static <V> void unBind(Class<V> v) {

        LifecycleObservable.get().unAttachLifecycle(v);

        removeTakeOverLifeKey(v);

        if (container.containsKey(v) || container.get(v) != null) {
            PO po = container.get(v);
            po.object = null;
            container.remove(v);
        }

        Iterator<Map.Entry<Class, PO>> iterator = container.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Class, PO> next = iterator.next();
            PO value = next.getValue();
            if (value.key == v) {
                value.object = null;
                iterator.remove();
            }
        }

        removeLife(v);
    }


    public static void unBindLife(Class lk) {
        removeLife(lk);
        unAttachLife(lk);
    }


    public static void unAttachLife(Class key) {
        LifecycleObservable.get().unAttachLifecycle(key);
        boolean b = attach.containsKey(key);
        if (b) attach.remove(key);
    }


    private static class LFP {
        private Class main;
        private Class[] with;
        private LifecycleProvider life;

        public LFP(Class main, Class[] with, LifecycleProvider life) {
            this.main = main;
            this.with = with;
            this.life = life;
        }
    }

    private static class PO {
        private Class key;
        private Object object;

        public PO(Class key, Object object) {
            this.key = key;
            this.object = object;
        }
    }
}
