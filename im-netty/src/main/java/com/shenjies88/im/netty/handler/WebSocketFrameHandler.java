package com.shenjies88.im.netty.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;


/**
 * @author shenjies88
 */
@Slf4j
@ChannelHandler.Sharable
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {

    /**
     * 发送消息
     *
     * @param ctx 管道上下文
     * @param msg 消息
     */
    private void writeMsg(ChannelHandlerContext ctx, String msg) {
        ctx.channel().writeAndFlush(new TextWebSocketFrame(msg));
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        String body = "";
        if (frame instanceof TextWebSocketFrame) {
            body = ((TextWebSocketFrame) frame).text();
        }
        //无消息体
        if (StringUtils.isEmpty(body)) {
            writeMsg(ctx, "无消息发送");
            return;
        }
        //无效的序列化
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.warn("通道关闭 {}", ctx.channel().id().asLongText());
        //TODO 移除缓存
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("通道id:{} 发生异常", ctx.channel().id().asLongText(), cause);
        ctx.channel().close();
        //TODO 移除缓存
    }
}