package com.littlehow.apm.test.user.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author littlehow
 */
@Data
@ApiModel(description = "登录请求信息")
public class LoginRequestVO {

    @NotBlank(message = "手机号码不能为空")
    @ApiModelProperty("手机号")
    private String mobilePhone;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("短信验证码")
    @NotBlank(message = "验证码不可为空")
    private String code;
}
