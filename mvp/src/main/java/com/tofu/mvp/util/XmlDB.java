package com.tofu.mvp.util;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.ArraySet;
import android.util.Base64;

import androidx.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by wxl on 2019/5/14.
 */

public class XmlDB {

    private static volatile SharedPreferences prefs;

    private static Application app;

    protected static class BuilderConstructor {
        private static XmlBuilder builder = new XmlBuilder();
        protected static XmlDB xmlDB = new XmlDB();
    }

    private XmlDB() {
    }

    public static void initialize(Application app) {
        XmlDB.app = app;
        prefs = app.getSharedPreferences(app.getPackageName(), MODE_PRIVATE);
    }


    public static XmlBuilder get(String name) {
        prefs = app.getSharedPreferences(name, MODE_PRIVATE);
        return BuilderConstructor.builder;
    }


    public static XmlBuilder get() {
        prefs = app.getSharedPreferences(app.getPackageName(), MODE_PRIVATE);
        return BuilderConstructor.builder;
    }


    public static Application getApp() {
        return app;
    }

    public static class XmlBuilder {

        private XmlBuilder() {
        }

        /**
         * 存放实体类以及任意类型
         *
         * @param
         * @param
         * @param obj
         */
        public <T> XmlBuilder putObject(T obj) {
            if (obj instanceof Serializable) {// obj必须实现Serializable接口，否则会出问题
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(obj);
                    String string64 = new String(Base64.encode(baos.toByteArray(), 0));
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(obj.getClass().getName(), string64).commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new IllegalArgumentException("the obj must implement Serializble");
            }

            return this;
        }

        public <T> T getObject(Class<T> clazz) {
            T obj = null;
            try {
                String base64 = prefs.getString(clazz.getName(), "");
                if (base64.equals("")) {
                    return null;
                }
                byte[] base64Bytes = Base64.decode(base64.getBytes(), 1);
                ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                obj = (T) ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return obj;
        }


        public <T> T getObject(String key) {
            T obj = null;
            try {
                String base64 = prefs.getString(key, "");
                if (base64.equals("")) {
                    return null;
                }
                byte[] base64Bytes = Base64.decode(base64.getBytes(), 1);
                ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
                ObjectInputStream ois = new ObjectInputStream(bais);
                obj = (T) ois.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return obj;
        }


        /**
         * 存放实体类以及任意类型
         *
         * @param
         * @param
         * @param obj
         */
        public <T> XmlBuilder putObject(String key,T obj) {
            if (obj instanceof Serializable) {// obj必须实现Serializable接口，否则会出问题
                try {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(obj);
                    String string64 = new String(Base64.encode(baos.toByteArray(), 0));
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString(key, string64).commit();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                throw new IllegalArgumentException("the obj must implement Serializble");
            }

            return this;
        }


        public XmlBuilder put(String key, String value) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(key, value).apply();
            editor.commit();
            return this;
        }

        public XmlBuilder put(String key, boolean value) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(key, value).apply();
            editor.commit();
            return this;
        }

        public XmlBuilder put(String key, int value) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt(key, value).apply();
            editor.commit();
            return this;
        }


        public XmlBuilder put(String key, long value) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(key, value).apply();
            editor.commit();
            return this;
        }


        public XmlBuilder put(String key, double value) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(key, Double.toString(value)).apply();
            editor.commit();
            return this;
        }


        public XmlBuilder put(String key, float value) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putFloat(key, value).apply();
            editor.commit();
            return this;
        }

        public XmlBuilder put(String key, Set<String> defValues) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putStringSet(key, defValues).apply();
            editor.commit();
            return this;
        }


        public String getString(String key, String defValue) {
            return prefs.getString(key, defValue);
        }


        public double getDouble(String key, String defValue) {
            double value = 0.00;
            try {
                value = Double.parseDouble(prefs.getString(key, defValue));
            } catch (Exception e){

            }
            return value;
        }

        public String getString(String key) {
            return prefs.getString(key, "");
        }

        public int getInt(String key, int defValue) {
            return prefs.getInt(key, defValue);
        }

        public int getInt(String key) {
            return prefs.getInt(key, -1);
        }

        public long getLong(String key) {
            return prefs.getLong(key, -1);
        }

        public long getLong(String key, long defValue) {
            return prefs.getLong(key, defValue);
        }

        public float getFloat(String key) {
            return prefs.getFloat(key, -1.0f);
        }

        public float getFloat(String key, float defValue) {
            return prefs.getFloat(key, defValue);
        }

        public boolean getBoolean(String key) {
            return prefs.getBoolean(key, false);
        }

        public boolean getBoolean(String key, boolean defValue) {
            return prefs.getBoolean(key, defValue);
        }

        @TargetApi(Build.VERSION_CODES.M)
        public Set<String> getSet(String key) {
            return prefs.getStringSet(key, new ArraySet());
        }

        public boolean contains(String key) {
            return prefs.contains(key);
        }

        public Map<String, ?> getAll() {
            return prefs.getAll();
        }

        public void clear() {
            SharedPreferences.Editor edit = prefs.edit();
            edit.clear().apply();
            edit.commit();
        }


        public void remove(@NonNull String key) {
            SharedPreferences.Editor edit = prefs.edit();
            edit.remove(key).apply();
            edit.commit();
        }

    }

}
