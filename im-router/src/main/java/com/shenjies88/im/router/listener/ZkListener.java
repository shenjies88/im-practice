package com.shenjies88.im.router.listener;

import lombok.extern.slf4j.Slf4j;
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

    @Override
    public void stateChanged(String dependencyName, DependencyState newState) {
        log.warn(" {} {}", dependencyName, newState);
    }
}
