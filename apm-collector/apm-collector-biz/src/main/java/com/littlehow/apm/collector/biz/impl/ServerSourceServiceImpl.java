package com.littlehow.apm.collector.biz.impl;

import com.littlehow.apm.base.enums.InterfaceStatus;
import com.littlehow.apm.base.enums.ServerStatus;
import com.littlehow.apm.base.util.ApmAssert;
import com.littlehow.apm.collector.biz.IServerSourceService;
import com.littlehow.apm.collector.biz.cache.ServerManager;
import com.littlehow.apm.collector.orm.IServerExecute;
import com.littlehow.apm.collector.orm.model.ServerBaseBo;
import com.littlehow.apm.collector.orm.model.ServerInfoBo;
import com.littlehow.apm.collector.orm.model.ServerInterfaceBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * 这里主要是服务信息入口
 * 如果性能受到影响，可以考虑重写持久化方案，考虑延时或定时持久化操作
 * @author littlehow
 */
@Service
@Slf4j
@SuppressWarnings("all")
public class ServerSourceServiceImpl implements IServerSourceService, InitializingBean {
    @Autowired
    private ServerManager serverManager;

    @Autowired
    @Qualifier("mysqlServerExecute")
    private IServerExecute serverExecute;

    /**
     * 只需要启动时加载一次即可
     */
    private AtomicBoolean loaded = new AtomicBoolean(false);

    @Override
    public void register(ServerBaseBo serverBase, ServerInfoBo serverInfo, List<ServerInterfaceBo> serverInterfaces) {
        synchronized (serverBase.getServerName()) {
            saveOrUpdateServerAndBase(serverInfo, serverBase);
            saveOrUpdateInterface(serverBase.getServerName(), serverInterfaces);
        }
    }

    @Override
    public void heartbeat(ServerInfoBo serverInfo) {
        synchronized (serverInfo.getServerName()) {
            //查询serverInfo
            ServerInfoBo old = serverManager.getServerInfo(serverInfo.getServerName(), serverInfo.getIpPort());
            if (old == null) {
                //心跳比注册先到，忽略本次心跳
                return;
            }
            ServerBaseBo base = checkServerBaseBo(serverInfo, old);
            old.setLastHeartbeat(System.currentTimeMillis());
            old.setStatus(serverInfo.getStatus());
            //持久化
            serverExecute.updateServer(old, base);
        }
    }

    @Override
    public void down(ServerInfoBo serverInfo) {
        String serverName = serverInfo.getServerName();
        String ipPort = serverInfo.getIpPort();
        synchronized (serverName) {
            //查询serverInfo
            ServerInfoBo old = serverManager.getServerInfo(serverName, ipPort);
            ApmAssert.notNull(old, "服务[{0}],标识[{1}]不存在", serverName, ipPort);
            if (old.getStatus() != ServerStatus.DOWN) {
                old.setStatus(ServerStatus.DOWN);
                ServerBaseBo serverBaseBo = serverManager.getServerBase(serverName);
                serverBaseBo.addDown(1);
                serverBaseBo.addUp(- 1);
                serverExecute.updateServer(old, serverBaseBo);
            }
        }
    }

    @Override
    public void removeServerInfo(ServerInfoBo serverInfo) {
        String serverName = serverInfo.getServerName();
        String ipPort = serverInfo.getIpPort();
        synchronized (serverName) {
            ServerInfoBo old = serverManager.getServerInfo(serverName, ipPort);
            ApmAssert.notNull(old, "服务[{0}], 标识[{1}]不存在", serverName, ipPort);
            ServerBaseBo baseBo = serverManager.getServerBaseToRemove(serverName, old.getStatus());
            serverExecute.removeServerInfo(serverName, ipPort, baseBo);
            serverManager.removeServerInfo(serverName, ipPort);
        }
    }

    @Override
    public void removeServer(String serverName) {
        synchronized (serverName) {
            ServerBaseBo old = serverManager.getServerBase(serverName);
            ApmAssert.notNull(old, "服务[{0}]信息不存在", serverName);
            serverExecute.removeServer(serverName);
            serverManager.removeServer(serverName);
        }
    }

    @Override
    public void updateServerBase(ServerBaseBo serverBase) {
        synchronized (serverBase.getServerName()) {
            saveOrUpdateBase(serverBase);
        }
    }


    @Override
    public void afterPropertiesSet() {
        loadServerInfo();
    }

    /**
     * 保存或更新服务基础信息
     * @param serverInfo -
     * @param serverBase -
     */
    private void saveOrUpdateServerAndBase(ServerInfoBo serverInfo, ServerBaseBo serverBase) {
        ServerBaseBo oldBase = serverManager.getServerBase(serverBase.getServerName());
        ServerInfoBo oldInfo = serverManager.getServerInfo(serverInfo.getServerName(), serverInfo.getIpPort());
        if (oldBase != null) {
            //判断oldInfo信息
            if (oldInfo == null) {
                oldBase.addUp(serverBase.getServerUp());
                oldBase.addDown(serverBase.getServerDown());
                oldBase.addCount(1);
            } else {
                oldBase.addUp(getUp(oldInfo.getStatus(), serverInfo.getStatus()));
                oldBase.addDown(getDown(oldInfo.getStatus(), serverInfo.getStatus()));
                serverInfo.setLastUpTimeIfNull(oldInfo.getLastUpTime());
            }
            oldBase.setServerNameCn(serverBase.getServerNameCn());
            oldBase.setDisplayName(serverBase.getDisplayName());
            oldBase.setContextPath(serverBase.getContextPath());
        } else {
            oldBase = serverBase;
            serverManager.putServerBase(oldBase);
        }
        serverManager.putServerInfo(serverInfo);
        serverExecute.updateServer(serverInfo, serverBase);
    }

    private void saveOrUpdateBase(ServerBaseBo serverBase) {
        serverExecute.updateServer(null, serverBase);
        serverManager.putServerBase(serverBase);
    }

    private void saveOrUpdateInterface(String serverName, List<ServerInterfaceBo> serverInterfaces) {
        Map<String, ServerInterfaceBo> old = serverManager.getInterface(serverName);
        List<ServerInterfaceBo> changeOrSave = new ArrayList<>();
        List<ServerInterfaceBo> all = new ArrayList<>(serverInterfaces);
        if (old == null) {
            changeOrSave.addAll(serverInterfaces);
        } else {
            serverInterfaces.forEach(o -> {
                ServerInterfaceBo oo = old.remove(o.getUri());
                if (oo == null) {
                    changeOrSave.add(o);
                } else {
                    o.setChangeIfChange(oo);
                    if (o.getChanged()) {
                        changeOrSave.add(o);
                    }
                }
            });
            //判断原来接口是否还有
            if (old.size() > 0) {
                old.forEach((uri, bo) -> {
                    bo.setChanged(true);
                    bo.setStatus(InterfaceStatus.DEPRECATED);
                    changeOrSave.add(bo);
                    all.add(bo);
                });
            }
            serverManager.addInterfaces(serverName, all);

        }
        serverManager.addInterfaces(serverName, all);
        serverExecute.addOrUpdateInterface(changeOrSave);
    }

    /**
     * 初始加载服务信息
     */
    private void loadServerInfo() {
        try {
            if (loaded.compareAndSet(false, true)) {
                //获取基础信息
                serverManager.initServerBase(serverExecute.loadServerBase());
                //获取服务信息
                serverManager.initServerInfo(serverExecute.loadServerInfo());
                //获取接口信息
                serverManager.initServerInterface(serverExecute.loadServerInterface());
            }
        } catch (Exception e) {
            log.error("加载服务信息失败", e);
            throw e;
        }
    }

    private ServerBaseBo checkServerBaseBo(ServerInfoBo newBo, ServerInfoBo oldBo) {
        if (newBo.getStatus() != oldBo.getStatus()) {
            ServerBaseBo base = serverManager.getServerBase(newBo.getServerName());
            int up = newBo.getStatus() == ServerStatus.UP ? 1 : 0;
            up = oldBo.getStatus() == ServerStatus.DOWN ? up : up - 1;
            base.addUp(getUp(oldBo.getStatus(), newBo.getStatus()));
            base.addDown(getDown(oldBo.getStatus(), newBo.getStatus()));
            return base;
        }
        return null;
    }

    private int getUp(ServerStatus o, ServerStatus n) {
        return o == ServerStatus.DOWN && n == ServerStatus.UP ? 1 : 0;
    }

    private int getDown(ServerStatus o, ServerStatus n) {
        return o != ServerStatus.DOWN && n == ServerStatus.DOWN ? 1 : 0;
    }
}
