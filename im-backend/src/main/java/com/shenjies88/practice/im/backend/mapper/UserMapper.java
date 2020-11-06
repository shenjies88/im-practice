package com.shenjies88.practice.im.backend.mapper;

import com.shenjies88.practice.im.backend.entity.UserDO;
import org.apache.ibatis.annotations.Param;

import java.util.Set;

/**
 * @author shenjies88
 * @since 2020/11/5-9:40 PM
 */
public interface UserMapper {

    /**
     * 根据账号获取
     *
     * @param account 账号
     * @return 实体
     */
    UserDO getByAccount(@Param("account") String account);

    /**
     * 插入
     *
     * @param entity 实体
     */
    void insert(UserDO entity);

    /**
     * 根据id count
     *
     * @param memberIdList 会员id列表
     * @return 统计数
     */
    int countById(@Param("memberIdList") Set<Integer> memberIdList);
}
