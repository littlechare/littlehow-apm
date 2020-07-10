package com.littlehow.apm.base.req;

import com.littlehow.apm.base.enums.YesOrNo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;


/**
 * 接口信息请求体
 * @author littlehow
 */
@Data
public class InterfaceReq {
    @ApiModelProperty("接口请求子域名")
    @NotNull(message = "uri不可为空")
    private String uri;

    @ApiModelProperty("所在类的类名")
    @NotBlank(message = "类名不可为空")
    private String className;

    @ApiModelProperty("方法名")
    @NotBlank(message = "方法名不可为空")
    private String methodName;

    @ApiModelProperty("服务说明(使用了swagger注解)")
    private String serviceExplain;

    @ApiModelProperty("是否为rpc接口")
    private YesOrNo rpcFlag;
}
