package com.nguyen.wechat.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

/**
 * @author RWM
 * @date 2018/3/28
 * @description:
 */
public class JsonUtils {

    private JsonUtils(){

    }

    public static String toJSONString(final Object o, SerializerFeature... features){
        SerializerFeature[] sfArray = !CollectionUtils.isNullOrEmpty(features) ? features : new SerializerFeature[]{
                SerializerFeature.QuoteFieldNames,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteDateUseDateFormat
        };
        return JSON.toJSONString(o, new SerializeFilter[0], sfArray);
    }

    public static String toJSONString(final Object o, SerializeFilter filter, SerializerFeature... features){
        return JSON.toJSONString(o, filter, features);
    }

    public static <T> T parseObject(final byte[] body, final Class<T> clazz){
        return null == body ? null : JSON.parseObject(body, clazz);
    }

    public static <T> byte[] toJSONBytes(final T data){
        return JSON.toJSONBytes(data);
    }

    public static <T> T parseObject(String body, final Class<T> clazz){
        return JSON.parseObject(body, clazz);
    }

    public static <T> List<T> parseArray(String text, final Class<T> clazz){
        return JSON.parseArray(text, clazz);
    }

    public static JSONObject toJSon(final Object javaObject){
        return (JSONObject) JSON.toJSON(javaObject);
    }

    public static JSONObject parseObject(String text){
        return JSON.parseObject(text);
    }

}
