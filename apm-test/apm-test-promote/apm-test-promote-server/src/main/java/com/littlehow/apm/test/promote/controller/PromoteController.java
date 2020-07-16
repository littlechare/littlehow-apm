package com.littlehow.apm.test.promote.controller;

import com.littlehow.apm.base.util.ApmBeanUtils;
import com.littlehow.apm.test.promote.api.client.PromoteClient;
import com.littlehow.apm.test.promote.api.vo.PromoteVO;
import com.littlehow.apm.test.promote.api.vo.QueryPromoteBatchReqVO;
import com.littlehow.apm.test.promote.api.vo.QueryPromoteReqVO;
import com.littlehow.apm.test.promote.service.PromoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 所有不判空指针都是因为测试类都有返回值
 * @author littlehow
 */
@RestController
public class PromoteController implements PromoteClient {

    @Autowired
    private PromoteService promoteService;

    @Override
    public PromoteVO queryPromote(@RequestBody @Valid QueryPromoteReqVO req) {
        return ApmBeanUtils.copy(promoteService.getPromote(req.getUserNo(), req.getItemNo()), PromoteVO.class);
    }

    @Override
    public List<PromoteVO> queryPromoteBatch(@RequestBody @Valid QueryPromoteBatchReqVO req) {
        return promoteService.getPromotes(req.getUserNo(), req.getItemNo())
                .stream()
                .map(o -> ApmBeanUtils.copyNewOnNull(o, PromoteVO.class))
                .collect(Collectors.toList());
    }
}
