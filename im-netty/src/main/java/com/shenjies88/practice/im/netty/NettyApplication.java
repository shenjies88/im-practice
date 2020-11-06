package com.shenjies88.practice.im.netty;

import com.shenjies88.practice.im.common.constant.CommonConstant;
import com.shenjies88.practice.im.netty.constant.NettyConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author shenjies88
 * @since 2020/11/03/13:54
 */
@SpringBootApplication(scanBasePackages = {NettyConstant.MY_BASE_PACKAGES, CommonConstant.COMMON_BASE_PACKAGES})
public class NettyApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyApplication.class, args);
    }
}
