package com.shenjies88.im.router;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author shenjies88
 * @since 2020/11/03/13:54
 */
@SpringBootApplication(scanBasePackages = {"com.shenjies88.im.router", "com.shenjies88.im.common"})
public class RouterApplication {

    public static void main(String[] args) {
        SpringApplication.run(RouterApplication.class, args);
    }
}
