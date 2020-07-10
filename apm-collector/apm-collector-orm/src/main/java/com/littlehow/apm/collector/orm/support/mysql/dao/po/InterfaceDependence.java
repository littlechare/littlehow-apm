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
 * 接口依赖关系
 * </p>
 *
 * @author littlehow
 * @since 2020-07-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class InterfaceDependence {

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
     * 接口uri
     */
    private String uri;

    /**
     * 依赖服务名
     */
    private String dependenceServerName;

    /**
     * 依赖服务uri
     */
    private String dependenceServerUri;

    /**
     * 状态 1使用中 2过时
     */
    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
