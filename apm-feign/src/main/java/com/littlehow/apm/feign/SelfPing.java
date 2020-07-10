package com.littlehow.apm.feign;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import com.netflix.niws.loadbalancer.NIWSDiscoveryPing;
import lombok.extern.slf4j.Slf4j;

/**
 * 实时获取服务的在线状态，forcePing和定时任务ping都会调用
 * @author littlehow
 */
@Slf4j
public class SelfPing extends NIWSDiscoveryPing {

    /**
     * 主要为了根据ping的结果以及ping时instance的状态
     * @param server  -- 需要的服务信息
     * @return  -- 返回true将进入upServer列表
     */
    public boolean isAlive(Server server) {
        if (server instanceof DiscoveryEnabledServer) {
            return isAlive((DiscoveryEnabledServer) server);
        } else {
            log.warn("invalid server {}", server);
            return false;
        }
    }

    private boolean isAlive(DiscoveryEnabledServer server) {
        boolean isAlive = super.isAlive(server);
        if (log.isDebugEnabled()) {
            log.debug("{} execute ping result is {}, server origin status is {}", server, isAlive, getStatus(server.getInstanceInfo()));
        }
        return isAlive;
    }

    /**
     * 获取服务实例的状态
     * @param instanceInfo  -- 服务实例信息
     * @return  --
     */
    private String getStatus(InstanceInfo instanceInfo) {
        if (instanceInfo == null || instanceInfo.getStatus() == null) {
            return null;
        }
        return instanceInfo.getStatus().name();
    }
}
