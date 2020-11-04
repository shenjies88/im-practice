package com.shenjies88.im.netty.service;

import com.alibaba.fastjson.JSON;
import com.shenjies88.im.netty.cache.MemberChannelCache;
import com.shenjies88.im.netty.dto.SingleChatTxtDTO;
import com.shenjies88.im.netty.dto.base.LoginTypeDTO;
import com.shenjies88.im.netty.dto.base.MessageDTO;
import com.shenjies88.im.netty.manager.MyMessageManager;
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
        MemberChannelCache.remove(ctx.channel());
        ctx.channel().close();
    }

    /**
     * 处理私聊消息
     *
     * @param ctx
     * @param messageDTO
     */
    public void handSingleChat(ChannelHandlerContext ctx, MessageDTO messageDTO) {
        //是已登录
        Assert.notNull(MemberChannelCache.get(ctx.channel()), "您未登录");
        switch (messageDTO.getContentType()) {
            case TXT:
                handSingleChatTxt(ctx, messageDTO);
                break;
            default:
                messageManager.writeErrorClose(ctx, "无效的内容类型");
        }
    }

    /**
     * 处理 私聊-文本
     *
     * @param ctx  管道上下文
     * @param body 消息体
     */
    private void handSingleChatTxt(ChannelHandlerContext ctx, MessageDTO body) {
        //序列化
        SingleChatTxtDTO singleChatTxtDTO;
        try {
            singleChatTxtDTO = JSON.parseObject(body.getContentJson(), SingleChatTxtDTO.class);
        } catch (Exception e) {
            messageManager.writeErrorClose(ctx, "无效的内容类型");
            return;
        }
        //校验
        Assert.hasText(singleChatTxtDTO.getMsg(), "消息内容不能为空");
        Integer toMemberId = singleChatTxtDTO.getToMemberId();
        Assert.notNull(toMemberId, "目标会员id不能为空");
        Integer myMemberId = MemberChannelCache.get(ctx.channel());
        Assert.isTrue(!toMemberId.equals(myMemberId), "目标会员不能是自己");
    }
}
