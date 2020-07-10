package com.littlehow.apm.collector.biz;


import com.littlehow.apm.collector.orm.model.ServerBaseBo;
import com.littlehow.apm.collector.orm.model.ServerInfoBo;
import com.littlehow.apm.collector.orm.model.ServerInterfaceBo;

import java.util.List;

/**
 * 服务器信息service
 * @author littlehow
 */
public interface IServerSourceService {

    /**
     * 注册服务信息
     * @param serverBase         -- 服务基础信息
     * @param serverInfo         -- 服务信息
     * @param serverInterfaces   -- 接口信息
     */
    void register(ServerBaseBo serverBase, ServerInfoBo serverInfo, List<ServerInterfaceBo> serverInterfaces);

    /**
     * 心跳链接
     * @param serverInfo - 服务信息
     */
    void heartbeat(ServerInfoBo serverInfo);

    /**
     * 下线服务
     * @param serverInfo -
     */
    void down(ServerInfoBo serverInfo);

    /**
     * 移除服务
     * @param serverInfo -
     */
    void removeServerInfo(ServerInfoBo serverInfo);

    /**
     * 移除所有服务信息
     * @param serverName -
     */
    void removeServer(String serverName);

    /**
     * 更新base基础信息
     * @param serverBase
     */
    void updateServerBase(ServerBaseBo serverBase);
}
