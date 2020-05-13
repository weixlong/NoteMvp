package com.tofu.mvp.gain.convert;

import com.alibaba.fastjson.JSON;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * Created by jason on 2017/12/19.
 */

public class FastJsonRequestBodyConverter<T> implements Converter<T, RequestBody> {

    private static final MediaType MEDIA_TYPE_TEXT = MediaType.parse("text/plain; charset=UTF-8");
    private static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=UTF-8");

    /**
     * @param value T
     * @return json
     * @throws IOException
     */
    @Override
    public RequestBody convert(T value) throws IOException {
        if (value instanceof String) {
            return RequestBody.create(MEDIA_TYPE_TEXT, (String) value);
        }
        return RequestBody.create(MEDIA_TYPE_JSON, JSON.toJSONBytes(value));
    }
}