package com.shenjies88.im.router.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenjies88
 * @since 2020/11/03/14:25
 */
@Slf4j
@Api(tags = "路由接口")
@RequestMapping("/router")
@RestController
public class RouterController {

    @Autowired
    private DiscoveryClient client;

    @ApiOperation("服务列表")
    @GetMapping("/list")
    public List<List<ServiceInstance>> serviceUrl() {
        List<String> services = client.getServices();
        List<List<ServiceInstance>> result = new ArrayList<>();
        services.forEach(id -> {
            try {
                List<ServiceInstance> instances = client.getInstances(id);
                if (!CollectionUtils.isEmpty(instances)) {
                    result.add(instances);
                }
            } catch (Exception e) {

            }
        });
        return result;
    }
}
