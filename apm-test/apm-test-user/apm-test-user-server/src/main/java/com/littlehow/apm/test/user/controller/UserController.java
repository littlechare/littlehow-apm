package com.littlehow.apm.test.user.controller;

import com.littlehow.apm.test.user.api.client.UserClient;
import com.littlehow.apm.test.user.api.vo.LoginRequestVO;
import com.littlehow.apm.test.user.api.vo.UserAccountVO;
import com.littlehow.apm.test.user.api.vo.UserInfoVO;
import com.littlehow.apm.test.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author littlehow
 */
@RestController
public class UserController implements UserClient {

    @Autowired
    private UserService userService;

    @Override
    public UserInfoVO login(@RequestBody @Valid LoginRequestVO request) {
        return userService.login(request);
    }

    @Override
    public UserInfoVO getInfo(@PathVariable String userNo) {
        return userService.get(userNo);
    }

    @Override
    public UserAccountVO getAccount(@PathVariable String userNo) {
        return userService.getAccount(userNo);
    }
}
