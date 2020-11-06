package com.shenjies88.practice.im.backend.utils;

import com.shenjies88.practice.im.common.vo.ContextTokenVO;

/**
 * @author shenjies88
 * @since 2020/11/6-9:19 AM
 */
public class TokenUtil {

    public static void save(ContextTokenVO vo) {
        ThreadContextUtil.set(vo);
    }

    public static ContextTokenVO getContextToken() {
        return (ContextTokenVO) ThreadContextUtil.get();
    }

    public static void clean() {
        ThreadContextUtil.remove();
    }
}
