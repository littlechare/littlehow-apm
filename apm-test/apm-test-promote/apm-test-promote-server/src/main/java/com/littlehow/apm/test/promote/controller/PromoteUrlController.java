package com.littlehow.apm.test.promote.controller;

import com.littlehow.apm.base.util.ApmBeanUtils;
import com.littlehow.apm.test.promote.api.client.PromoteUrlClient;
import com.littlehow.apm.test.promote.api.vo.PromoteVO;
import com.littlehow.apm.test.promote.api.vo.QueryPromoteReqVO;
import com.littlehow.apm.test.promote.service.PromoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author littlehow
 */
@RestController
public class PromoteUrlController implements PromoteUrlClient {

    @Autowired
    private PromoteService promoteService;

    @Override
    public PromoteVO addPromote(@RequestBody @Valid QueryPromoteReqVO req) {
        return ApmBeanUtils.copy(promoteService.getPromote(req.getUserNo(), req.getItemNo()), PromoteVO.class);
    }
}
