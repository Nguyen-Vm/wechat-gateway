package com.nguyen.wechat.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author RWM
 * @date 2018/2/8
 */
@Slf4j
public class SpringBeanHelper implements ApplicationContextAware {
    protected static ApplicationContext ctx;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        SpringBeanHelper.ctx = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz){
        try {
            return ctx.getBean(clazz);
        }catch (NoSuchBeanDefinitionException e){
            log.warn("no qualifying bean of type: {}", clazz);
            return null;
        }
    }
}
