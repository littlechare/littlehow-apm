package com.littlehow.apm.test.boss.controller;

import com.littlehow.apm.test.user.api.client.UserClient;
import com.littlehow.apm.test.user.api.vo.LoginRequestVO;
import com.littlehow.apm.test.user.api.vo.UserAccountVO;
import com.littlehow.apm.test.user.api.vo.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 *
 * boss调用用户信息
 * 偷懒使用api提供的VO
 *
 * @author littlehow
 */
@RestController
@RequestMapping("/apm/test/boss/user")
@Api(tags = "boss用户相关接口")
public class BossUserController {

    @Autowired
    private UserClient userClient;

    @ApiOperation("登录接口")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public UserInfoVO login(@RequestBody @Valid LoginRequestVO request) {
        return userClient.login(request);
    }

    @ApiOperation("获取用户信息")
    @RequestMapping(value = "/{userNo}/info", method = RequestMethod.GET)
    public UserInfoVO getInfo(@PathVariable String userNo) {
        return userClient.getInfo(userNo);
    }

    @ApiOperation("获取用户账户信息")
    @RequestMapping(value = "/{userNo}/account", method = RequestMethod.GET)
    public UserAccountVO getAccount(@PathVariable String userNo) {
        return userClient.getAccount(userNo);
    }
}
