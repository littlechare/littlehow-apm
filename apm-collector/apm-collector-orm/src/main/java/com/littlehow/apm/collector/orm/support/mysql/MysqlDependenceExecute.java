package com.littlehow.apm.collector.orm.support.mysql;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.littlehow.apm.base.util.ApmBeanUtils;
import com.littlehow.apm.collector.orm.IDependenceExecute;
import com.littlehow.apm.collector.orm.model.InterfaceDependenceBo;
import com.littlehow.apm.collector.orm.support.mysql.dao.po.InterfaceDependence;
import com.littlehow.apm.collector.orm.support.mysql.service.IInterfaceDependenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component("mysqlDependenceExecute")
public class MysqlDependenceExecute implements IDependenceExecute {

    private final List<InterfaceDependenceBo> EMPTY =  new ArrayList<>();

    @Autowired
    private IInterfaceDependenceService interfaceDependenceService;

    @Override
    public List<InterfaceDependenceBo> queryServerDependence(String serverName) {
        return queryDependence(InterfaceDependence::getServerName, serverName, null, null);
    }

    @Override
    public List<InterfaceDependenceBo> queryServerBeDependence(String serverName) {
        return queryDependence(InterfaceDependence :: getDependenceServerName, serverName, null, null);
    }

    @Override
    public List<InterfaceDependenceBo> queryInterfaceDependence(String interfaceName, String serverName) {
        return queryDependence(InterfaceDependence :: getUri, interfaceName,
                InterfaceDependence::getServerName, serverName);
    }

    @Override
    public List<InterfaceDependenceBo> queryInterfaceBeDependence(String interfaceName, String serverName) {
        return queryDependence(InterfaceDependence::getDependenceServerUri, interfaceName,
                InterfaceDependence::getDependenceServerName, serverName);
    }

    @Override
    public List<InterfaceDependenceBo> serverDependenceDetail(String serverName, String dependenceServerName) {
        return queryDependence(InterfaceDependence::getServerName, serverName,
                InterfaceDependence::getDependenceServerName, dependenceServerName);
    }

    private List<InterfaceDependenceBo> queryDependence(SFunction<InterfaceDependence, String> consumer1, String value1,
                                                        SFunction<InterfaceDependence, String> consumer2, String value2) {
        List<InterfaceDependence> interfaceDependence = interfaceDependenceService.lambdaQuery()
                .eq(consumer1, value1)
                .eq(consumer2 != null, consumer2, value2)
                .list();
        if (CollectionUtils.isEmpty(interfaceDependence)) {
            //防止在外部被添加数据
            EMPTY.clear();
            return EMPTY;
        }
        return interfaceDependence.stream()
                .map(o -> ApmBeanUtils.copyNewOnNull(o, InterfaceDependenceBo.class))
                .collect(Collectors.toList());
    }


}
