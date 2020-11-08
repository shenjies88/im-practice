package com.shenjies88.practice.im.backend.service;

import com.shenjies88.practice.im.backend.client.RouterClient;
import com.shenjies88.practice.im.backend.entity.UserDO;
import com.shenjies88.practice.im.backend.manager.GroupMemberManager;
import com.shenjies88.practice.im.backend.mapper.GroupMemberMapper;
import com.shenjies88.practice.im.backend.mapper.UserMapper;
import com.shenjies88.practice.im.backend.utils.TokenUtil;
import com.shenjies88.practice.im.backend.vo.authentication.LoginReqVO;
import com.shenjies88.practice.im.backend.vo.authentication.RegReqVO;
import com.shenjies88.practice.im.common.bean.manager.MyCacheManager;
import com.shenjies88.practice.im.common.vo.ContextTokenVO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;
import java.util.UUID;

/**
 * @author shenjies88
 * @since 2020/11/5-9:34 PM
 */
@AllArgsConstructor(onConstructor_ = @Autowired)
@Service
public class AuthenticationService {

    private final UserMapper userMapper;
    private final GroupMemberMapper groupMemberMapper;
    private final MyCacheManager cacheManager;
    private final GroupMemberManager groupMemberManager;
    private final RouterClient routerClient;

    /**
     * 生成VO并缓存token
     *
     * @param entity
     * @return
     */
    private ContextTokenVO createContextTokenCache(UserDO entity) {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        ContextTokenVO vo = ContextTokenVO.builder()
                .account(entity.getAccount())
                .id(entity.getId())
                .nickname(entity.getNickname())
                .token(token).build();
        cacheManager.saveToken(token, vo);
        cacheManager.saveLiveToken(vo.getId(), token);
        return vo;
    }

    public String regLogin(RegReqVO params) {
        //账号不存在
        Assert.isTrue(userMapper.getByAccount(params.getAccount()) == null, "该账号已存在");
        UserDO build = UserDO.builder()
                .account(params.getAccount())
                .nickname(params.getNickname())
                .pwd(params.getPwd())
                .creatTime(new Date()).build();
        userMapper.insert(build);
        return createContextTokenCache(build).getToken();
    }

    public String login(LoginReqVO params) {
        UserDO userDO = userMapper.getByAccount(params.getAccount());
        boolean pass = userDO != null && userDO.getPwd().equals(params.getPwd());
        Assert.isTrue(pass, "账号或密码不匹配");
        //我在的群上线
        groupMemberManager.groupOnline(userDO.getId());
        return createContextTokenCache(userDO).getToken();
    }

    public void logout() {
        cacheManager.removeToken(TokenUtil.getContextToken().getToken());
        cacheManager.removeLiveToken(TokenUtil.getContextToken().getId());
        //调用router通知netty删除对应管道
        routerClient.logout(TokenUtil.getContextToken().getId());
        //删除redis登陆信息
        cacheManager.removeUserNettyLogin(TokenUtil.getContextToken().getId());
        //我在的群下线
        groupMemberManager.groupOffline();
    }
}
