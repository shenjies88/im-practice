package com.shenjies88.practice.im.router.controller;

import com.shenjies88.practice.im.common.vo.HttpResultVO;
import com.shenjies88.practice.im.common.vo.ServiceMetadataVO;
import com.shenjies88.practice.im.router.service.RouterService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shenjies88
 * @since 2020/11/03/14:25
 */
@Slf4j
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Api(tags = "路由接口")
@RequestMapping("/router")
@RestController
public class RouterController {

    private final RouterService routerService;

    @ApiOperation("获取服务")
    @GetMapping
    public HttpResultVO<ServiceMetadataVO> serviceInstance() {
        return HttpResultVO.success(routerService.serviceInstance());
    }
}
