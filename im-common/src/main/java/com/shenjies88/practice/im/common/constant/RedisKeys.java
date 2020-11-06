package com.shenjies88.practice.im.common.constant;

/**
 * @author shenjies88
 * @since 2020/11/5-9:54 PM
 */
public abstract class RedisKeys {

    private static final String PREFIX = "im";
    private static final String TOKEN = PREFIX + ":token";
    private static final String LIVE_TOKEN = PREFIX + ":live-token";
    private static final String USER_NETTY_LOGIN = PREFIX + ":user-netty-login";

    /**
     * 生成token key
     * 格式: im:token:tokenData
     *
     * @param token
     * @return
     */
    public static String createToken(String token) {
        return TOKEN + ":" + token;
    }

    /**
     * 生成 live-token key
     * 格式: im:live-token:id
     *
     * @param id
     * @return
     */
    public static String createLiveToken(Integer id) {
        return LIVE_TOKEN + ":" + id;
    }

    /**
     * 生成 用户netty登陆地址 key
     * 格式 im:user-netty-login:id
     *
     * @param id
     * @return
     */
    public static String createUserNettyLogin(Integer id) {
        return USER_NETTY_LOGIN + ":" + id;
    }
}
