package com.shenjies88.im.router.listener;

import com.shenjies88.im.router.constant.RouterConstant;
import com.shenjies88.im.router.hold.MyServerHold;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.zookeeper.discovery.watcher.DependencyState;
import org.springframework.cloud.zookeeper.discovery.watcher.DependencyWatcherListener;
import org.springframework.stereotype.Component;

/**
 * @author shenjies88
 * @since 2020/11/03/17:11
 */
@Slf4j
@Component
public class ZkListener implements DependencyWatcherListener {

    @Autowired
    private DiscoveryClient client;

    @Override
    public void stateChanged(String dependencyName, DependencyState newState) {
        MyServerHold.serverList = client.getInstances(RouterConstant.NETTY_SERVER_ID);
    }
}
