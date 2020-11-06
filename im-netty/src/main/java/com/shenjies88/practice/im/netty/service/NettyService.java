package com.shenjies88.practice.im.netty.service;

import com.alibaba.fastjson.JSON;
import com.shenjies88.practice.im.common.dto.base.MessageDTO;
import com.shenjies88.practice.im.netty.cache.MemberChannelCache;
import com.shenjies88.practice.im.netty.manager.MyMessageManager;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shenjies88
 * @since 2020/11/6-11:05 AM
 */
@Slf4j
@AllArgsConstructor(onConstructor_ = @Autowired)
@Service
public class NettyService {

    private final MyMessageManager messageManager;

    public void logout(Integer id) {
        ChannelHandlerContext ctx = MemberChannelCache.get(id);
        if (ctx == null) {
            return;
        }
        log.warn("用户 id: {} 下线", id);
        MemberChannelCache.remove(ctx);
    }

    public void handSingleChat(Integer id, MessageDTO prams) {
        //接受消息的管道存在
        ChannelHandlerContext ctx = MemberChannelCache.get(id);
        if (ctx == null) {
            log.warn("用户 id:{} 不在线", id);
            return;
        }
        messageManager.writeBody(ctx, JSON.toJSONString(prams));
    }
}
