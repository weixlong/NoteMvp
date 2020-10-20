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
import java.util.HashMap;
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
        if(app != null) {
            prefs = app.getSharedPreferences(name, MODE_PRIVATE);
        }
        return BuilderConstructor.builder;
    }


    public static XmlBuilder get() {
        if(app != null) {
            prefs = app.getSharedPreferences(app.getPackageName(), MODE_PRIVATE);
        }
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

            if (prefs != null && obj instanceof Serializable) {// obj必须实现Serializable接口，否则会出问题
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
                if(prefs != null) {
                    String base64 = prefs.getString(clazz.getName(), "");
                    if (base64.equals("")) {
                        return null;
                    }
                    byte[] base64Bytes = Base64.decode(base64.getBytes(), 1);
                    ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    obj = (T) ois.readObject();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return obj;
        }


        public <T> T getObject(String key) {
            T obj = null;
            try {
                if(prefs != null) {
                    String base64 = prefs.getString(key, "");
                    if (base64.equals("")) {
                        return null;
                    }
                    byte[] base64Bytes = Base64.decode(base64.getBytes(), 1);
                    ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    obj = (T) ois.readObject();
                }
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
            if (prefs != null && obj instanceof Serializable) {// obj必须实现Serializable接口，否则会出问题
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
            if (prefs != null) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(key, value).apply();
                editor.commit();
            }
            return this;
        }

        public XmlBuilder put(String key, boolean value) {
            if (prefs != null) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean(key, value).apply();
                editor.commit();
            }
            return this;
        }

        public XmlBuilder put(String key, int value) {
            if (prefs != null) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt(key, value).apply();
                editor.commit();
            }
            return this;
        }


        public XmlBuilder put(String key, long value) {
            if (prefs != null) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putLong(key, value).apply();
                editor.commit();
            }
            return this;
        }


        public XmlBuilder put(String key, double value) {
            if (prefs != null) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(key, Double.toString(value)).apply();
                editor.commit();
            }
            return this;
        }


        public XmlBuilder put(String key, float value) {
            if (prefs != null) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putFloat(key, value).apply();
                editor.commit();
            }
            return this;
        }

        public XmlBuilder put(String key, Set<String> defValues) {
            if (prefs != null) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putStringSet(key, defValues).apply();
                editor.commit();
            }
            return this;
        }


        public String getString(String key, String defValue) {
            if (prefs != null) {
                return prefs.getString(key, defValue);
            }
            return "";
        }


        public double getDouble(String key, String defValue) {
            double value = 0.00;
            try {
                if (prefs != null) {
                    value = Double.parseDouble(prefs.getString(key, defValue));
                }
            } catch (Exception e){

            }
            return value;
        }

        public String getString(String key) {
            if (prefs != null) {
                return prefs.getString(key, "");
            }
            return "";
        }

        public int getInt(String key, int defValue) {
            if (prefs != null) {
                return prefs.getInt(key, defValue);
            }
            return defValue;
        }

        public int getInt(String key) {
            if (prefs != null) {
                return prefs.getInt(key, -1);
            }
            return -1;
        }

        public long getLong(String key) {
            if (prefs != null) {
                return prefs.getLong(key, -1);
            }
            return -1;
        }

        public long getLong(String key, long defValue) {
            if (prefs != null) {
                return prefs.getLong(key, defValue);
            }
            return defValue;
        }

        public float getFloat(String key) {
            if (prefs != null) {
                return prefs.getFloat(key, -1.0f);
            }
            return -1.0f;
        }

        public float getFloat(String key, float defValue) {
            if (prefs != null) {
                return prefs.getFloat(key, defValue);
            }
            return defValue;
        }

        public boolean getBoolean(String key) {
            if (prefs != null) {
                return prefs.getBoolean(key, false);
            }
            return false;
        }

        public boolean getBoolean(String key, boolean defValue) {
            if (prefs != null) {
                return prefs.getBoolean(key, defValue);
            }
            return defValue;
        }

        @TargetApi(Build.VERSION_CODES.M)
        public Set<String> getSet(String key) {
            if (prefs != null) {
                return prefs.getStringSet(key, new ArraySet());
            }
            return new ArraySet<>();
        }

        public boolean contains(String key) {
            if (prefs != null) {
                return prefs.contains(key);
            }
            return false;
        }

        public Map<String, ?> getAll() {
            if (prefs != null) {
                return prefs.getAll();
            }
            return new HashMap<>();
        }

        public void clear() {
            if (prefs != null) {
                SharedPreferences.Editor edit = prefs.edit();
                edit.clear().apply();
                edit.commit();
            }
        }


        public void remove(@NonNull String key) {
            if (prefs != null) {
                SharedPreferences.Editor edit = prefs.edit();
                edit.remove(key).apply();
                edit.commit();
            }
        }

    }

}
