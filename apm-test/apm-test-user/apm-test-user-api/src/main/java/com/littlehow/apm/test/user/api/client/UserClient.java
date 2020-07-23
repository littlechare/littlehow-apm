package com.littlehow.apm.test.user.api.client;

import com.littlehow.apm.test.user.api.vo.LoginRequestVO;
import com.littlehow.apm.test.user.api.vo.UserAccountVO;
import com.littlehow.apm.test.user.api.vo.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * 用户接口
 *
 * @author littlehow
 */
@Api(tags = "用户接口")
@FeignClient("apm-user")
@RequestMapping("/apm/test/user")
public interface UserClient {

    @ApiOperation("登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    UserInfoVO login(LoginRequestVO request);

    @ApiOperation("查询用户信息")
    @RequestMapping(value = "/info/{userNo}", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    UserInfoVO getInfo(@PathVariable("userNo") String userNo);

    @ApiOperation("查询账户信息")
    @RequestMapping(value = "/account/{userNo}", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    UserAccountVO getAccount(@PathVariable("userNo") String userNo);
}
