package com.shenjies88.practice.im.backend.manager;

import com.alibaba.fastjson.JSON;
import com.shenjies88.practice.im.backend.constant.RedisKeys;
import com.shenjies88.practice.im.backend.vo.ContextTokenVO;
import com.shenjies88.practice.im.common.vo.ServiceMetadataVO;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
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
     * token 过期时间
     */
    private static final long TOKEN_EXPIRED = 60L * 24L;
    /**
     * token 过期时间单位
     */
    private static final TimeUnit TOKEN_UNIT = TimeUnit.MINUTES;

    /**
     * 缓存 token
     *
     * @param token
     * @param vo
     */
    public void saveToken(String token, ContextTokenVO vo) {
        redisTemplate.opsForValue().set(RedisKeys.createToken(token), vo, TOKEN_EXPIRED, TOKEN_UNIT);
    }

    /**
     * 获取 vo
     *
     * @param token
     * @return
     */
    public ContextTokenVO getToken(String token) {
        return (ContextTokenVO) redisTemplate.opsForValue().get(RedisKeys.createToken(token));
    }

    /**
     * 删除 token
     *
     * @param token
     */
    public void removeToken(String token) {
        redisTemplate.delete(RedisKeys.createToken(token));
    }

    /**
     * 缓存 live-token
     *
     * @param id    用户id
     * @param token
     */
    public void saveLiveToken(Integer id, String token) {
        redisTemplate.opsForValue().set(RedisKeys.createLiveToken(id), token, TOKEN_EXPIRED + 1, TimeUnit.MINUTES);
    }

    /**
     * 获取 live-token
     *
     * @param id
     * @return
     */
    public String getLiveToken(Integer id) {
        return (String) redisTemplate.opsForValue().get(RedisKeys.createLiveToken(id));
    }

    /**
     * 删除 live-token
     *
     * @param id
     */
    public void removeLiveToken(Integer id) {
        redisTemplate.delete(RedisKeys.createLiveToken(id));
    }

    /**
     * 保存 用户netty登陆地址
     *
     * @param vo
     */
    public void saveUserNettyLogin(ServiceMetadataVO vo) {
        redisTemplate.opsForHash().putAll(RedisKeys.createUserNettyLogin(vo.getId()), (Map<?, ?>) JSON.toJSON(vo));
    }
}
