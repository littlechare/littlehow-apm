package com.littlehow.apm.test.order.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ApiModel(description = "订单编号")
@Getter
@Setter
public class OrderNoVO {
    @NotNull(message = "订单编号不可为空")
    @Pattern(regexp = "DD\\d+", message = "订单编号格式有误")
    @ApiModelProperty("订单编号")
    private String orderNo;
}
