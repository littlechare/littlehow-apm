package com.littlehow.apm.test.api;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页请求统一响应体
 * @author littlehow
 * @param <T>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "分页结果")
public class PageResult<T> {

    @ApiModelProperty("页容")
    private Integer pageSize;

    @ApiModelProperty("页码")
    private Integer pageIndex;

    @ApiModelProperty("数据总条数")
    private Long total;

    @ApiModelProperty("数据")
    private List<T> data;
}
