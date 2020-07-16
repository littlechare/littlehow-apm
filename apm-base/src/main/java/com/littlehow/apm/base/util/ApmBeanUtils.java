package com.littlehow.apm.base.util;


import org.springframework.beans.BeanUtils;

/**
 * 使用spring的beanUtil
 * @author littlehow
 */
public class ApmBeanUtils {
    public static <T> T copyNewOnNull(Object source, Class<T> target) {
        try {
            T t = target.newInstance();
            if (source != null) {
                BeanUtils.copyProperties(source, t);
            }
            return t;
        } catch (Exception e) {
            throw new RuntimeException("Please provide a none parameter constructor", e);
        }
    }

    public static <T> T copy(Object source, Class<T> target) {
        try {
            if (source == null) {
                return null;
            }
            T t = target.newInstance();
            BeanUtils.copyProperties(source, t);
            return t;
        } catch (Exception e) {
            throw new RuntimeException("Please provide a none parameter constructor", e);
        }
    }
}
