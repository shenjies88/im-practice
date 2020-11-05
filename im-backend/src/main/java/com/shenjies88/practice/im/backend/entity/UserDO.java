package com.shenjies88.practice.im.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * im_db.user
 * 用户
 *
 * @author shenjies88
 * @since 2020-11-05
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDO {

    /**
     * column: id
     * 主键
     */
    private Integer id;

    /**
     * column: account
     * 账号
     */
    private String account;

    /**
     * column: pwd
     * 密码
     */
    private String pwd;

    /**
     * column: nickname
     * 昵称
     */
    private String nickname;

    /**
     * column: creat_time
     * 创建时间
     */
    private Date creatTime;

}