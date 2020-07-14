package com.littlehow.apm.register.eureka;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.InstanceInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 服务管理接口（基于spring cloud eureka的client）
 *
 * @author littlehow
 */
@RequestMapping("/eureka/manage")
@RestController
public class EurekaManagementController {

    @Autowired
    private ApplicationInfoManager applicationInfoManager;

    /**
     * 服务上线
     *
     * @return
     */
    @RequestMapping("/online")
    @ApiOperation("eureka服务上线")
    public boolean online() {
        return changeInstanceStatus(InstanceInfo.InstanceStatus.UP);
    }

    /**
     * 服务下线
     *
     * @return
     */
    @RequestMapping("/offline")
    @ApiOperation("eureka服务下线")
    public boolean offline() {
        return changeInstanceStatus(InstanceInfo.InstanceStatus.DOWN);
    }

    private boolean changeInstanceStatus(InstanceInfo.InstanceStatus status) {
        if (null != applicationInfoManager && null != status) {
            applicationInfoManager.setInstanceStatus(status);
            return true;
        }
        return false;
    }
}
