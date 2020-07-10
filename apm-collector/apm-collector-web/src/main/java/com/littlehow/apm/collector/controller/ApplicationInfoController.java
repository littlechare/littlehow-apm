package com.littlehow.apm.collector.controller;

import com.alibaba.fastjson.JSONObject;
import com.littlehow.apm.base.enums.InterfaceStatus;
import com.littlehow.apm.base.enums.ServerStatus;
import com.littlehow.apm.base.req.ApplicationReq;
import com.littlehow.apm.base.req.HeartbeatReq;
import com.littlehow.apm.base.util.ApmBeanUtils;
import com.littlehow.apm.collector.api.req.ServerBaseUpdateReq;
import com.littlehow.apm.collector.api.req.ServerNameReq;
import com.littlehow.apm.collector.api.req.SingleServerInfoReq;
import com.littlehow.apm.collector.biz.IServerSourceService;
import com.littlehow.apm.collector.orm.model.ServerBaseBo;
import com.littlehow.apm.collector.orm.model.ServerInfoBo;
import com.littlehow.apm.collector.orm.model.ServerInterfaceBo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Api(tags = "服务资源接口")
@RestController
@RequestMapping("/apm/collection/application")
@Slf4j
public class ApplicationInfoController {

    @Value("${apm.log.heartbeat:false}")
    private boolean heartbeatLogFlag;

    private Map<String, String> SERVER_NAME_CACHE = new ConcurrentHashMap<>();

    @Autowired
    private IServerSourceService serverSourceService;

    @ApiOperation("注册服务信息")
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public void registerApplication(@RequestBody @Valid ApplicationReq req) {
        log.info("ApplicationInfoController.registerApplication({})", JSONObject.toJSONString(req));
        ServerStatus serverStatus = req.getStatus();
        String serverName = getServerName(req.getApplicationName());
        int up = serverStatus == ServerStatus.UP ? 1 : 0;
        //基础信息
        ServerBaseBo serverBase = new ServerBaseBo()
                .setServerName(serverName)
                .setServerNameCn(req.getApplicationNameCn())
                .setContextPath(req.getContextPath())
                .setDisplayName(req.getDisplayName())
                .setServerUp(up)
                .setServerCount(1)
                .setServerDown(1 - up)
                .setStatus(InterfaceStatus.USING);

        //服务信息
        ServerInfoBo serverInfo = new ServerInfoBo()
                .setServerName(serverName)
                .setServerPort(req.getServerPort())
                .setIp(req.getIp())
                .setHeartbeatDistance(req.getHeartbeatDistance())
                .setLastHeartbeat(System.currentTimeMillis())
                .setStatus(serverStatus);
        if (serverStatus == ServerStatus.UP) {
            serverInfo.setLastUpTime(LocalDateTime.now());
        }

        //接口信息
        List<ServerInterfaceBo> serverInterfaces = req.getInterfaceReqs().stream()
                .map(o -> ApmBeanUtils.copyNewOnNull(o, ServerInterfaceBo.class)
                        .setServerName(serverName)
                        .setChanged(false)
                        .setStatus(InterfaceStatus.USING)
                ).collect(Collectors.toList());

        //调用业务层
        serverSourceService.register(serverBase, serverInfo, serverInterfaces);
    }

    @ApiOperation("注册服务信息")
    @RequestMapping(value = "heartbeat", method = RequestMethod.POST)
    public void heartbeat(@RequestBody @Valid HeartbeatReq req) {
        if (heartbeatLogFlag) {
            log.info("ApplicationInfoController.heartbeat({})", JSONObject.toJSONString(req));
        }
        serverSourceService.heartbeat(new ServerInfoBo()
                .setServerName(getServerName(req.getApplicationName()))
                .setServerPort(req.getServerPort())
                .setIp(req.getIp())
                .setStatus(req.getStatus())
        );
    }

    @ApiOperation("下线服务信息")
    @RequestMapping(value = "downServer", method = RequestMethod.POST)
    public void downServer(@RequestBody @Valid SingleServerInfoReq req) {
        log.info("ApplicationInfoController.downServer({})", JSONObject.toJSONString(req));
        serverSourceService.down(new ServerInfoBo()
                .setServerName(getServerName(req.getApplicationName()))
                .setServerPort(req.getServerPort())
                .setIp(req.getIp())
        );
    }

    @ApiOperation("移除单个服务信息")
    @RequestMapping(value = "removeServerInfo", method = RequestMethod.POST)
    public void removeServerInfo(@RequestBody @Valid SingleServerInfoReq req) {
        log.info("ApplicationInfoController.removeServerInfo({})", JSONObject.toJSONString(req));
        serverSourceService.removeServerInfo(new ServerInfoBo()
                .setServerName(getServerName(req.getApplicationName()))
                .setIp(req.getIp())
                .setServerPort(req.getServerPort())
        );
    }

    @ApiOperation("移除服务的所有信息")
    @RequestMapping(value = "removeServer", method = RequestMethod.POST)
    public void removeServer(@RequestBody @Valid ServerNameReq req) {
        log.info("ApplicationInfoController.removeServer({})", JSONObject.toJSONString(req));
        serverSourceService.removeServer(getServerName(req.getServerName()));
    }

    @ApiOperation("更新服务基础信息")
    @RequestMapping(value = "updateServerBase", method = RequestMethod.POST)
    public void updateServerBase(@RequestBody @Valid ServerBaseUpdateReq req) {
        log.info("ApplicationInfoController.updateServerBase({})", JSONObject.toJSONString(req));
        ServerBaseBo baseBo = ApmBeanUtils.copyNewOnNull(req, ServerBaseBo.class);
        baseBo.setServerName(getServerName(req.getServerName()));
        baseBo.setStatus(InterfaceStatus.USING);
        serverSourceService.updateServerBase(baseBo);
    }

    private String getServerName(String serverName) {
        return SERVER_NAME_CACHE.computeIfAbsent(serverName, k -> serverName.intern());
    }
}
