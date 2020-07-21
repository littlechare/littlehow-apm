package com.littlehow.apm.test.user.service;

import com.littlehow.apm.base.util.ApmAssert;
import com.littlehow.apm.test.base.util.IDCreator;
import com.littlehow.apm.test.user.api.vo.LoginRequestVO;
import com.littlehow.apm.test.user.api.vo.UserAccountVO;
import com.littlehow.apm.test.user.api.vo.UserInfoVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import static com.littlehow.apm.test.base.util.RandomUtils.*;

/**
 *
 * 偷懒直接使用VO
 * @author littlehow
 */
@Service
public class UserService {

    private static String[] names = {"littlehow", "color wolf", "pig", "cat", "dog", "july", "cherry"};

    /**
     * 加上一定几率的错误
     * @param request -
     * @return -
     */
    public UserInfoVO login(LoginRequestVO request) {
        int range = randomCount(0, 40);
        ApmAssert.isTrue(range < 30, "密码错误:{0}", request.getPassword());
        ApmAssert.isTrue(range < 20, "账户错误:{0}", request.getMobilePhone());
        return createUser(IDCreator.get());
    }

    public UserInfoVO get(String userNo) {
        int range = randomCount();
        ApmAssert.isTrue(range < 42, "用户[{0}]不存在，请检查", userNo);
        return createUser(userNo);
    }


    public UserAccountVO getAccount(String userNo) {
        int range = randomCount();
        ApmAssert.isTrue(range < 42, "用户[{0}]不存在，请检查", userNo);
        UserAccountVO account = new UserAccountVO();
        account.setAccount(IDCreator.get());
        account.setAmount(randomPrice());
        account.setFreezeAmount(randomPrice());
        account.setUserNo(userNo);
        return account;
    }

    private UserInfoVO createUser(String userNo) {
        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setAge(randomCount(18, 70));
        userInfo.setBirthDay(LocalDate.now().minusYears(userInfo.getAge()));
        userInfo.setCertificateNo(IDCreator.get());
        // 本人csdn头像地址 -_-
        userInfo.setHeadUrl("https://profile.csdnimg.cn/1/2/5/1_w172087242");
        userInfo.setLastLoginTime(randomDateTime());
        userInfo.setNickName(names[randomCount(0, names.length - 1)]);
        userInfo.setMobilePhone(randomMobilePhone());
        userInfo.setUserName(names[randomCount(0, names.length - 1)]);
        userInfo.setUserNo(userNo);
        return userInfo;
    }
}
