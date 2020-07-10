package com.littlehow.apm.feign;

import com.littlehow.apm.base.web.RemoteServerContext;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.loadbalancer.BaseLoadBalancer;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.RoundRobinRule;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;


/**
 * 确定负载均衡器选择的是哪一个ip
 * 并管理对应的负载均衡器
 * @author littlehow
 */
@Slf4j
public class FeignRule extends RoundRobinRule {

    @Override
    public Server choose(Object key) {
        Server server = super.choose(key);
        if (Objects.isNull(server)) {
            log.info("server is null");
            return null;
        }
        log.info("feign rule ---> final server ip:{}", server.getHostPort());
        RemoteServerContext.setHostPort(server.getHostPort());
        return server;
    }

    @Override
    public Server choose(ILoadBalancer lb, Object key) {
        Server chooseServer = super.choose(lb, key);
        if (log.isDebugEnabled()) {
            List<Server> reachableServers = lb.getReachableServers();
            List<Server> allServers = lb.getAllServers();
            int upCount = reachableServers.size();
            int serverCount = allServers.size();
            log.debug("upCount:{}, serverCount:{}", upCount, serverCount);
            for (Server server : allServers) {
                if (server instanceof DiscoveryEnabledServer){
                    DiscoveryEnabledServer dServer = (DiscoveryEnabledServer)server;
                    InstanceInfo instanceInfo = dServer.getInstanceInfo();
                    if (instanceInfo!=null){
                        InstanceInfo.InstanceStatus status = instanceInfo.getStatus();
                        if (status!=null){
                            log.debug("server:{}, status:{}", server.getHostPort(), ((DiscoveryEnabledServer) server).getInstanceInfo().getStatus());
                        }
                    }
                }
            }
        }


        return chooseServer;
    }

    @Override
    public void setLoadBalancer(ILoadBalancer lb){
        if (lb instanceof BaseLoadBalancer) {
            BaseLoadBalancerContext.addBalancer((BaseLoadBalancer) lb);
        }
        super.setLoadBalancer(lb);
    }
}
