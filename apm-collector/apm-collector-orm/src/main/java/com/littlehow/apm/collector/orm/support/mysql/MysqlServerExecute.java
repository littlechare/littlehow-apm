package com.littlehow.apm.collector.orm.support.mysql;

import com.alibaba.fastjson.JSONObject;
import com.littlehow.apm.collector.orm.IServerExecute;
import com.littlehow.apm.collector.orm.model.ServerBaseBo;
import com.littlehow.apm.collector.orm.model.ServerInfoBo;
import com.littlehow.apm.collector.orm.model.ServerInterfaceBo;
import com.littlehow.apm.collector.orm.support.mysql.dao.po.ServerBase;
import com.littlehow.apm.collector.orm.support.mysql.dao.po.ServerChangeLog;
import com.littlehow.apm.collector.orm.support.mysql.dao.po.ServerInfo;
import com.littlehow.apm.collector.orm.support.mysql.dao.po.ServerInterface;
import com.littlehow.apm.collector.orm.support.mysql.service.IServerBaseService;
import com.littlehow.apm.collector.orm.support.mysql.service.IServerChangeLogService;
import com.littlehow.apm.collector.orm.support.mysql.service.IServerInfoService;
import com.littlehow.apm.collector.orm.support.mysql.service.IServerInterfaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class MysqlServerExecute extends BaseExecute implements IServerExecute {

    @Autowired
    protected IServerInfoService serverInfoService;

    @Autowired
    protected IServerBaseService serverBaseService;

    @Autowired
    protected IServerInterfaceService serverInterfaceService;

    @Autowired
    protected IServerChangeLogService serverChangeLogService;


    @Override
    public void updateServer(ServerInfoBo serverInfo, ServerBaseBo serverBase) {
        if (log.isDebugEnabled()) {
            log.debug("MysqlServerExecute.updateServer({},{})", JSONObject.toJSONString(serverInfo),
                    serverBase == null ? "" : JSONObject.toJSONString(serverBase));
        }

        transactionSupport.execute(() -> {
            if (serverBase != null) {
                saveOrUpdateBase(serverBase);
            }
            if (serverInfo != null) {
                saveOrUpdateServerInfo(serverInfo);
            }
        });
    }

    @Override
    public void updateServerInfo(ServerInfoBo serverInfo) {
        if (log.isDebugEnabled()) {
            log.debug("MysqlServerExecute.updateServerInfo({})", JSONObject.toJSONString(serverInfo));
        }
        transactionSupport.execute(() -> saveOrUpdateServerInfo(serverInfo));
    }

    @Override
    public void addOrUpdateInterface(List<ServerInterfaceBo> interfaces) {
        log.info("MysqlServerExecute.addOrUpdateInterface({})", JSONObject.toJSONString(interfaces));
        transactionSupport.execute(() -> {
            List<ServerInterface> serverInterfaces = new ArrayList<>();
            for (ServerInterfaceBo si : interfaces) {
                if (!si.getChanged()) {
                    serverInterfaces.add(new ServerInterface()
                        .setName(si.getServiceExplain())
                        .setClassName(si.getClassName())
                        .setMethodName(si.getMethodName())
                        .setServerName(si.getServerName())
                        .setRpcFlag(si.getRpcFlag().v)
                        .setUri(si.getUri())
                        .setStatus(si.getStatus().v)
                    );
                } else {
                    serverInterfaceService.lambdaUpdate()
                            .set(ServerInterface::getName, si.getServiceExplain())
                            .set(ServerInterface::getRpcFlag, si.getRpcFlag().v)
                            .set(ServerInterface::getClassName, si.getClassName())
                            .set(ServerInterface::getMethodName, si.getMethodName())
                            .set(ServerInterface::getStatus, si.getStatus().v)
                            .eq(ServerInterface::getServerName, si.getServerName())
                            .eq(ServerInterface::getUri, si.getUri())
                            .update();
                }
            }
            if (serverInterfaces.size() > 0) {
                serverInterfaceService.saveBatch(serverInterfaces);
            }
        });
    }

    @Override
    public void removeServerInfo(String serverName, String ipPort, ServerBaseBo baseBo) {
        log.info("MysqlServerExecute.removeServerInfo({},{})", serverName, ipPort);

        transactionSupport.execute(() -> {
            serverInfoService.lambdaUpdate()
                    .eq(ServerInfo::getServerName, serverName)
                    .eq(ServerInfo::getIpPort, ipPort)
                    .remove();
            saveOrUpdateBase(baseBo);
        });

    }

    @Override
    public void removeServer(String serverName) {
        log.info("MysqlServerExecute.removeServer({})", serverName);
        transactionSupport.execute(() -> {
            serverInfoService.lambdaUpdate().eq(ServerInfo::getServerName, serverName).remove();
            serverBaseService.lambdaUpdate().eq(ServerBase::getServerName, serverName).remove();
            serverInterfaceService.lambdaUpdate().eq(ServerInterface::getServerName, serverName).remove();
        });
    }

    /**
     * 修改或插入基础信息
     * @param serverBase -
     */
    private void saveOrUpdateBase(ServerBaseBo serverBase) {
        ServerBase base = serverBaseService.lambdaQuery()
                .eq(ServerBase::getServerName, serverBase.getServerName())
                .one();
        if (base == null) {
            /* 插入基础信息 */
            serverBaseService.save(new ServerBase()
                    .setContextPath(serverBase.getContextPath())
                    .setDisplayName(serverBase.getDisplayName())
                    .setServerCount(serverBase.getServerCount())
                    .setServerDown(serverBase.getServerDown())
                    .setServerUp(serverBase.getServerUp())
                    .setStatus(serverBase.getStatus().v)
                    .setServerName(serverBase.getServerName())
                    .setServerNameCn(serverBase.getServerNameCn())
            );
        } else {
            base.setUpdateTime(null)
                    .setServerDown(serverBase.getServerDown())
                    .setServerUp(serverBase.getServerUp())
                    .setServerCount(serverBase.getServerCount())
                    .setServerNameCn(serverBase.getServerNameCn())
                    .setContextPath(serverBase.getContextPath())
                    .setDisplayName(serverBase.getDisplayName())
                    .setStatus(serverBase.getStatus().v);
            serverBaseService.updateById(base);
        }
    }

    /**
     * 更新或插入服务信息
     * @param serverInfo -
     */
    private void saveOrUpdateServerInfo(ServerInfoBo serverInfo) {
        ServerInfo si = serverInfoService.lambdaQuery()
                .eq(ServerInfo::getServerName, serverInfo.getServerName())
                .eq(ServerInfo::getIpPort, serverInfo.getIpPort())
                .one();
        if (si == null) {
            /* 插入服务信息 */
            serverInfoService.save(new ServerInfo()
                    .setCurrentStatus(serverInfo.getStatus().name())
                    .setHeartbeatDistance(serverInfo.getHeartbeatDistance())
                    .setIpPort(serverInfo.getIpPort())
                    .setServerName(serverInfo.getServerName())
                    .setLastHeartbeatTime(serverInfo.getLastHeartbeat())
                    .setLastUpTime(serverInfo.getLastUpTime())
            );
        } else {
            if (!serverInfo.getStatus().name().equals(si.getCurrentStatus())) {
                //设置状态变更log
                serverChangeLogService.save(new ServerChangeLog()
                        .setBeforeStatus(si.getCurrentStatus())
                        .setAfterStatus(serverInfo.getStatus().name())
                        .setIpPort(serverInfo.getIpPort())
                        .setServerName(serverInfo.getServerName())
                );
            }
            si.setCurrentStatus(serverInfo.getStatus().name())
                    .setHeartbeatDistance(serverInfo.getHeartbeatDistance())
                    .setLastHeartbeatTime(serverInfo.getLastHeartbeat())
                    .setLastUpTime(serverInfo.getLastUpTime())
                    .setUpdateTime(null);

            serverInfoService.updateById(si);
        }

    }

}
