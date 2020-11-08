package com.shenjies88.practice.im.backend.service;

import com.shenjies88.practice.im.backend.entity.GroupDO;
import com.shenjies88.practice.im.backend.entity.GroupMemberDO;
import com.shenjies88.practice.im.backend.mapper.GroupMapper;
import com.shenjies88.practice.im.backend.mapper.GroupMemberMapper;
import com.shenjies88.practice.im.backend.mapper.UserMapper;
import com.shenjies88.practice.im.common.bean.manager.MyCacheManager;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author shenjies88
 * @since 2020/11/6-2:42 PM
 */
@AllArgsConstructor(onConstructor_ = @Autowired)
@Service
public class GroupService {

    private final UserMapper userMapper;
    private final GroupMapper groupMapper;
    private final GroupMemberMapper groupMemberMapper;
    private final MyCacheManager cacheManager;

    private List<GroupMemberDO> createGroupMembers(Integer groupId, Set<Integer> memberIdList) {
        List<GroupMemberDO> result = new ArrayList<>();
        Date now = new Date();
        memberIdList.forEach(memberId ->
                result.add(GroupMemberDO.builder().groupId(groupId).memberId(memberId).createTime(now).build())
        );
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public Integer createGroup(Set<Integer> memberIdList) {
        //有效的会员id
        Assert.isTrue(memberIdList.size() == userMapper.countById(memberIdList), "存在非法用户");
        //建群
        GroupDO group = GroupDO.builder().createTime(new Date()).build();
        groupMapper.insert(group);
        //插入群成员
        groupMemberMapper.insertBatch(createGroupMembers(group.getId(), memberIdList));
        //redis群内上线
        cacheManager.saveGroupOnline(group.getId(), memberIdList);
        return group.getId();
    }
}
