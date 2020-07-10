package com.littlehow.apm.collector.orm.model;

import com.littlehow.apm.base.enums.ServerStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class ServerInfoBo {

    /**
     * 服务名称
     */
    private String serverName;

    /**
     * ip地址
     */
    private String ip;

    /**
     * 端口号
     */
    private String serverPort;

    /**
     * 状态
     */
    private ServerStatus status;

    /**
     * 心跳间隔时间
     */
    private Integer heartbeatDistance;

    /**
     * 最近心跳时间
     */
    private Long lastHeartbeat;

    /**
     * 最新上线时间
     */
    private LocalDateTime lastUpTime;

    public void setLastHeartBeatIfNull(Long lastHeartbeat) {
        if (this.lastHeartbeat == null) {
            this.lastHeartbeat = lastHeartbeat;
        }
    }

    public void setLastUpTimeIfNull(LocalDateTime lastUpTime) {
        if (this.lastUpTime == null) {
            this.lastUpTime = lastUpTime;
        }
    }

    public String getIpPort() {
        return this.ip + ":" + this.serverPort;
    }

    public ServerInfoBo setIpPort(String ipPort) {
        String[] info = ipPort.split(":");
        this.ip = info[0];
        this.serverPort = info[1];
        return this;
    }
}
