package com.nguyen.wechat.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author RWM
 * @date 2018/4/2
 * @description:
 */
public class BeanUtils {
    private static ModelMapper modelMapper = new ModelMapper();

    static {
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setFieldMatchingEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setPropertyCondition(ctx -> ctx.getSource() != null
                        && StringUtils.isNotBlank(ctx.getSource().toString())
                        && ctx.getSourceType().equals(ctx.getDestinationType()));
    }

    /** 对象克隆 **/
    public static <Source, Target> Target map(final Source source, final Class<Target> destType) {
        if (source == null || destType == null) {
            return null;
        }
        try {
            Target target = destType.newInstance();
            map(source, target);
            return target;
        } catch (Exception e) {
            throw new RuntimeException("map error", e);
        }
    }

    /** 集合对象克隆 **/
    public static <Source, Target> List<Target> map(final Collection<Source> sources, final Class<Target> targetType) {
        List<Target> targets = Lists.newArrayListWithExpectedSize(sources.size());
        for (Source source : sources) {
            targets.add(map(source, targetType));
        }
        return targets;
    }

    private static <Source, Target> void map(final Object source, final Object target) {
        if (source != null && target != null) {
            modelMapper.map(source, target);
        }
    }

    /** Bean转Map **/
    public static <T> T map2Bean(final Map<String, Object> source, final Class<T> clazz) {
        return JsonUtils.parseObject(new JSONObject(source).toString(), clazz);
    }

    /** Map转Bean **/
    public static Map<String, Object> bean2Map(Object source) {
        return (JSONObject) JSON.toJSON(source);
    }


}
