package com.littlehow.apm.collector.orm.support.mysql.dao.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 服务信息表
 * </p>
 *
 * @author littlehow
 * @since 2020-07-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ServerInfo {

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 服务名
     */
    private String serverName;

    /**
     * ip端口
     */
    private String ipPort;

    /**
     * 当前状态 DOWN:下线 UP:上线 HEARTBEAT_LOSE:心跳丢失
     */
    private String currentStatus;

    /**
     * 心跳间隔时间(秒)
     */
    private Integer heartbeatDistance;

    /**
     * 上次心跳时间
     */
    private Long lastHeartbeatTime;

    /**
     * 最后上线时间
     */
    private LocalDateTime lastUpTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
