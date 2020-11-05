package com.shenjies88.practice.im.netty.dto;

import com.shenjies88.practice.im.netty.dto.base.GroupChatTypeDTO;
import lombok.Data;

/**
 * 群聊-文本
 *
 * @author shenjies88
 * @since 2020/11/04/15:51
 */
@Data
public class GroupChatTxtDTO extends GroupChatTypeDTO {

    /**
     * 消息
     */
    private String msg;
}
