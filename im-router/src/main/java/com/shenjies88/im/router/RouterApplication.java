package com.shenjies88.im.router;

import com.shenjies88.im.router.constant.MyConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author shenjies88
 * @since 2020/11/03/13:54
 */
@SpringBootApplication(scanBasePackages = {MyConstant.MY_BASE_PACKAGES, MyConstant.COMMON_BASE_PACKAGES})
public class RouterApplication {

    public static void main(String[] args) {
        SpringApplication.run(RouterApplication.class, args);
    }
}
