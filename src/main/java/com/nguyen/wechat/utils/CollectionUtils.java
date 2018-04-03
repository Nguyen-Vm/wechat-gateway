package com.nguyen.wechat.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author RWM
 * @date 2018/3/28
 * @description:
 */
public final class CollectionUtils {
    private CollectionUtils(){}

    public static <T> boolean isNullOrEmpty(final Collection<T> collection){
        return collection == null || collection.isEmpty();
    }

    public static <T> boolean isNullOrEmpty(final T[] array){
        return array == null || array.length == 0;
    }

    public static <K, V> boolean isNullOrEmpty(final Map<K, V> map){
        return map == null || map.isEmpty();
    }

    public static <T> T head(final List<T> list){
        return list.get(0);
    }

    public static <T> T end(final List<T> list){
        return list.get(list.size() - 1);
    }

    public static <T> boolean contains(final T[] array, final T val){
        for (T t : array){
            if (val.equals(t)){
                return true;
            }
        }
        return false;
    }

    public static <T, R> List<R> map(final List<T> list, Function<? super T, R> function){
        return list.stream().map(function).collect(Collectors.toList());
    }

    public static <K, V> ListMultimap<K, V> groupBy(final List<V> list, final Function<V, K> keyFunction){
        ListMultimap<K, V> result = ArrayListMultimap.create();
        for (V value : list){
            result.put(keyFunction.apply(value), value);
        }
        return result;
    }

    public static <K, V> LinkedHashMap<K, V> ofMap(final List<V> list, final Function<V, K> keyFunction){
        LinkedHashMap<K, V> result = Maps.newLinkedHashMap();
        for (V value : list){
            result.put(keyFunction.apply(value), value);
        }
        return result;
    }

}
