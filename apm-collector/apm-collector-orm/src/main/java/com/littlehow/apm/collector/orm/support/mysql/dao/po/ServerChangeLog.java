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
 * 服务状态变化日志表
 * </p>
 *
 * @author littlehow
 * @since 2020-07-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ServerChangeLog {

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
     * 变更前状态 DOWN:下线 UP:上线 HEARTBEAT_LOSE:心跳丢失
     */
    private String beforeStatus;

    /**
     * 变更后状态 DOWN:下线 UP:上线 HEARTBEAT_LOSE:心跳丢失
     */
    private String afterStatus;

    private LocalDateTime createTime;


}
