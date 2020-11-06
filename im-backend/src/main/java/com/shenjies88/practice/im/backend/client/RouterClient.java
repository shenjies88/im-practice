package com.shenjies88.practice.im.backend.client;

import com.shenjies88.practice.im.common.vo.HttpResultVO;
import com.shenjies88.practice.im.common.vo.ServiceMetadataVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * @author shenjies88
 * @since 2020/11/6-10:01 AM
 */
@Slf4j
@Configuration
public class RouterClient {

    @Value("${router-base-url}")
    private String baseUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.create(baseUrl);
    }

    public ServiceMetadataVO getNettyAddr() {
        try {
            HttpResultVO<ServiceMetadataVO> result = webClient().get()
                    .uri("/router")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<HttpResultVO<ServiceMetadataVO>>() {
                    })
                    .block();
            log.info("获取netty服务结果 {}", result);
            Assert.isTrue(result.getStatus(), "服务器异常，请稍后再试");
            return result.getData();
        } catch (Exception e) {
            log.error("获取netty服务失败", e);
            Assert.isTrue(false, "服务器异常，请稍后再试");
            return null;
        }
    }
}
