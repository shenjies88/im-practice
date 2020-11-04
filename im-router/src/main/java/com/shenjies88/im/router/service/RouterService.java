package com.shenjies88.im.router.service;

import com.shenjies88.im.router.constant.MyConstant;
import com.shenjies88.im.router.hold.MyServerHold;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author shenjies88
 * @since 2020/11/04/14:16
 */
@Service
public class RouterService {

    @Autowired
    private DiscoveryClient client;

    public ServiceInstance serviceInstance() {
        //服务列表为空，请求一次服务服务列表
        if (CollectionUtils.isEmpty(MyServerHold.serverList)) {
            MyServerHold.serverList = client.getInstances(MyConstant.NETTY_SERVER_ID);
        }
        if (CollectionUtils.isEmpty(MyServerHold.serverList)) {
            return null;
        }
        //不为空则按取余遍历返回
        return MyServerHold.serverList.get((++MyServerHold.currIndex) % MyServerHold.serverList.size());
    }
}
