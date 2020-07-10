package com.littlehow.apm.collector.orm.model;

import com.littlehow.apm.base.enums.InterfaceStatus;
import com.littlehow.apm.base.enums.YesOrNo;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Objects;

@Data
@Accessors(chain = true)
public class ServerInterfaceBo {
    /**
     * 服务名称
     */
    private String serverName;

    /**
     * 接口uri
     */
    private String uri;

    /**
     * 所在类的类名
     */
    private String className;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 服务说明
     */
    private String serviceExplain;

    /**
     * rpc标志
     */
    private YesOrNo rpcFlag;

    /**
     * 是否改变
     */
    private Boolean changed;

    /**
     * 服务接口状态
     */
    private InterfaceStatus status;

    public void setChangeIfChange(ServerInterfaceBo other) {
        changed = !Objects.equals(other.methodName, this.methodName)
                || !Objects.equals(other.className, this.className)
                || !Objects.equals(other.serviceExplain, this.serviceExplain)
                || other.status != this.status
                || other.rpcFlag != this.rpcFlag;
    }

}
