package com.shenjies88.practice.im.netty.handler;

import com.alibaba.fastjson.JSON;
import com.shenjies88.practice.im.common.dto.base.MessageDTO;
import com.shenjies88.practice.im.netty.cache.MemberChannelCache;
import com.shenjies88.practice.im.netty.manager.MyMessageManager;
import com.shenjies88.practice.im.netty.service.MessageService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;


/**
 * @author shenjies88
 */
@Slf4j
@ChannelHandler.Sharable
@AllArgsConstructor(onConstructor_ = {@Autowired})
@Component
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    private final MyMessageManager messageManager;
    private final MessageService messageService;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        String body = "";
        if (frame instanceof TextWebSocketFrame) {
            body = ((TextWebSocketFrame) frame).text();
        }
        //无消息体
        Assert.hasText(body, "无消息发送");
        //无效的序列化
        MessageDTO messageDTO;
        try {
            messageDTO = JSON.parseObject(body, MessageDTO.class);
        } catch (Exception e) {
            messageManager.writeErrorClose(ctx, "无效的消息格式");
            return;
        }
        log.info("\n\n------------收到的消息------------");
        log.info("\n消息体 {}\n\n", messageDTO);
        switch (messageDTO.getType()) {
            case LOGIN:
                messageService.handLogin(ctx, messageDTO);
                break;
            case LOGOUT:
                messageService.handLogout(ctx);
                break;
            case SINGLE_CHAT:
                messageService.handSingleChat(ctx, messageDTO, body);
                break;
            case GROUP_CHAT:
                messageService.handGroupChat(ctx, messageDTO, body);
                break;
            default:
                messageManager.writeErrorClose(ctx, "无效的消息类型");
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.warn("\n\n------------管道关闭------------");
        log.warn("\n管道id:{}\n\n", ctx.channel().id().asLongText());
        MemberChannelCache.remove(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.warn("\n\n------------管道异常------------");
        log.error("\n管道id:{} 发生异常\n\n", ctx.channel().id().asLongText(), cause);
        if (cause instanceof IllegalArgumentException) {
            messageManager.writeErrorClose(ctx, cause.getMessage());
        }
        ctx.channel().close();
        MemberChannelCache.remove(ctx);
    }
}