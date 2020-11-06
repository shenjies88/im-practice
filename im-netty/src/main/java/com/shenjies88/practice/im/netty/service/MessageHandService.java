package com.shenjies88.practice.im.netty.service;

import com.alibaba.fastjson.JSON;
import com.shenjies88.practice.im.common.bean.client.NettyClient;
import com.shenjies88.practice.im.common.bean.manager.MyCacheManager;
import com.shenjies88.practice.im.common.dto.base.LoginTypeDTO;
import com.shenjies88.practice.im.common.dto.base.MessageDTO;
import com.shenjies88.practice.im.common.vo.ServiceMetadataVO;
import com.shenjies88.practice.im.netty.cache.MemberChannelCache;
import com.shenjies88.practice.im.netty.manager.MyMessageManager;
import com.shenjies88.practice.im.netty.utils.MsgPreCheckUtil;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;


/**
 * @author shenjies88
 * @since 2020/11/04/16:27
 */
@Slf4j
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Service
public class MessageHandService {

    private final MyMessageManager messageManager;
    private final MyCacheManager cacheManager;
    private final NettyClient nettyClient;

    /**
     * 发送私聊消息
     *
     * @param ctx        管道上下文
     * @param messageDTO 消息体
     * @param body       消息体json
     * @param toMemberId 接受用户id
     */
    private void sendSingleChat(ChannelHandlerContext ctx, MessageDTO messageDTO, String body, Integer toMemberId) {
        //缓存中获取目标会员管道
        ChannelHandlerContext toCtx = MemberChannelCache.get(toMemberId);
        if (toCtx == null) {
            //从redis获取用户信息
            ServiceMetadataVO userNettyLogin = cacheManager.getUserNettyLogin(toMemberId);
            if (userNettyLogin == null) {
                log.warn("用户 id:{} 不在线", toMemberId);
                messageManager.writeError(ctx, "目标用户不在线");
                return;
            }
            //异步调用 用户所在的netty服务发送消息
            nettyClient.sendSingleChat(NettyClient.createBaseUrl(userNettyLogin.getHost(), userNettyLogin.getServerPort()), messageDTO, toMemberId);
            return;
        }
        //发送给目标用户
        messageManager.writeBody(toCtx, body);
        messageManager.writeSuccessful(ctx);
    }

    /**
     * 发送群聊消息
     *
     * @param ctx        管道上下文
     * @param messageDTO 消息体
     * @param body       消息体json
     * @param groupId    群id
     */
    private void sendGroupChat(ChannelHandlerContext ctx, MessageDTO messageDTO, String body, Integer groupId) {
        //TODO 从redis中获取群在线用户信息，不在本实例的用户批量一次http请求发送到另一个实例的接口
        //TODO 发送给目标群
        messageManager.writeSuccessful(ctx);
    }

    /**
     * 登录处理
     *
     * @param ctx        管道上下文
     * @param messageDTO 消息体
     */
    public void handLogin(ChannelHandlerContext ctx, MessageDTO messageDTO) {
        //序列化
        LoginTypeDTO loginTypeDTO;
        try {
            loginTypeDTO = JSON.parseObject(messageDTO.getContentJson(), LoginTypeDTO.class);
        } catch (Exception e) {
            messageManager.writeErrorClose(ctx, "无效的内容类型");
            return;
        }
        //校验
        Assert.notNull(loginTypeDTO.getMemberId(), "会员id不能为空");
        //绑定通道和会员id
        boolean save = MemberChannelCache.save(loginTypeDTO.getMemberId(), ctx);
        if (save) {
            messageManager.writeSuccessful(ctx);
        } else {
            messageManager.writeError(ctx, "您已登录");
        }
    }

    /**
     * 下线处理
     *
     * @param ctx 管道上下文
     */
    public void handLogout(ChannelHandlerContext ctx) {
        //移除通道和会员的缓存
        MemberChannelCache.remove(ctx);
        ctx.channel().close();
    }

    /**
     * 处理私聊消息
     *
     * @param ctx        管道上下文
     * @param messageDTO 消息体
     * @param body       消息体json
     */
    public void handSingleChat(ChannelHandlerContext ctx, MessageDTO messageDTO, String body) {
        //是已登录
        Assert.notNull(MemberChannelCache.get(ctx), "您未登录");
        switch (messageDTO.getContentType()) {
            case TXT:
                sendSingleChat(ctx, messageDTO, body, MsgPreCheckUtil.singleChatTxtPreCheck(ctx, messageDTO));
                break;
            default:
                messageManager.writeErrorClose(ctx, "无效的内容类型");
        }
    }

    /**
     * 处理群聊消息
     *
     * @param ctx        管道上下文
     * @param messageDTO 消息体
     * @param body       消息体json
     */
    public void handGroupChat(ChannelHandlerContext ctx, MessageDTO messageDTO, String body) {
        //是已登录
        Assert.notNull(MemberChannelCache.get(ctx), "您未登录");
        switch (messageDTO.getContentType()) {
            case TXT:
                sendGroupChat(ctx, messageDTO, body, MsgPreCheckUtil.groupChatTxtPreCheck(messageDTO));
                break;
            default:
                messageManager.writeErrorClose(ctx, "无效的内容类型");
        }
    }
}
