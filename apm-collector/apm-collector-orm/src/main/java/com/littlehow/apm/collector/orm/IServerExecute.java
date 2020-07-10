package com.littlehow.apm.collector.orm;


import com.littlehow.apm.collector.orm.model.ServerBaseBo;
import com.littlehow.apm.collector.orm.model.ServerInfoBo;
import com.littlehow.apm.collector.orm.model.ServerInterfaceBo;

import java.util.List;

/**
 * 持久化服务接口
 * @author littlehow
 */
public interface IServerExecute {

    /**
     * 上线服务信息
     * @param serverInfo -
     * @param serverBase -
     */
    void updateServer(ServerInfoBo serverInfo, ServerBaseBo serverBase);

    /**
     * 切换服务状态
     * @param serverInfo -
     */
    void updateServerInfo(ServerInfoBo serverInfo);

    /**
     * 保存或更新接口信息
     * @param interfaces -
     */
    void addOrUpdateInterface(List<ServerInterfaceBo> interfaces);

    /**
     * 移除server信息
     * @param serverName -
     * @param ipPort -
     * @param baseBo -
     */
    void removeServerInfo(String serverName, String ipPort, ServerBaseBo baseBo);

    /**
     * 移除所有server信息
     * @param serverName -
     */
    void removeServer(String serverName);

    /**
     * 加载所有server信息
     * @return -
     */
    List<ServerInfoBo> loadServerInfo();

    /**
     * 加载所有基础信息
     * @return -
     */
    List<ServerBaseBo> loadServerBase();

    /**
     * 加载所有server interface信息
     * @return -
     */
    List<ServerInterfaceBo> loadServerInterface();
}
