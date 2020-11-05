package com.shenjies88.practice.im.backend.service;

import com.shenjies88.practice.im.backend.entity.UserDO;
import com.shenjies88.practice.im.backend.manager.MyCacheManager;
import com.shenjies88.practice.im.backend.mapper.UserMapper;
import com.shenjies88.practice.im.backend.vo.authentication.LoginRespVO;
import com.shenjies88.practice.im.backend.vo.authentication.RegReqVO;
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
    private final MyCacheManager cacheManager;

    /**
     * 生成登陆VO并缓存token
     *
     * @param entity
     * @return
     */
    private LoginRespVO createLoginCacheVo(UserDO entity) {
        String token = UUID.randomUUID().toString().replaceAll("-", "");
        LoginRespVO vo = LoginRespVO.builder()
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
        return createLoginCacheVo(build).getToken();
    }

}
