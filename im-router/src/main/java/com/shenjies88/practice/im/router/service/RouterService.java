package com.shenjies88.practice.im.router.service;

import com.alibaba.fastjson.JSON;
import com.shenjies88.practice.im.common.bean.client.NettyClient;
import com.shenjies88.practice.im.common.bean.manager.MyCacheManager;
import com.shenjies88.practice.im.common.vo.ServiceMetadataVO;
import com.shenjies88.practice.im.router.constant.RouterConstant;
import com.shenjies88.practice.im.router.hold.MyServerHold;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * @author shenjies88
 * @since 2020/11/04/14:16
 */
@AllArgsConstructor(onConstructor_ = @Autowired)
@Service
public class RouterService {

    private final NettyClient nettyClient;
    private final DiscoveryClient discoveryClient;
    private final MyCacheManager cacheManager;

    public ServiceMetadataVO serviceInstance() {
        //服务列表为空，请求一次服务服务列表
        if (CollectionUtils.isEmpty(MyServerHold.serverList)) {
            MyServerHold.serverList = discoveryClient.getInstances(RouterConstant.NETTY_SERVER_ID);
        }
        if (CollectionUtils.isEmpty(MyServerHold.serverList)) {
            return null;
        }
        //不为空则按取余遍历返回
        ServiceInstance serviceInstance = MyServerHold.serverList.get((++MyServerHold.currIndex) % MyServerHold.serverList.size());
        if (serviceInstance == null) {
            return null;
        }
        return JSON.parseObject(JSON.toJSONString(serviceInstance.getMetadata()), ServiceMetadataVO.class);
    }

    public void logout(Integer id) {
        //从redis中获取用户登陆所在netty服务地址
        ServiceMetadataVO serviceMetadataVO = cacheManager.getUserNettyLogin(id);
        if (serviceMetadataVO == null) {
            return;
        }
        //异步调用对应的netty服务删除管道实例
        nettyClient.logout(NettyClient.createBaseUrl(serviceMetadataVO.getHost(), serviceMetadataVO.getServerPort()), id);
    }
}
