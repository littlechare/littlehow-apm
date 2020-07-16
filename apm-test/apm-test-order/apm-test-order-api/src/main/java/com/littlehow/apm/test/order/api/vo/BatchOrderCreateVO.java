package com.littlehow.apm.test.order.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel(description = "批量下单")
@Data
public class BatchOrderCreateVO {

    @NotNull(message = "用户编号不可为空")
    @ApiModelProperty("用户编号")
    private String userNo;

    @Valid
    @NotEmpty(message = "订单商品信息不可为空")
    @ApiModelProperty("订单商品信息")
    private List<OrderItemVO> orderItems;
}
