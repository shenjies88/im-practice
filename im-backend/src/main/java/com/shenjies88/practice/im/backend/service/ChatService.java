package com.shenjies88.practice.im.backend.service;

import com.shenjies88.practice.im.backend.client.RouterClient;
import com.shenjies88.practice.im.backend.utils.TokenUtil;
import com.shenjies88.practice.im.common.bean.manager.MyCacheManager;
import com.shenjies88.practice.im.common.vo.ServiceMetadataVO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author shenjies88
 * @since 2020/11/6-9:42 AM
 */
@AllArgsConstructor(onConstructor_ = @Autowired)
@Service
public class ChatService {

    private final RouterClient routerClient;
    private final MyCacheManager cacheManager;

    public ServiceMetadataVO getNettyAddr() {
        //请求router获取一个netty服务地址
        ServiceMetadataVO nettyAddr = routerClient.getNettyAddr();
        nettyAddr.setId(TokenUtil.getContextToken().getId());
        //保存当前用户的信息至redis
        cacheManager.saveUserNettyLogin(nettyAddr);
        return nettyAddr;
    }
}
