package com.shenjies88.practice.im.backend.manager;

import com.shenjies88.practice.im.backend.mapper.GroupMemberMapper;
import com.shenjies88.practice.im.backend.utils.TokenUtil;
import com.shenjies88.practice.im.common.bean.manager.MyCacheManager;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author shenjies88
 * @since 2020/11/8-12:24 PM
 */
@AllArgsConstructor(onConstructor_ = @Autowired)
@Component
public class GroupMemberManager {

    private final GroupMemberMapper groupMemberMapper;
    private final MyCacheManager cacheManager;

    /**
     * 当前会员所在的所有群上线
     *
     * @param memberId 会员id
     */
    public void groupOnline(Integer memberId) {
        List<Integer> groupIdList = groupMemberMapper.findGroupIdByMemberId(memberId);
        if (!CollectionUtils.isEmpty(groupIdList)) {
            Set<Integer> set = new HashSet<>();
            set.add(memberId);
            groupIdList.forEach(groupId -> {
                cacheManager.saveGroupOnline(groupId, set);
                cacheManager.removeGroupOffline(groupId, set);
            });
        }
    }

    /**
     * 当前会员所在的所有群下线
     */
    public void groupOffline() {
        List<Integer> groupIdList = groupMemberMapper.findGroupIdByMemberId(TokenUtil.getContextToken().getId());
        if (!CollectionUtils.isEmpty(groupIdList)) {
            Set<Integer> set = new HashSet<>();
            set.add(TokenUtil.getContextToken().getId());
            groupIdList.forEach(groupId -> {
                cacheManager.saveGroupOffline(groupId, set);
                cacheManager.removeGroupOnline(groupId, set);
            });
        }
    }
}
