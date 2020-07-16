package com.littlehow.apm.test.price.api.client;

import com.littlehow.apm.test.price.api.vo.BatchItemVO;
import com.littlehow.apm.test.price.api.vo.PriceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;


/**
 * 示例价格服务，将不会有持久化动作
 *
 * @author littlehow
 */
@Api(tags = "获取价格信息")
@FeignClient("apm-price")
@RequestMapping("/apm/test/price")
public interface PriceClient {

    @ApiOperation("根据商品编号获取价格信息, 模拟get请求")
    @RequestMapping(value = "/get", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    PriceVO queryByItemNo(@RequestParam("itemNo") String itemNo, @RequestParam(required = false, value = "userNo") String userNo);

    @ApiOperation("根据多个商品编号和用户编号获取商品价格")
    @RequestMapping(value = "/batchQuery", method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
    List<PriceVO> queryPrice(@RequestBody @Valid BatchItemVO batchItem);
}
