package com.shenjies88.practice.im.netty.initializer;

import com.shenjies88.practice.im.netty.constant.NettyConstant;
import com.shenjies88.practice.im.netty.handler.WebSocketFrameHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author shenjies88
 */
@Component
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {

    @Autowired
    private WebSocketFrameHandler webSocketFrameHandler;

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebSocketServerCompressionHandler());
        pipeline.addLast(new WebSocketServerProtocolHandler(NettyConstant.WEBSOCKET_PATH, null, true));
        pipeline.addLast(webSocketFrameHandler);
    }
}