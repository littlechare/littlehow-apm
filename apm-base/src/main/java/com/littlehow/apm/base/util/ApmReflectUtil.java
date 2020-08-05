package com.littlehow.apm.base.util;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author littlehow
 */
public class ApmReflectUtil {
    public static List<Method> getCustomerMethods(Class<?> target) {
        return Arrays.stream(ReflectionUtils.getAllDeclaredMethods(target))
                .filter(o -> o.getDeclaringClass() != Object.class)
                .collect(Collectors.toList());
    }
}
