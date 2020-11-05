package com.shenjies88.practice.im.backend;

import com.shenjies88.practice.im.backend.constant.BackendConstant;
import com.shenjies88.practice.im.common.constant.CommonConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author shenjies88
 * @since 2020/11/5-5:16 PM
 */
@SpringBootApplication(scanBasePackages = {BackendConstant.MY_BASE_PACKAGES, CommonConstant.COMMON_BASE_PACKAGES})
public class BackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }
}
