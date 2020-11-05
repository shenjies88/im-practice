package com.shenjies88.practice.im.router.hold;

import org.springframework.cloud.client.ServiceInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shenjies88
 * @since 2020/11/03/18:27
 */
public class MyServerHold {

    public static List<ServiceInstance> serverList = new ArrayList<>(0);
    public static int currIndex = 0;
}
