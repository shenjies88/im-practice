package com.shenjies88.practice.im.netty.server;

import com.shenjies88.practice.im.netty.initializer.WebSocketServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author shenjies88
 */
@Slf4j
@Component
public class WebSocketServer {

    @Value("${spring.cloud.zookeeper.discovery.instance-port}")
    private Integer port;
    @Value("${spring.cloud.zookeeper.discovery.instance-host}")
    private String host;
    @Autowired
    private WebSocketServerInitializer initializer;

    private final EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    private final EventLoopGroup workerGroup = new NioEventLoopGroup();

    @PostConstruct
    public void init() throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(initializer);
        log.warn("netty服务 port {}", port);
        bootstrap.bind(host, port).sync().addListener(future -> {
            if (future.isSuccess()) {
                log.warn("netty服务启动成功");
            } else {
                log.warn("netty服务启动失败");
            }
        });
    }

    @PreDestroy
    public void destroy() {
        bossGroup.shutdownGracefully().syncUninterruptibly();
        workerGroup.shutdownGracefully().syncUninterruptibly();
        log.warn("netty服务关闭 port {}", port);
    }
}