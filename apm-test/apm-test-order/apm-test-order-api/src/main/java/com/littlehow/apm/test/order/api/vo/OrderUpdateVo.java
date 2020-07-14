package com.littlehow.apm.test.order.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * 订单修改，仅仅作为测试使用
 *
 * @author littlehow
 */
@Getter
@Setter
@ApiModel(description = "订单修改")
public class OrderUpdateVo extends OrderNoVO {

    @NotNull(message = "订单状态不可为空")
    @Pattern(regexp = "\\d{1,2}", message = "状态必须是1位或2位的数字")
    @ApiModelProperty("订单状态")
    private String status;

    @ApiModelProperty("订单数量(支持纠正订单数量)")
    @NotNull(message = "数量不可为空")
    @Min(value = 1, message = "数量最小为1")
    private Integer count;
}
