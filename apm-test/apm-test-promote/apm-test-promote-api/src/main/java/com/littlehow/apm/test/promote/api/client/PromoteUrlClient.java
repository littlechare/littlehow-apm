package com.littlehow.apm.test.promote.api.client;

import com.littlehow.apm.test.promote.api.vo.PromoteVO;
import com.littlehow.apm.test.promote.api.vo.QueryPromoteReqVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 使用url模式
 * @author littlehow
 */
@Api(tags = "活动操作接口")
@FeignClient(name = "apm-promote", url = "${promote.api.url:}")
@RequestMapping("/apm/test/promote")
public interface PromoteUrlClient {
    /**
     * 这里就偷懒直接使用查询req
     * 添加活动信息
     * @param req -
     * @return
     */
    @ApiOperation("添加活动")
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    PromoteVO addPromote(QueryPromoteReqVO req);
}
