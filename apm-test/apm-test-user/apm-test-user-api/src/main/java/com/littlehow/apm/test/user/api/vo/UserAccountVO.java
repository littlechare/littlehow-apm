package com.littlehow.apm.test.user.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @author littlehow
 */
@ApiModel(description = "用户账户信息")
@Data
public class UserAccountVO {

    @ApiModelProperty("用户编号")
    private String userNo;

    @ApiModelProperty("账户")
    private String account;

    @ApiModelProperty("余额")
    private BigDecimal amount;

    @ApiModelProperty("冻结金额")
    private BigDecimal freezeAmount;
}
