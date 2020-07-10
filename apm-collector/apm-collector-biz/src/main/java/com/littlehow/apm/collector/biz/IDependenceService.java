package com.littlehow.apm.collector.biz;

import com.littlehow.apm.collector.orm.model.InterfaceDependenceBo;

import java.util.List;

public interface IDependenceService {
    /**
     * 查询服务依赖关系
     * @param serverName -
     * @return
     */
    List<InterfaceDependenceBo> queryServerDependence(String serverName);

    /**
     * 查询服务被依赖关系
     * @param serverName -
     * @return -
     */
    List<InterfaceDependenceBo> queryServerBeDependence(String serverName);

    /**
     * 查询接口依赖关系
     * @param interfaceName - 接口名称
     * @param serverName    - 服务名
     * @return
     */
    List<InterfaceDependenceBo> queryInterfaceDependence(String interfaceName, String serverName);

    /**
     * 查询接口被依赖关系
     * @param interfaceName - 接口名称
     * @param serverName    - 服务名
     * @return
     */
    List<InterfaceDependenceBo> queryInterfaceBeDependence(String interfaceName, String serverName);

    /**
     * 查询接口被依赖关系
     * @param dependenceServerName -
     * @param serverName    - 服务名
     * @return
     */
    List<InterfaceDependenceBo> serverDependenceDetail(String serverName, String dependenceServerName);
}
