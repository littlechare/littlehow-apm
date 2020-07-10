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
 * 服务基础信息表
 * </p>
 *
 * @author littlehow
 * @since 2020-07-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ServerBase {

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
     * 服务中文名
     */
    private String serverNameCn;

    /**
     * 应用path
     */
    private String contextPath;

    /**
     * 服务总机器数
     */
    private Integer serverCount;

    /**
     * 机器下线数
     */
    private Integer serverDown;

    /**
     * 机器上线数
     */
    private Integer serverUp;

    /**
     * 展示名
     */
    private String displayName;

    /**
     * 状态 1:使用中 2:过时
     */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
