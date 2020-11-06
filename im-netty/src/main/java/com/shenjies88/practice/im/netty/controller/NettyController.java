package com.shenjies88.practice.im.netty.controller;

import com.shenjies88.practice.im.common.vo.HttpResultVO;
import com.shenjies88.practice.im.netty.service.NettyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shenjies88
 * @since 2020/11/03/14:25
 */
@Slf4j
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Api(tags = "Netty接口")
@RequestMapping("/netty")
@RestController
public class NettyController {

    private final NettyService nettyService;

    @ApiOperation("根据用户id找到对应的管道移除")
    @DeleteMapping("/{id}")
    public HttpResultVO<Void> logout(@PathVariable("id") Integer id) {
        nettyService.logout(id);
        return HttpResultVO.success();
    }
}
