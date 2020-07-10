package com.littlehow.apm.collector.orm.support.mysql;

import com.littlehow.apm.base.enums.InterfaceStatus;
import com.littlehow.apm.base.enums.ServerStatus;
import com.littlehow.apm.base.enums.YesOrNo;
import com.littlehow.apm.collector.orm.model.ServerBaseBo;
import com.littlehow.apm.collector.orm.model.ServerInfoBo;
import com.littlehow.apm.collector.orm.model.ServerInterfaceBo;
import com.littlehow.apm.collector.orm.support.mysql.dao.po.ServerBase;
import com.littlehow.apm.collector.orm.support.mysql.dao.po.ServerInfo;
import com.littlehow.apm.collector.orm.support.mysql.dao.po.ServerInterface;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component("mysqlServerExecute")
public class MysqlServerReadExecute extends MysqlServerExecute {
    @Override
    public List<ServerInfoBo> loadServerInfo() {
        //查询所有服务器信息
        List<ServerInfo> sis = serverInfoService.list();
        //组装成serverInfoBo
        if (CollectionUtils.isEmpty(sis)) {
            return new ArrayList<>();
        } else {
            return sis.stream().map(o -> new ServerInfoBo()
                    .setIpPort(o.getIpPort())
                    .setStatus(ServerStatus.valueOf(o.getCurrentStatus()))
                    .setHeartbeatDistance(o.getHeartbeatDistance())
                    .setLastHeartbeat(o.getLastHeartbeatTime())
                    .setLastUpTime(o.getLastUpTime())
                    .setServerName(o.getServerName())
            ).collect(Collectors.toList());
        }
    }

    @Override
    public List<ServerBaseBo> loadServerBase() {
        //查询所有基础信息
        List<ServerBase> sbs = serverBaseService.list();
        if (CollectionUtils.isEmpty(sbs)) {
            return new ArrayList<>();
        } else {
            return sbs.stream().map(o -> new ServerBaseBo()
                    .setContextPath(o.getContextPath())
                    .setDisplayName(o.getDisplayName())
                    .setServerCount(o.getServerCount())
                    .setServerDown(o.getServerDown())
                    .setServerName(o.getServerName())
                    .setServerNameCn(o.getServerNameCn())
                    .setServerUp(o.getServerUp())
                    .setStatus(InterfaceStatus.value(o.getStatus()))
            ).collect(Collectors.toList());
        }
    }

    @Override
    public List<ServerInterfaceBo> loadServerInterface() {
        //查询所有接口信息
        List<ServerInterface> sis = serverInterfaceService.list();
        if (CollectionUtils.isEmpty(sis)) {
            return new ArrayList<>();
        } else {
            return sis.stream().map(o -> new ServerInterfaceBo()
                    .setClassName(o.getClassName())
                    .setMethodName(o.getMethodName())
                    .setRpcFlag(YesOrNo.value(o.getRpcFlag()))
                    .setStatus(InterfaceStatus.value(o.getStatus()))
                    .setServiceExplain(o.getName())
                    .setUri(o.getUri())
                    .setServerName(o.getServerName())
            ).collect(Collectors.toList());
        }
    }
}
