package com.shenjies88.practice.im.netty.service;

import com.alibaba.fastjson.JSON;
import com.shenjies88.practice.im.common.bean.client.NettyClient;
import com.shenjies88.practice.im.common.bean.manager.MyCacheManager;
import com.shenjies88.practice.im.common.dto.GroupChatTxtDTO;
import com.shenjies88.practice.im.common.dto.SingleChatTxtDTO;
import com.shenjies88.practice.im.common.dto.base.LoginTypeDTO;
import com.shenjies88.practice.im.common.dto.base.MessageDTO;
import com.shenjies88.practice.im.common.vo.ServiceMetadataVO;
import com.shenjies88.practice.im.netty.cache.MemberChannelCache;
import com.shenjies88.practice.im.netty.manager.MyMessageManager;
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
public class MessageService {

    private final MyMessageManager messageManager;
    private final MyCacheManager cacheManager;
    private final NettyClient nettyClient;

    /**
     * 处理 私聊-文本
     *
     * @param ctx        管道上下文
     * @param messageDTO 消息体
     * @param body       消息体json
     */
    private void handSingleChatTxt(ChannelHandlerContext ctx, MessageDTO messageDTO, String body) {
        //序列化
        SingleChatTxtDTO singleChatTxtDTO;
        try {
            singleChatTxtDTO = JSON.parseObject(messageDTO.getContentJson(), SingleChatTxtDTO.class);
        } catch (Exception e) {
            messageManager.writeErrorClose(ctx, "无效的内容类型");
            return;
        }
        //校验
        Assert.hasText(singleChatTxtDTO.getMsg(), "消息内容不能为空");
        Integer toMemberId = singleChatTxtDTO.getToMemberId();
        Assert.notNull(toMemberId, "目标会员id不能为空");
        Integer myMemberId = MemberChannelCache.get(ctx);
        Assert.isTrue(!toMemberId.equals(myMemberId), "目标会员不能是自己");
        //缓存中获取目标会员管道
        ChannelHandlerContext toCtx = MemberChannelCache.get(toMemberId);
        if (toCtx == null) {
            //从redis获取用户信息
            ServiceMetadataVO userNettyLogin = cacheManager.getUserNettyLogin(toMemberId);
            if (userNettyLogin == null) {
                messageManager.writeError(ctx, "目标用户不在线");
                return;
            }
            //异步调用 用户所在的netty服务发送消息
            nettyClient.sendSingleChat(NettyClient.createBaseUrl(userNettyLogin.getHost(), userNettyLogin.getServerPort()), messageDTO, toMemberId);
        }
        //发送给目标用户
        messageManager.writeBody(toCtx, body);
        messageManager.writeSuccessful(ctx);
    }

    /**
     * 处理 群聊-文本
     *
     * @param ctx        管道上下文
     * @param messageDTO 消息体
     * @param body       消息体json
     */
    private void handGroupChatTxt(ChannelHandlerContext ctx, MessageDTO messageDTO, String body) {
        //序列化
        GroupChatTxtDTO groupChatTxtDTO;
        try {
            groupChatTxtDTO = JSON.parseObject(messageDTO.getContentJson(), GroupChatTxtDTO.class);
        } catch (Exception e) {
            messageManager.writeErrorClose(ctx, "无效的内容类型");
            return;
        }
        //校验
        Assert.hasText(groupChatTxtDTO.getMsg(), "消息内容不能为空");
        Integer groupId = groupChatTxtDTO.getGroupId();
        Assert.notNull(groupId, "目标群id不能为空");

        //TODO 从redis中获取群在线用户信息，不在本实例的用户批量一次http请求发送到另一个实例的接口

        //发送给目标群

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
                handSingleChatTxt(ctx, messageDTO, body);
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
                handGroupChatTxt(ctx, messageDTO, body);
                break;
            default:
                messageManager.writeErrorClose(ctx, "无效的内容类型");
        }
    }
}
