package com.littlehow.apm.base.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 分页请求标准
 * @author littlehow
 */
@Getter
@Setter
public class PageReq {

    @ApiModelProperty("页码 从1开始计数")
    @NotNull(message = "页码不可为空")
    @Min(value = 1, message = "页码最小从1开始")
    private Integer pageIndex;

    @ApiModelProperty("页容 区间[1, 1000]")
    @NotNull(message = "页容不可为空")
    @Range(min = 1, max = 1000, message = "页容的取值区间为[1, 1000]")
    private Integer pageSize;
}
