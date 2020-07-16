package com.littlehow.apm.test.promote.api.client;

import com.littlehow.apm.test.promote.api.vo.PromoteVO;
import com.littlehow.apm.test.promote.api.vo.QueryPromoteBatchReqVO;
import com.littlehow.apm.test.promote.api.vo.QueryPromoteReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 *
 * 活动接口
 * @author littlehow
 */
@Api(tags = "活动接口")
@FeignClient("apm-promote")
@RequestMapping("/apm/test/promote")
public interface PromoteClient {

    @ApiOperation("查询活动信息")
    @RequestMapping(value = "/query", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    PromoteVO queryPromote(QueryPromoteReqVO req);

    @ApiOperation("查询活动信息")
    @RequestMapping(value = "/query/batch", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    List<PromoteVO> queryPromoteBatch(QueryPromoteBatchReqVO req);
}
