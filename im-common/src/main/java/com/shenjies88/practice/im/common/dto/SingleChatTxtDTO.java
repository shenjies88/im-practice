package com.shenjies88.practice.im.common.dto;

import com.shenjies88.practice.im.common.dto.base.SingleChatTypeDTO;
import lombok.Data;

/**
 * 私聊-文本
 *
 * @author shenjies88
 * @since 2020/11/04/15:51
 */
@Data
public class SingleChatTxtDTO extends SingleChatTypeDTO {

    /**
     * 消息
     */
    private String msg;
}
