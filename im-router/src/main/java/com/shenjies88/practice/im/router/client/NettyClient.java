package com.shenjies88.practice.im.router.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

/**
 * @author shies88
 * @since 2020/11/6-10:01 AM
 */
@Slf4j
@Component
public class NettyClient {

    /**
     * 通知对应netty服务删除对应管道
     *
     * @param baseUrl netty服务地址
     * @param id      用户id
     */
    public void logout(String baseUrl, Integer id) {
        try {
            WebClient.create(baseUrl).delete()
                    .uri("/netty/" + id)
                    .retrieve()
                    .bodyToMono(Map.class).subscribe();
        } catch (Exception e) {
            log.error("通知对应netty服务删除对应管道 异常", e);
        }
    }
}
