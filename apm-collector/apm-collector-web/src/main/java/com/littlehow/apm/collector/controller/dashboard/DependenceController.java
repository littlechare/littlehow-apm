package com.littlehow.apm.collector.controller.dashboard;


import com.littlehow.apm.base.util.ApmBeanUtils;
import com.littlehow.apm.collector.api.req.DependenceServerNameReq;
import com.littlehow.apm.collector.api.req.InterfaceNameReq;
import com.littlehow.apm.collector.api.req.ServerNameReq;
import com.littlehow.apm.collector.api.resp.InterfaceDependenceResp;
import com.littlehow.apm.collector.api.resp.ServerDependenceResp;
import com.littlehow.apm.collector.biz.IDependenceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "服务依赖相关接口")
@RestController
@RequestMapping("/apm/collection/dashboard/dependence")
@Slf4j
public class DependenceController {

    @Autowired
    private IDependenceService dependenceService;

    /**
     * 查询服务依赖关系
     * @param req   -
     * @return -
     */
    @ApiOperation("查询服务依赖关系")
    @RequestMapping(value = "server", method = RequestMethod.POST)
    public List<ServerDependenceResp> server(@RequestBody @Valid ServerNameReq req) {
        return dependenceService.queryServerDependence(req.getServerName())
                .stream().map(o -> ApmBeanUtils.copyNewOnNull(o, ServerDependenceResp.class))
                .collect(Collectors.toList());
    }

    /**
     * 查询依赖该服务的服务信息
     * @param req   -
     * @return -
     */
    @ApiOperation("查询服务被依赖关系")
    @RequestMapping(value = "serverBe", method = RequestMethod.POST)
    public List<ServerDependenceResp> serverBe(@RequestBody @Valid ServerNameReq req) {
        return dependenceService.queryServerBeDependence(req.getServerName())
                .stream().map(o -> ApmBeanUtils.copyNewOnNull(o, ServerDependenceResp.class))
                .collect(Collectors.toList());
    }

    /**
     * 查询接口依赖关系
     * @param req -
     * @return
     */
    @ApiOperation("查询接口依赖关系")
    @RequestMapping(value = "interface", method = RequestMethod.POST)
    public List<InterfaceDependenceResp> inter(@RequestBody @Valid InterfaceNameReq req) {
        return dependenceService.queryInterfaceDependence(req.getUri(), req.getServerName())
                .stream().map(o -> ApmBeanUtils.copyNewOnNull(o, InterfaceDependenceResp.class))
                .collect(Collectors.toList());
    }

    /**
     * 查询接口被依赖关系
     * @param req -
     * @return
     */
    @ApiOperation("查询接口被依赖关系")
    @RequestMapping(value = "interfaceBe", method = RequestMethod.POST)
    public List<InterfaceDependenceResp> interfaceBe(@RequestBody @Valid InterfaceNameReq req) {
        return dependenceService.queryInterfaceBeDependence(req.getUri(), req.getServerName())
                .stream().map(o -> ApmBeanUtils.copyNewOnNull(o, InterfaceDependenceResp.class))
                .collect(Collectors.toList());
    }

    @ApiOperation("查询服务详细依赖信息")
    @RequestMapping(value = "detail", method = RequestMethod.POST)
    public List<InterfaceDependenceResp> detail(@RequestBody @Valid DependenceServerNameReq req) {
        return dependenceService.serverDependenceDetail(req.getServerName(), req.getDependenceServerName())
                .stream().map(o -> ApmBeanUtils.copyNewOnNull(o, InterfaceDependenceResp.class))
                .collect(Collectors.toList());
    }
}
