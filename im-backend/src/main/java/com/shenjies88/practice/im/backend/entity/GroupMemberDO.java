package com.shenjies88.practice.im.backend.entity;

import lombok.*;

import java.util.Date;

/**
 * im_db.group_member
 * 群成员
 *
 * @author shenjies88
 * @since 2020-11-06
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupMemberDO {

    /**
     * column: group_id
     * 群id
     */
    private Integer groupId;

    /**
     * column: member_id
     * 会员id
     */
    private Integer memberId;

    /**
     * column: create_time
     * 创建时间
     */
    private Date createTime;

}