package com.tofu.mvp.util;

import java.util.HashMap;
import java.util.Map;

public class Params {

    public static class builder {

        private Map<String, Object> params = new HashMap<>();

        public static builder create(){
            return new builder();
        }

        private builder() {
        }

        public builder put(String key, Object value) {
            params.put(key, value);
            return this;
        }

        public builder put(String key, Object value,boolean isAdd) {
            if(isAdd) {
                params.put(key, value);
            }
            return this;
        }

        public builder put(Map<String, Object> params) {
            params.putAll(params);
            return this;
        }

        public Map<String, Object> build() {
            return params;
        }
    }
}
