package com.shenjies88.practice.im.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * im_db.group
 * 群
 *
 * @author shenjies88
 * @since 2020-11-06
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupDO {

    /**
     * column: id
     * 主键
     */
    private Integer id;

    /**
     * column: create_time
     * 创建时间
     */
    private Date createTime;

}