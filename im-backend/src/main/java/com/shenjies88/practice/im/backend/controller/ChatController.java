package com.shenjies88.practice.im.backend.controller;

import com.shenjies88.practice.im.backend.service.ChatService;
import com.shenjies88.practice.im.common.vo.HttpResultVO;
import com.shenjies88.practice.im.common.vo.ServiceMetadataVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shenjies88
 * @since 2020/11/6-9:41 AM
 */
@Api(tags = "聊天接口")
@AllArgsConstructor(onConstructor_ = @Autowired)
@RequestMapping("/im/chat")
@RestController
public class ChatController {

    private final ChatService chatService;

    @ApiOperation("获取netty地址")
    @PostMapping("/get-netty-addr")
    public HttpResultVO<ServiceMetadataVO> getNettyAddr() {
        return HttpResultVO.success(chatService.getNettyAddr());
    }
}
