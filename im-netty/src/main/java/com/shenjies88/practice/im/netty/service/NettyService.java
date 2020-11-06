package com.shenjies88.practice.im.netty.service;

import com.shenjies88.practice.im.netty.cache.MemberChannelCache;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

/**
 * @author shenjies88
 * @since 2020/11/6-11:05 AM
 */
@Service
public class NettyService {

    public void logout(Integer id) {
        ChannelHandlerContext ctx = MemberChannelCache.get(id);
        if (ctx == null) {
            return;
        }
        MemberChannelCache.remove(ctx);
    }
}
