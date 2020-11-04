package com.shenjies88.im.netty.dto.base;

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
     * 目标用户id
     */
    private Integer memberId;

}
