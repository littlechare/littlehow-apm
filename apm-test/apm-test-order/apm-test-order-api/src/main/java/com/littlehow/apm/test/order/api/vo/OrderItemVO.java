package com.littlehow.apm.test.order.api.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Setter
@Getter
public class OrderItemVO {

    @NotBlank(message = "商品编号不可为空")
    @ApiModelProperty("商品编号")
    private String itemNo;

    @NotBlank(message = "商家编号不可为空")
    @ApiModelProperty("商家编号")
    private String shopNo;

    @NotNull(message = "数量不可为空")
    @Min(value = 1, message = "最下数量为1")
    @ApiModelProperty("数量")
    private Integer count;
}
