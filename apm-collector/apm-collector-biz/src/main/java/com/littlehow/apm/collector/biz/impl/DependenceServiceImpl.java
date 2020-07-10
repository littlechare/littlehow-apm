package com.littlehow.apm.collector.biz.impl;

import com.littlehow.apm.collector.biz.IDependenceService;
import com.littlehow.apm.collector.orm.IDependenceExecute;
import com.littlehow.apm.collector.orm.model.InterfaceDependenceBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DependenceServiceImpl implements IDependenceService {

    @Autowired
    @Qualifier("mysqlDependenceExecute")
    private IDependenceExecute dependenceExecute;

    @Override
    public List<InterfaceDependenceBo> queryServerDependence(String serverName) {
        return removeDuplicate(dependenceExecute.queryServerDependence(serverName));
    }

    @Override
    public List<InterfaceDependenceBo> queryServerBeDependence(String serverName) {
        return removeDuplicate(dependenceExecute.queryServerBeDependence(serverName));
    }

    @Override
    public List<InterfaceDependenceBo> queryInterfaceDependence(String interfaceName, String serverName) {
        return dependenceExecute.queryInterfaceDependence(interfaceName, serverName);
    }

    @Override
    public List<InterfaceDependenceBo> queryInterfaceBeDependence(String interfaceName, String serverName) {
        return dependenceExecute.queryInterfaceBeDependence(interfaceName, serverName);
    }

    @Override
    public List<InterfaceDependenceBo> serverDependenceDetail(String serverName, String dependenceServerName) {
        return dependenceExecute.serverDependenceDetail(serverName, dependenceServerName);
    }

    private List<InterfaceDependenceBo> removeDuplicate(List<InterfaceDependenceBo> bos) {
        //进行去重
        Set<String> names = new HashSet<>();
        return bos.stream().filter(o -> {
            if (names.contains(o.getDependenceServerName())) {
                return false;
            }
            names.add(o.getDependenceServerName());
            return true;
        }).collect(Collectors.toList());
    }
}
