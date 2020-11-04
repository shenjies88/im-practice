package com.shenjies88.im.netty.cache;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author shenjies88
 * @since 2020/11/04/16:40
 */
@Slf4j
public class MemberChannelCache {

    private static final Map<Integer, Channel> memberChannelMap = new ConcurrentHashMap();
    private static final Map<Channel, Integer> channelMemberMap = new ConcurrentHashMap();

    /**
     * 保存
     *
     * @param memberId 会员id
     * @param ctx      管道上下文
     */
    public static boolean save(Integer memberId, ChannelHandlerContext ctx) {
        //查看该用户当前管道是否有对应的id
        Integer cacheId = channelMemberMap.get(ctx.channel());
        //如果id存在，拒绝保存
        if (cacheId != null) {
            return false;
        }
        Channel channel = memberChannelMap.get(memberId);
        //如果管道存在，拒绝保存
        if (channel != null) {
            return false;
        }
        memberChannelMap.putIfAbsent(memberId, ctx.channel());
        channelMemberMap.putIfAbsent(ctx.channel(), memberId);
        log.info("绑定会员 id:{}", memberId);
        log.info("memberChannelMap size {}", memberChannelMap.size());
        log.info("channelMemberMap size {}", channelMemberMap.size());
        return true;
    }

    /**
     * 移除
     *
     * @param channel 管道
     */
    public static void remove(Channel channel) {
        Integer memberId = channelMemberMap.get(channel);
        channelMemberMap.remove(channel);
        if (memberId == null) {
            return;
        }
        memberChannelMap.remove(memberId);
        log.info("移除会员 id:{}", memberId);
        log.info("memberChannelMap size {}", memberChannelMap.size());
        log.info("channelMemberMap size {}", channelMemberMap.size());
    }
}
