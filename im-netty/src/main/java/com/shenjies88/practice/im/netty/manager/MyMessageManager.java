package com.shenjies88.practice.im.netty.manager;

import com.alibaba.fastjson.JSON;
import com.shenjies88.practice.im.netty.constant.MessageTypeEnum;
import com.shenjies88.practice.im.netty.dto.base.MessageDTO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author shenjies88
 * @since 2020/11/04/16:10
 */
@Slf4j
@Component
public class MyMessageManager {

    /**
     * 发送错误消息并且关闭链接
     */
    public void writeErrorClose(ChannelHandlerContext ctx, String msg) {
        MessageDTO messageDTO = MessageDTO.builder()
                .type(MessageTypeEnum.ERROR)
                .contentJson(msg).build();
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(messageDTO)));
        ctx.channel().close();
    }

    /**
     * 发送错误消息
     */
    public void writeError(ChannelHandlerContext ctx, String msg) {
        MessageDTO messageDTO = MessageDTO.builder()
                .type(MessageTypeEnum.ERROR)
                .contentJson(msg).build();
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(messageDTO)));
    }

    /**
     * 成功响应
     */
    public void writeSuccessful(ChannelHandlerContext ctx) {
        MessageDTO messageDTO = MessageDTO.builder()
                .type(MessageTypeEnum.SUCCESSFUL)
                .contentJson("发送成功").build();
        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(messageDTO)));
    }

    /**
     * 发送 Body
     */
    public void writeBody(ChannelHandlerContext ctx, String body) {
        ctx.channel().writeAndFlush(new TextWebSocketFrame(body));
    }
}
