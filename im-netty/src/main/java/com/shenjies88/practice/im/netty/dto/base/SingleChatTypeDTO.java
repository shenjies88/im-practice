package com.shenjies88.practice.im.netty.dto.base;

import lombok.Data;

/**
 * 私聊
 *
 * @author shenjies88
 * @since 2020/11/04/15:50
 */
@Data
public class SingleChatTypeDTO {

    /**
     * 目标会员id
     */
    private Integer toMemberId;

}
