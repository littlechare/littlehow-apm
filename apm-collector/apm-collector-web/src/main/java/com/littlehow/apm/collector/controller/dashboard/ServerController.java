package com.littlehow.apm.collector.controller.dashboard;

import com.littlehow.apm.base.util.ApmBeanUtils;
import com.littlehow.apm.collector.api.req.ServerNameReq;
import com.littlehow.apm.collector.api.resp.ServerBaseResp;
import com.littlehow.apm.collector.api.resp.ServerInfoResp;
import com.littlehow.apm.collector.api.resp.ServerInterfaceResp;
import com.littlehow.apm.collector.biz.cache.ServerManager;
import com.littlehow.apm.collector.orm.model.ServerBaseBo;
import com.littlehow.apm.collector.orm.model.ServerInfoBo;
import com.littlehow.apm.collector.orm.model.ServerInterfaceBo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Api(tags = "服务资源读取接口")
@RestController
@RequestMapping("/apm/collection/dashboard/server")
@Slf4j
public class ServerController {

    @Autowired
    private ServerManager serverManager;

    /**
     * 服务信息过多可考虑分页
     * @return -
     */
    @ApiOperation("获取基础服务列表")
    @RequestMapping(value = "baseList", method = RequestMethod.POST)
    public List<ServerBaseResp> baseList() {
        List<ServerBaseBo> serverBaseBos = serverManager.getAllServerBase();
        return serverBaseBos.stream().map(o -> {
            ServerBaseResp resp = ApmBeanUtils.copyNewOnNull(o, ServerBaseResp.class);
            resp.setStatus(o.getStatus().v);
            return resp;
        }).collect(Collectors.toList());
    }

    @ApiOperation("获取服务列表")
    @RequestMapping(value = "infoList", method = RequestMethod.POST)
    public List<ServerInfoResp> infoList(@RequestBody @Valid ServerNameReq serverNameReq) {
        log.info("ServerController.infoList({})", serverNameReq.getServerName());
        List<ServerInfoBo> serverInfoBos = serverManager.getServerInfos(serverNameReq.getServerName());
        return serverInfoBos.stream().map(o -> ApmBeanUtils.copyNewOnNull(o, ServerInfoResp.class))
                .collect(Collectors.toList());
    }

    @ApiOperation("获取服务接口列表")
    @RequestMapping(value = "interfaceList", method = RequestMethod.POST)
    public List<ServerInterfaceResp> interfaceList(@RequestBody @Valid ServerNameReq serverNameReq) {
        log.info("ServerController.interfaceList({})", serverNameReq.getServerName());
        Map<String, ServerInterfaceBo> serverInterfaceMap = serverManager.getInterface(serverNameReq.getServerName());
        List<ServerInterfaceResp> respList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(serverInterfaceMap)) {
            serverInterfaceMap.forEach((k, v) -> {
                ServerInterfaceResp resp = ApmBeanUtils.copyNewOnNull(v, ServerInterfaceResp.class);
                resp.setStatus(v.getStatus().v);
                resp.setRpcFlag(v.getRpcFlag().v);
                respList.add(resp);
            });
            //将同名class接口排在一起
            respList.sort(Comparator.comparing(ServerInterfaceResp::getClassName));
        }
        return respList;
    }
}
