package com.nguyen.wechat.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author RWM
 * @date 2018/1/24
 */
public final class DateUtils {
    public static final long DAT_TIME = 24 * 60 * 60 * 1000L;

    private DateUtils() {
    }

    /**
     * 当前系统时间毫秒
     * **/
    public static Long time(){
        return System.currentTimeMillis();
    }

    /**
     * 当前系统时间 Date 类型
     * **/
    public static Date now(){
        return Calendar.getInstance().getTime();
    }

    /**
     * 给定 format 将当前系统时间转换成 string
     * **/
    public static String now(DateFormat format){
        return format(now(), format);
    }

    /**
     * 给定 format 将 Date 转换成 string
     * **/
    public static String format(Date date, DateFormat format){
        return null == date ? format.name() : new SimpleDateFormat(format.pattern).format(date);
    }

    /**
     * 给定 format 将 string 转换成 Date
     * **/
    public static Date ofDate(String sDate, DateFormat format){
        try {
            return new SimpleDateFormat(format.pattern).parse(sDate);
        }catch (Exception e){
            throw new RuntimeException("ofDate error, sDate: " + sDate + "format: " + format.pattern, e);
        }
    }

    /**
     * 当前时间距离下次某一时刻的时间差，单位:毫秒
     * **/
    public static long timeSlot(final Date time, final Long period){
        final long now = time();
        return (now < time.getTime()) ? (time.getTime() - now) : (time.getTime() + period - now);
    }

    /**
     * 数字时间转换成字符类型，位数不足补零
     * **/
    public static String leftPadTime(Integer time){
        return StringUtils.leftPad(String.valueOf(time), 2, "0");
    }

}
