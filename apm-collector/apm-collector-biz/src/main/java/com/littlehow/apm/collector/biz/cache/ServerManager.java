package com.littlehow.apm.collector.biz.cache;

import com.littlehow.apm.base.enums.InterfaceStatus;
import com.littlehow.apm.base.enums.ServerStatus;
import com.littlehow.apm.collector.orm.IServerExecute;
import com.littlehow.apm.collector.orm.model.ServerBaseBo;
import com.littlehow.apm.collector.orm.model.ServerInfoBo;
import com.littlehow.apm.collector.orm.model.ServerInterfaceBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Component
@SuppressWarnings("all")
public class ServerManager {

    @Autowired
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    @Autowired
    @Qualifier("mysqlServerExecute")
    private IServerExecute serverExecute;

    /**
     * 当心跳超过配置次数还未接收，则判定为心跳丢失
     */
    @Value("${apm.collector.lose.heartbeat.time:5}")
    private long heartBeatLoseTime;

    /**
     * 当心跳超过配置次数还未接收，则判定服务丢失
     */
    @Value("${apm.collector.lose.server.time:120}")
    private long serverLoseTime;

    /**
     * 服务信息缓存
     */
    private Map<String, Map<String, ServerInfoBo>> serverCache = new ConcurrentHashMap<>();

    /**
     * 服务基础信息缓存
     */
    private Map<String, ServerBaseBo> serverBaseCache = new ConcurrentHashMap<>();

    /**
     * 服务接口缓存
     */
    private Map<String, Map<String, ServerInterfaceBo>> serverInterfaceCache = new ConcurrentHashMap<>();

    /**
     * 获取base信息
     * @param serverName -
     * @return
     */
    public ServerBaseBo getServerBase(String serverName) {
        return serverBaseCache.get(serverName);
    }

    /**
     * 获取所有base信息
     * @return
     */
    public List<ServerBaseBo> getAllServerBase() {
        return new ArrayList<>(serverBaseCache.values());
    }

    /**
     * 移除服务时获取
     * @param serverName - 服务名
     * @param status - 移除服务的原始状态
     * @return
     */
    public ServerBaseBo getServerBaseToRemove(String serverName, ServerStatus status) {
        ServerBaseBo baseBo = serverBaseCache.get(serverName);
        if (baseBo == null) {
            baseBo = rebuildBase(serverName);
            putServerBase(baseBo);
        }
        baseBo.addUp(status != ServerStatus.DOWN ? -1 : 0);
        baseBo.addDown(status == ServerStatus.DOWN ? -1 : 0);
        baseBo.addCount(-1);
        return baseBo;
    }

    public ServerBaseBo rebuildBase(String serverName) {
        ServerBaseBo serverBaseBo = new ServerBaseBo()
                .setServerCount(0)
                .setServerDown(0)
                .setServerUp(0)
                .setContextPath("")
                .setServerNameCn("")
                .setDisplayName("application")
                .setStatus(InterfaceStatus.USING)
                .setServerName(serverName);
        List<ServerInfoBo> serverInfos = getServerInfos(serverName);
        serverInfos.forEach(serverInfo -> {
            serverBaseBo.addUp(serverInfo.getStatus() != ServerStatus.DOWN ? 1 : 0);
            serverBaseBo.addDown(serverInfo.getStatus() == ServerStatus.DOWN ? 1 : 0);
            serverBaseBo.addCount(1);
        });
        return serverBaseBo;
    }

    /**
     * 设置base信息
     * @param serverBase -
     */
    public void putServerBase(ServerBaseBo serverBase) {
        serverBaseCache.put(serverBase.getServerName(), serverBase);
    }

    /**
     * 获取服务信息
     * @param serverName -
     * @param ipPort -
     * @return -
     */
    public ServerInfoBo getServerInfo(String serverName, String ipPort) {
        Map<String, ServerInfoBo> serverInfoBoMap = serverCache.get(serverName);
        if (serverInfoBoMap != null) {
            return serverInfoBoMap.get(ipPort);
        }
        return null;
    }

    /**
     * 获取服务详细信息
     * @param serverName -
     * @return
     */
    public List<ServerInfoBo> getServerInfos(String serverName) {
        Map<String, ServerInfoBo> serverInfoBoMap = serverCache.get(serverName);
        if (serverInfoBoMap != null) {
            return new ArrayList<>(serverInfoBoMap.values());
        }
        return new ArrayList<>();
    }

    /**
     * 移除服务信息
     * @param serverName
     * @param ipPort
     * @return
     */
    public ServerInfoBo removeServerInfo(String serverName, String ipPort) {
        Map<String, ServerInfoBo> serverInfoBoMap = serverCache.get(serverName);
        if (serverInfoBoMap != null) {
            return serverInfoBoMap.remove(ipPort);
        }
        return null;
    }

    /**
     * 移除所有服务信息
     * @param serverName
     */
    public void removeServer(String serverName) {
        serverCache.remove(serverName);
        serverBaseCache.remove(serverName);
        serverInterfaceCache.remove(serverName);
    }

    /**
     * 获取服务的所有接口信息
     * @param serverName -
     * @return -
     */
    public Map<String, ServerInterfaceBo> getInterface(String serverName) {
        return serverInterfaceCache.get(serverName);
    }

    /**
     * 设置服务接口信息
     * @param serverName -
     * @param interfaces -
     */
    public void addInterfaces(String serverName, List<ServerInterfaceBo> interfaces) {
        serverInterfaceCache.put(serverName, interfaces.stream()
                .collect(Collectors.toMap(ServerInterfaceBo::getUri, o -> o,
                        (o1, o2) -> o2, ConcurrentHashMap::new)));
    }


    /**
     * 存储serverInfo信息
     * @param serverInfo -
     */
    public void putServerInfo(ServerInfoBo serverInfo) {
        Map<String, ServerInfoBo> serverInfoBoMap = serverCache.computeIfAbsent(serverInfo.getServerName(),
                k -> new ConcurrentHashMap<>());
        serverInfoBoMap.put(serverInfo.getIpPort(), serverInfo);
    }

    public void initServerBase(List<ServerBaseBo> serverBases) {
        serverBases.forEach(o -> serverBaseCache.put(o.getServerName().intern(), o));
    }

    public void initServerInfo(List<ServerInfoBo> serverInfos) {
        serverInfos.forEach(o -> {
            Map<String, ServerInfoBo> map = serverCache.computeIfAbsent(o.getServerName().intern(),
                    k -> new ConcurrentHashMap<>());
            map.put(o.getIpPort(), o);
        });
    }

    public void initServerInterface(List<ServerInterfaceBo> serverInterfaces) {
        serverInterfaces.forEach(o -> {
            Map<String, ServerInterfaceBo> map = serverInterfaceCache.computeIfAbsent(o.getServerName().intern(),
                    k -> new ConcurrentHashMap<>());
            map.put(o.getUri(), o);
        });
    }

    @PostConstruct
    private void runTask() {
        scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
            long now = System.currentTimeMillis();
            serverCache.forEach((serverName, infos) -> {
                synchronized (serverName) {
                    try {
                        infos.forEach((ipPort, info) -> {
                            Integer distance = info.getHeartbeatDistance();
                            if (info.getStatus() == ServerStatus.UP) {
                                //5次心跳丢失就设置丢失标志,次数可以选择配置项来设置,这里暂时写死
                                if ((now - info.getLastHeartbeat()) > heartBeatLoseTime * distance) {
                                    log.info("服务器心跳丢失,serverInfo={}", info);
                                    info.setStatus(ServerStatus.HEARTBEAT_LOSE);
                                    serverExecute.updateServerInfo(info);
                                }
                            } else {
                                //当down或者lose时，超过配置心跳次数没有则进行移除
                                //这里使用的ConcurrentHashMap,所以可以不用关心读时移除
                                if ((now - info.getLastHeartbeat()) > serverLoseTime * distance) {
                                    log.info("服务器已经失联，将进行移除, serverInfo={}", info);
                                    ServerBaseBo baseBo = getServerBaseToRemove(info.getServerName(), info.getStatus());
                                    serverExecute.removeServerInfo(info.getServerName(), info.getIpPort(), baseBo);
                                    removeServerInfo(info.getServerName(), info.getIpPort());
                                }
                            }
                        });
                    } catch (Exception e) {
                        log.error("处理服务器信息异常", e);
                    }

                }
            });
            //此处延迟60秒执行任务是为了防止重启时间间隔过长导致的心跳误判
        }, 60, 3, TimeUnit.SECONDS);
    }
}
