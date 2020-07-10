package com.littlehow.apm.feign;

import com.netflix.loadbalancer.BaseLoadBalancer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 负载均衡管理上下文
 *
 * @author littlehow
 */
public class BaseLoadBalancerContext {
    private static final Map<String, BaseLoadBalancer> context = new ConcurrentHashMap<>();

    public static BaseLoadBalancer addBalancer(BaseLoadBalancer baseLoadBalancer) {
        return context.put(baseLoadBalancer.getName().toUpperCase(), baseLoadBalancer);
    }

    public static BaseLoadBalancer getBalancer(String name) {
        return context.get(name.toUpperCase());
    }

    public static void readAll(ILoadBalancerReader reader) {
        context.forEach((key, value) -> reader.read(value));
    }

}
