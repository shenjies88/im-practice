package com.shenjies88.practice.im.backend.manager;

import com.shenjies88.practice.im.backend.constant.RedisKeys;
import com.shenjies88.practice.im.backend.vo.authentication.LoginRespVO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author shenjies88
 * @since 2020/11/5-9:53 PM
 */
@AllArgsConstructor(onConstructor_ = @Autowired)
@Component
public class MyCacheManager {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * token过期时间
     */
    private static final long TOKEN_EXPIRED = 60L * 24L;
    /**
     * token过期时间单位
     */
    private static final TimeUnit TOKEN_UNIT = TimeUnit.MINUTES;

    /**
     * 缓存token
     *
     * @param token
     * @param vo
     */
    public void saveToken(String token, LoginRespVO vo) {
        redisTemplate.opsForValue().set(RedisKeys.createToken(token), vo, TOKEN_EXPIRED, TOKEN_UNIT);
    }

    /**
     * 获取vo
     *
     * @param token
     * @return
     */
    public LoginRespVO getToken(String token) {
        return (LoginRespVO) redisTemplate.opsForValue().get(RedisKeys.createToken(token));
    }

    /**
     * 缓存存活的token
     *
     * @param id    用户id
     * @param token
     */
    public void saveLiveToken(Integer id, String token) {
        redisTemplate.opsForValue().set(RedisKeys.createLiveToken(id), token, TOKEN_EXPIRED + 1, TimeUnit.MINUTES);
    }

    /**
     * 获取token
     *
     * @param id
     * @return
     */
    public String getLiveToken(Integer id) {
        return (String) redisTemplate.opsForValue().get(RedisKeys.createLiveToken(id));
    }
}
