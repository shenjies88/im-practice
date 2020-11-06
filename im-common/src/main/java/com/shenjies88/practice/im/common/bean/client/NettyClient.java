package com.shenjies88.practice.im.common.bean.client;

import com.shenjies88.practice.im.common.dto.base.MessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

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
        WebClient.create(baseUrl).delete()
                .uri("/netty/" + id)
                .retrieve()
                .bodyToMono(Map.class)
                .doOnError(Exception.class, e ->
                        log.error("通知对应netty服务删除对应管道 异常", e))
                .subscribe();
    }

    /**
     * 发送私聊消息
     */
    public void sendSingleChat(String baseUrl, MessageDTO messageDTO, Integer toMemberId) {
        WebClient.create(baseUrl).post()
                .uri("/netty/" + toMemberId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(messageDTO), MessageDTO.class)
                .retrieve()
                .bodyToMono(Map.class)
                .doOnError(Exception.class, e ->
                        log.error("发送私聊消息 异常", e)
                ).subscribe();
    }

    public static String createBaseUrl(String host, String port) {
        return "http://" + host + ":" + port;
    }
}
