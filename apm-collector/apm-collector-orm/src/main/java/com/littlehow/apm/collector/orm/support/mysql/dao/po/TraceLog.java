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
 * 调用日志表
 * </p>
 *
 * @author littlehow
 * @since 2020-07-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TraceLog {

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 追中id
     */
    private String traceId;

    /**
     * 子id
     */
    private String spanId;

    /**
     * 请求url
     */
    private String requestUrl;

    /**
     * 请求头
     */
    private String requestHeaders;

    /**
     * 请求体
     */
    private String requestBody;

    /**
     * 响应体
     */
    private String responseBody;

    /**
     * 自身服务信息
     */
    private String self;

    /**
     * 远程服务信息
     */
    private String remote;

    /**
     * 异常信息
     */
    private String exception;

    /**
     * 成功标志1成功 0失败
     */
    private Integer success;

    /**
     * 请求开始时间
     */
    private Long startTime;

    /**
     * 请求持续时间
     */
    private Long during;

    private LocalDateTime createTime;


}
