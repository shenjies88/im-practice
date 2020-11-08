package com.shenjies88.practice.im.backend.mapper;

import com.shenjies88.practice.im.backend.entity.GroupMemberDO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author shenjies88
 * @since 2020/11/6-4:05 PM
 */
public interface GroupMemberMapper {

    /**
     * 批量插入
     *
     * @param list 实体列表
     */
    void insertBatch(@Param("list") List<GroupMemberDO> list);

    /**
     * 根据会员id查找群id列表
     *
     * @param memberId
     * @return
     */
    List<Integer> findGroupIdByMemberId(Integer memberId);
}
