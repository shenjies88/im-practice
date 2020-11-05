package com.shenjies88.practice.im.backend.utils;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * 正则校验工具
 *
 * @author shenjies88
 * @since 2020/11/5-5:52 PM
 */
public class RegularUtil {

    /**
     * 手机号正则
     */
    private static final String REGEX_MOBILE = "((\\+86|0086)?\\s*)((134[0-8]\\d{7})|(((13([0-3]|[5-9]))|(14[5-9])|15([0-3]|[5-9])|(16(2|[5-7]))|17([0-3]|[5-8])|18[0-9]|19(1|[8-9]))\\d{8})|(14(0|1|4)0\\d{7})|(1740([0-5]|[6-9]|[10-12])\\d{7}))";

    /**
     * 密码正则
     * 最短6位，最长16位 {6,16}
     * 可以包含小写大母 [a-z] 和大写字母 [A-Z]
     * 可以包含数字 [0-9]
     * 可以包含下划线 [ _ ] 和减号 [ - ]
     */
    private static final String PWD_REGEX = "^[\\w_-]{6,16}$";

    /**
     * 校验手机号
     *
     * @param phone 手机号
     * @return 结果
     */
    public static boolean isPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return false;
        }
        return Pattern.matches(REGEX_MOBILE, phone);
    }

    /**
     * 校验合法密码
     *
     * @param pwd 密码
     * @return 结果
     */
    public static boolean legalPassword(String pwd) {
        if (StringUtils.isEmpty(pwd)) {
            return false;
        }
        if (pwd.length() < 8) {
            return false;
        }
        return Pattern.matches(PWD_REGEX, pwd);
    }
}
