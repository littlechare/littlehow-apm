package com.littlehow.apm.test.user.api.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * -- 用户信息
 * @author littlehow
 */
@Data
@ApiModel(description = "用户信息")
public class UserInfoVO {

    @ApiModelProperty("用户编号")
    private String userNo;

    @ApiModelProperty("身份证号")
    private String certificateNo;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("头像")
    private String headUrl;

    @ApiModelProperty("手机号")
    private String mobilePhone;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("生日")
    private LocalDate birthDay;

    @ApiModelProperty("最后登录时间")
    private LocalDateTime lastLoginTime;
}
