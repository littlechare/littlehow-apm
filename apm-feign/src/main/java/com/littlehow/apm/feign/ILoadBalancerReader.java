package com.littlehow.apm.feign;

import com.netflix.loadbalancer.BaseLoadBalancer;

/**
 * 服务均衡访问接口
 */
public interface ILoadBalancerReader {
    /**
     * 读取负载均衡器
     * @param balancer -
     */
    void read(BaseLoadBalancer balancer);
}
