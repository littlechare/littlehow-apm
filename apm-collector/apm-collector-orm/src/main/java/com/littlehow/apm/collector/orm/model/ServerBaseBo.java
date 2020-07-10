package com.littlehow.apm.collector.orm.model;

import com.littlehow.apm.base.enums.InterfaceStatus;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ServerBaseBo {
    /**
     * 应用名
     */
    private String serverName;

    /**
     * 应用中文名
     */
    private String serverNameCn;

    /**
     * 应用path
     */
    private String contextPath;

    /**
     * 展示名
     */
    private String displayName;

    /**
     * 服务器数量
     */
    private Integer serverCount;

    /**
     * 上线数量
     */
    private Integer serverUp;

    /**
     * 下线数量
     */
    private Integer serverDown;

    /**
     * 服务接口状态
     */
    private InterfaceStatus status;

    public void addUp(int up) {
        serverUp = serverUp + up;
        if (serverUp < 0) {
            serverUp = 0;
        }
    }

    public void addDown(int down) {
        serverDown = serverDown + down;
        if (serverDown < 0) {
            serverDown = 0;
        }
    }

    public void addCount(int count) {
        serverCount = serverCount + count;
        if (serverCount < 0) {
            serverCount = 0;
            serverUp = 0;
            serverDown = 0;
        }
    }
}
