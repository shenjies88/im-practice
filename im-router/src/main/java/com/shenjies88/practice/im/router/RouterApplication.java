package com.shenjies88.practice.im.router;

import com.shenjies88.practice.im.common.constant.CommonConstant;
import com.shenjies88.practice.im.router.constant.RouterConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author shenjies88
 * @since 2020/11/03/13:54
 */
@SpringBootApplication(scanBasePackages = {RouterConstant.MY_BASE_PACKAGES, CommonConstant.COMMON_BASE_PACKAGES})
public class RouterApplication {

    public static void main(String[] args) {
        SpringApplication.run(RouterApplication.class, args);
    }
}
